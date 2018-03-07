<template>
    <div>
        <el-breadcrumb separator="/">
            <el-breadcrumb-item>控制面板</el-breadcrumb-item>
            <el-breadcrumb-item :to="{ path: '/index' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item :to="{ path: '/table' }">data</el-breadcrumb-item>
            <el-breadcrumb-item>角色管理({{ orderDate }})</el-breadcrumb-item>
        </el-breadcrumb>
        <div class="element__body">
            <div class="row">
                <el-form :inline="true" :model="form">
                    <el-form-item label="角色名:" class>
                        <el-input v-model="form.name" auto-complete="off"></el-input>
                    </el-form-item>
                    <el-select v-model="form.status" placeholder="请选择" class="process_select">
                        <el-option v-for="item in options" :key="item.key" :label="item.val"
                                   :value="item.key"></el-option>
                    </el-select>
                    <el-form-item label="描述:">
                        <el-input v-model="form.descr" auto-complete="off"></el-input>
                    </el-form-item>

                    <el-button class="btn1" @click="query(1)" type="primary">查询</el-button>
                    <el-button class="btn2" @click="reset" type="primary">重置</el-button>
                    <el-button class="btn2" @click="create" type="primary">新增</el-button>
                </el-form>
            </div>

            <div class="table">
                <el-table :data="dataList" border>
                    <el-table-column prop="id" label="#" width="80"></el-table-column>
                    <el-table-column prop="name" label="角色"></el-table-column>
                    <el-table-column prop="status" label="状态"></el-table-column>
                    <el-table-column prop="descr" label="描述"></el-table-column>
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
        <el-dialog title="角色管理-编辑" :visible.sync="dialogFormVisible">
            <el-row :gutter="20">
                <el-col :span="3" class="form-label">角色名：</el-col>
                <el-col :span="21">
                    <el-input v-model="dataListItem.name" placeholder="角色名"></el-input>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="3" class="form-label">状态：</el-col>
                <el-col :span="21">
                    <el-select v-model="dataListItem.status" placeholder="请选择">
                        <el-option v-for="item in options" :key="item.key" :label="item.val"
                                   :value="item.key"></el-option>
                    </el-select>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="3" class="form-label">描述：</el-col>
                <el-col :span="21">
                    <el-input v-model="dataListItem.descr" placeholder="描述"></el-input>
                </el-col>
            </el-row>

            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button v-if="!dataListItem.id" type="primary" @click="save(0)">新 增</el-button>
                <el-button v-if="dataListItem.id" type="primary" @click="save(1)">更 新</el-button>
            </div>
        </el-dialog>

        <!--<el-dialog title="检查确认" :visible.sync="dialogShowCheck" :before-close="closeDialog">-->
        <el-dialog title="检查确认" :visible.sync="dialogShowCheck">
            <form class="form">
                <div class="row" float:left>
                    <span class="label_status">状态</span>
                    <el-select v-model="status" clearable>
                        <el-option v-for="item in refusalCauses" :label="item.refuseEx" :value="item.id"></el-option>
                    </el-select>
                    <el-button float:right @click="updateOrderStatus()">确认</el-button>
                </div>
            </form>
        </el-dialog>

    </div>
</template>

<script>
    export default {
        name: 'roleList',
        data () {
            return {
                dialogFormVisible: false,
                title: '',
                pager: {
                    pageNo: 1,//第几页
                    pageSize: 10,//每页多少条
                    totalSize: 0, // 共多少条
                },
                dataList: [],
                dataListItem: {
                    id: '',
                    name: '',
                    status: '0',
                    descr: ''
                },
                form: {
                    name: '',
                    status: '0',
                    descr: ''
                },
                options: [
                    {key: '0', val: "可用"},
                    {key: '1', val: "不可用"}
                ],
                orderDate: '',
                orderStatus: '',
                fromDate: '',
                endDate: '',
                status: '请选择',
                dialogFormVisible: false,
                formLabelWidth: '100px',

                pickerOptions: {
                    disabledDate (time) {
                        return time.getTime() > Date.now()
                    }
                },
                tableData: [],
                total: 0,
                currentPage: 1,
                pageSize: 20,
                basePageSize: 20,
                pages: [20, 50, 100],
                detail: {
                    name: '',
                    phone: '',
                    idCard: '',
                    address: '',
                    marriage: '',
                    debitAccount: '',
                    loanAccount: '',
                    applyMoney: '',
                    loanRate: '',
                    overRate: '',
                    startDate: '',
                    endDate: ''
                },
                confirmAble: false,
                dialogShowCheck: false,
                refusalCauses: [],
                checkList: [],
                processStatus: '5',
                processes: []
            }
        },
        created () {
            // 判断是否登录
            if (!Utils.getSession('token')) {
                Utils.go('')
                return
            }
            const me = this
            me.confirmAble = +Utils.getSession('loginType') === 2 // 判断用户权限
            me.orderDate = Utils.getSession('orderDate')
            me.orderStatus = Utils.getSession('orderStatus')
            me.account = Utils.getSession('billQuery-account')
            me.permissionSet = Utils.getSession('permissionSet')
            me.isChecked = Utils.getSession('isChecked')
            me.detailStatus = Utils.getSession('detailStatus')
            const status = me.processStatus
            me.query();
        },
        methods: {
            //分页
            sizeChange(val){//pageSize 改变时会触发
                this.$data.pager.pageSize = val;
                this.query();
            },
            currentChange(val){//currentPage 改变时会触发
                this.$data.pager.pageNo = val;
                this.query();
            },
            reset(){
                this.$data.form = {};
                this.$data.form = {
                    name: '',
                    status: '0',
                    descr: ''
                }
            },

            create(){
                this.$data.form = {
                    name: '',
                    status: '0',
                    descr: ''
                }
                this.$data.dataListItem = {};
                this.$data.dataListItem.status = '0';
                this.$data.title = '角色管理 - 新增';
                this.$data.dialogFormVisible = true;
            },
            update(item){
                this.$data.dataListItem = Utils.extendObj(item);

                this.$data.dataListItem.status = !item.status ? "0" : "1";

                this.$data.dialogFormVisible = true;
                this.$data.title = '角色管理 - 编辑';
            },
            query(query){
                var pager = this.$data.pager;
                var aa = this.$data.form;
                var param = {};
                if (aa.name) {
                    param.name = aa.name;
                }
                if (aa.status == "0" || aa.status) {
                    param.status = aa.status;
                }
                if (aa.descr) {
                    param.descr = aa.descr;
                }
                param.pageNo = pager.pageNo;
                param.pageSize = pager.pageSize;

                Utils.ajax(Api.role.list, param, (success) => {
                    this.$data.dataList = success.data.list;
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
                var param = this.$data.dataListItem;
                var url = '';
                if (type) {
                    url = Api.role.upd;
                } else {
                    url = Api.role.add;
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
                            Utils.ajax(Api.role.del, param, 'Form Data', (success) => {
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
