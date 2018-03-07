<template>
    <div>
        <el-breadcrumb separator="/">
            <el-breadcrumb-item>控制面板</el-breadcrumb-item>
            <el-breadcrumb-item :to="{ path: '/index' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>账号管理</el-breadcrumb-item>
        </el-breadcrumb>
        <div class="element__body">
            <div class="row">
                <el-form :inline="true" :model="form.query">

                    <el-form-item label="账号:" class>
                        <el-input v-model="form.query.account" auto-complete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="手机号:" class>
                        <el-input v-model="form.query.cellphone" auto-complete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="邮箱:" class>
                        <el-input v-model="form.query.email" auto-complete="off"></el-input>
                    </el-form-item>

                    <el-select v-model="form.query.status" placeholder="请选择" class="process_select">
                        <el-option v-for="item in options.accountStatus" :key="item.key" :label="item.val"
                                   :value="item.key"></el-option>
                    </el-select>

                    <el-button class="btn1" @click="query(1)" type="primary">查询</el-button>
                    <el-button class="btn2" @click="reset" type="primary">重置</el-button>
                    <el-button class="btn2" @click="create" type="primary">新增</el-button>
                </el-form>
            </div>

            <div class="table">
                <el-table :data="dataList" border>
                    <el-table-column prop="id" label="#" width="80"></el-table-column>
                    <el-table-column prop="account" label="账号"></el-table-column>
                    <el-table-column prop="nick" label="昵称"></el-table-column>
                    <el-table-column prop="status" label="状态"></el-table-column>
                    <el-table-column prop="typ" label="类型"></el-table-column>
                    <el-table-column label="操作" width="200">
                        <template slot-scope="scope">
                            <el-button @click="update(scope.row)" type="text">编辑</el-button>
                            <el-button @click="del(scope.row)" type="text">删除</el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <el-pagination
                        @size-change="sizeChange"
                        @current-change="currentChange"
                        :current-page="pager.pageNo"
                        :page-sizes="[10, 20, 50]"
                        :page-size="pager.pageSize"
                        layout="sizes, prev, pager, next ,total,  jumper"
                        :total="pager.totalSize">
                </el-pagination>
            </div>


        </div>
        <el-dialog title="账号管理-编辑" :visible.sync="dialogFormVisible">
            <el-row :gutter="20">
                <el-col :span="3" class="form-label">账号：</el-col>
                <el-col :span="21">
                    <el-input v-model="form.edit.account" placeholder="账号"></el-input>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="3" class="form-label">密码：</el-col>
                <el-col :span="21">
                    <el-input v-model="form.edit.pwd" placeholder="密码"></el-input>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="3" class="form-label">昵称：</el-col>
                <el-col :span="21">
                    <el-input v-model="form.edit.nick" placeholder="昵称"></el-input>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="3" class="form-label">类型：</el-col>
                <el-col :span="21">
                    <el-select v-model="form.edit.typ" placeholder="请选择">
                        <el-option v-for="item in options.accountTypes" :key="item.key" :label="item.val"
                                   :value="item.key"></el-option>
                    </el-select>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="3" class="form-label">状态：</el-col>
                <el-col :span="21">
                    <el-select v-model="form.edit.status" placeholder="请选择">
                        <el-option v-for="item in options.accountStatus" :key="item.key" :label="item.val"
                                   :value="item.key"></el-option>
                    </el-select>
                </el-col>
            </el-row>


            <el-row :gutter="20">
                <el-col :span="3" class="form-label">角色：</el-col>
                <el-col :span="21">
                    <el-checkbox :indeterminate="options.roleOptions.isIndeterminate" v-model="options.roleOptions.checkAll"
                                 @change="handleCheckAllChange">全选
                    </el-checkbox>

                    <el-checkbox-group v-model="options.roleOptions.checkedRoles" @change="handleCheckedRolesChange">
                        <el-checkbox v-for="role in options.roleOptions.roles" :label="role" :key="role">{{role}}</el-checkbox>
                    </el-checkbox-group>
                </el-col>
            </el-row>


            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button v-if="!form.edit.id" type="primary" @click="save(0)">新 增</el-button>
                <el-button v-if="form.edit.id" type="primary" @click="save(1)">更 新</el-button>
            </div>
        </el-dialog>

    </div>
</template>

<script>
    export default {
        name: 'account',
        data () {
            return {
                title: '',
                dataList: [],
                dialogShowCheck: false,
                dialogFormVisible: false,
                // 分页
                pager: {
                    pageNo: 1,
                    pageSize: 10,
                    totalSize: 0,
                },

                // 表单
                form: {
                    edit: {
                        id: '',
                        account: '',
                        pwd: '',
                        typ: '0',
                        nick: '',
                        status: '0'
                    },
                    query: {
                        id: '',
                        account: '',
                        pwd: '',
                        typ: '0',
                        nick: '',
                        status: '0'
                    }
                },

                // 下拉选项
                options: {
                    accountStatus: [
                        {key: '0', val: "可用"},
                        {key: '1', val: "不可用"}
                    ],

                    accountTypes: [
                        {key: '0', val: "个人"},
                        {key: '1', val: "企业"}
                    ],

                    roleOptions: {
                        checkAll: true,
                        checkedRoles: [],
                        roles: [],
                        isIndeterminate: true
                    },
                }
            }
        },
        created () {
            // 判断是否登录
            if (!Utils.getSession('token')) {
                Utils.go('')
                return
            }
            const me = this
            me.confirmAble = +Utils.getSession('loginType') === 2
            me.query();

        },
        methods: {
            handleCheckAllChange(event) {
                this.options.roleOptions.checkedRoles = event.target.checked ? this.options.roleOptions.roles : [];
                this.options.roleOptions.isIndeterminate = false;
            },
            handleCheckedRolesChange(value) {
                let checkedCount = value.length;
                this.options.roleOptions.checkAll = checkedCount === this.options.roleOptions.roles.length;
                this.options.roleOptions.isIndeterminate = checkedCount > 0 && checkedCount < this.options.roleOptions.roles.length;
            },
            //分页pageSize 改变时会触发
            sizeChange(val){
                this.$data.pager.pageSize = val;
                this.query();
            },
            //currentPage 改变时会触发
            currentChange(val){
                this.$data.pager.pageNo = val;
                this.query();
            },
            reset(){
                this.$data.form.query = {};
                this.$data.form.query = {
                    name: '',
                    status: '0',
                    descr: ''
                }
            },

            create(){
                this.$data.title = '账号管理 - 新增';
                this.$data.dialogFormVisible = true;
                this.$data.form.edit = {
                    account: '',
                    pwd: '',
                    typ: '0',
                    nick: '',
                    status: '0'

                };
                Utils.ajax(Api.role.selectRole, null, (success) => {
                    this.options.roleOptions.roles = success.data;
                });
            },
            update(item){
                Utils.ajax(Api.role.selectRole, null, (success) => {
                    this.options.roleOptions.roles = success.data;
                });
                var arrObj = null;
                if (item.roles instanceof Array) {
                    arrObj = new Array();
                    item.roles.forEach((temptItem) => {
                        arrObj.push(temptItem.name);
                    })
                }
                this.$data.title = '角色管理 - 编辑';
                this.$data.dialogFormVisible = true;
                this.options.roleOptions.checkedRoles = arrObj;
                this.$data.form.edit = Utils.extendObj(item);
                this.$data.form.edit.id = item.id;
            },
            query(query){
                var pager = this.$data.pager;
                var aa = this.$data.form.query;
                var param = {};
                if (aa.account) {
                    param.account = aa.account;
                }
                if(aa.email) {
                    param.email = aa.email;
                }
                if(aa.cellphone) {
                    param.cellPhone = aa.cellphone;
                }
                if (aa.status == "0" || aa.status) {
                    param.status = aa.status;
                }
                if (aa.descr) {
                    param.descr = aa.descr;
                }
                param.pageNo = pager.pageNo;
                param.pageSize = pager.pageSize;

                Utils.ajax(Api.account.paging, param, (success) => {
                    if (success.status == '1') {
                        this.$data.dataList = success.data.list;
                        if (this.$data.dataList instanceof Array) {
                            this.$data.dataList.forEach((item) => {
                                item.cg_status = (item.status == '0' ? '可用' : '不可用')
                                item.cg_typ = (item.typ == '0' ? '个人' : '企业')
                            })
                        }
                    }
                    if (query) {
                        pager.pageNo = 1;
                    } else {
                        pager.pageNo = pager.pageNo || 1;
                    }
                    pager.pageSize = pager.pageSize;
                    pager.totalSize = success.data.totalSize;
                });
            },
            save (type){
                var param = this.$data.form.edit;
                param.accountId = this.$data.form.edit.id;
                var temptName = '';
                this.options.roleOptions.checkedRoles.forEach((item) => {
                    temptName += item;
                    temptName += ',';
                })
                param.roleNames = temptName.substr(0, temptName.length - 1);
                var url = '';
                if (type === 0) {
                    url = Api.account.add;
                } else {
                    url = Api.account.upd;
                }
                Utils.ajax(url, param, (success) => {
                    Dialog.alert(success);
                    this.query();
                    this.$data.dialogFormVisible = false;
                });
            },
            del(item){
                var self = this;
                Dialog.alert("您确定要删除吗？", {
                    confirm: true,
                    callback: function (txt) {
                        if (txt === 'confirm') {//点击确定按钮
                            var param = {id: item.id};
                            Utils.ajax(Api.account.del, param, (success) => {
                                Dialog.message("删除成功!", 'success');
                                self.query();
                            });
                        }
                    }
                })
            }
        }
    }
</script>

<style lang="sass" scoped>
    @import "./index";
</style>
