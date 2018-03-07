<template>
    <div>
        <el-breadcrumb separator="/">
            <el-breadcrumb-item>控制面板</el-breadcrumb-item>
            <el-breadcrumb-item :to="{ path: '/index' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item :to="{ path: '/table' }">data</el-breadcrumb-item>
            <el-breadcrumb-item>资源管理({{}})</el-breadcrumb-item>
        </el-breadcrumb>
        <div class="element__body">
            <div class="row">
                <el-form :inline="true" :model="form">
                    <el-form-item label="资源名:" class>
                        <el-input v-model="form.query.name" auto-complete="off" name="name"
                                  @blur="inputChange"></el-input>
                    </el-form-item>
                    <el-select v-model="form.query.status" placeholder="请选择" class="process_select">
                        <el-option v-for="item in options.status" :key="item.key" :label="item.val"
                                   :value="item.key"></el-option>
                    </el-select>
                    <el-form-item label="描述:">
                        <el-input v-model="form.query.descr" auto-complete="off" name="name"
                                  @blur="inputChange"></el-input>
                    </el-form-item>

                    <el-button class="btn1" @click="query(1)" type="primary">查询</el-button>
                    <!--<el-button class="btn2" @click="reset" type="primary">重置</el-button>-->
                    <el-button class="btn2" @click="create" type="primary">新增</el-button>
                </el-form>
            </div>

            <div class="table">
                <el-table :data="dataList" border>
                    <el-table-column prop="id" label="#" width="80"></el-table-column>
                    <el-table-column prop="typ" label="资源类型"></el-table-column>
                    <el-table-column prop="name" label="资源名"></el-table-column>
                    <el-table-column prop="val" label="资源值"></el-table-column>
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


        <el-dialog title="资源管理-编辑" :visible.sync="dialogFormVisible">
            <el-row :gutter="20">
                <el-col :span="3" class="form-label">父节点：</el-col>
                <el-col :span="21">
                    <tree-select
                            :treeData="tree.resources.treeData"
                            :treeProps="tree.resources.treeProps"
                            v-model="form.edit.pid"
                            :multiple="false"
                            placeholder="请选择父节点"
                            @errorCallback="showTreeError"
                            @setSelectedId="setSelectedId">
                    </tree-select>

                    <!-- <el-autocomplete
                             v-model="form.edit.pid"
                             :fetch-suggestions="listResourceTree"
                             placeholder="请输入内容"
                             @select="handleSelect"
                     ></el-autocomplete>-->
                    <!--<el-select v-model="form.pid" placeholder="请选择">
                        <el-option v-for="item in options.ids" :key="item.key" :label="item.val"
                                   :value="item.key"></el-option>
                    </el-select>-->
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="3" class="form-label">资源名：</el-col>
                <el-col :span="21">
                    <el-input v-model="form.edit.name" placeholder="资源名"></el-input>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="3" class="form-label">资源值：</el-col>
                <el-col :span="21">
                    <el-input v-model="form.edit.val" placeholder="资源值"></el-input>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="3" class="form-label">排序：</el-col>
                <el-col :span="21">
                    <el-input v-model="form.edit.sort" placeholder="排序"></el-input>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="3" class="form-label">描述：</el-col>
                <el-col :span="21">
                    <el-input v-model="form.edit.descr" placeholder="描述"></el-input>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="3" class="form-label">资源图标：</el-col>
                <el-col :span="21">
                    <el-select v-model="form.edit.icon" placeholder="请选择">
                        <el-option v-for="item in options.icons" :key="item.key" :label="item.val"
                                   :value="item.key"></el-option>
                    </el-select>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="3" class="form-label">打开方式：</el-col>
                <el-col :span="21">
                    <el-select v-model="form.edit.target" placeholder="请选择">
                        <el-option v-for="item in options.targets" :key="item.key" :label="item.val"
                                   :value="item.key"></el-option>
                    </el-select>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="3" class="form-label">资源类型：</el-col>
                <el-col :span="21">
                    <el-select v-model="form.edit.typ" placeholder="请选择">
                        <el-option v-for="item in options.types" :key="item.key" :label="item.val"
                                   :value="item.key"></el-option>
                    </el-select>
                </el-col>
            </el-row>

            <el-row :gutter="20">
                <el-col :span="3" class="form-label">状态：</el-col>
                <el-col :span="21">
                    <el-select v-model="form.edit.status" placeholder="请选择">
                        <el-option v-for="item in options.status" :key="item.key" :label="item.val"
                                   :value="item.key"></el-option>
                    </el-select>
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
    import treeSelect from '../../components/tree-select/tree.vue'
    export default {
        name: 'resourcesList',
        data () {
            return {
                title: '',
                dialogFormVisible: false,
                dialogShowCheck: false,
                formLabelWidth: '100px',
                dataList: [],

                pager: {
                    pageNo: 1,
                    pageSize: 10,
                    totalSize: 0,
                },

                form: {
                    edit: {
                        id: '',
                        pid: '0',
                        val: '',
                        name: '',
                        level:'1',
                        sort: '',
                        descr: '',
                        target: '0',
                        icon: '0',
                        typ: '0',
                        status: '0'
                    },
                    query: {
                        name: '',
                        descr: '',
                        status: '0'
                    }
                },

                options: {
                    status: [{
                        key: '0', val: "可用"
                    }, {
                        key: '1', val: "不可用"
                    }],

                    types: [{
                        key: '0', val: 'url'
                    }, {
                        key: '1', val: 'menu'
                    }, {
                        key: '2', val: 'butto'
                    }],

                    targets: [{
                        key: '0', val: '当前窗口'
                    }, {
                        key: '1', val: '新窗口'
                    }],

                    icons: [{
                        key: '0', val: 'icon1'
                    }, {
                        key: '1', val: 'icon2'
                    }],

                    ids: [],
                },
                tree: {
                    resources: {
                        treeSelected: '',
                        treeSelectArr: [],
                        treeData: [],
                        treeProps: {
                            label: "name",
                            children: "childs",
                            level: "level"
                        }
                    }
                },

                pickerOptions: {
                    disabledDate (time) {
                        return time.getTime() > Date.now()
                    }
                }
            }
        },
        components: {
            treeSelect
        },
        created () {
            // 判断是否登录
            if (!Utils.getSession('token')) {
                Utils.go('')
                return
            }

            this.query();


        },
        mounted(){

            /* Utils.ajax(Api.resources.listTree, param, (success) => {
             this.tree.resourcesTree = success.data.list;
             });*/

            let i = 75170;
            setInterval(function () {
                i += 1;
                //this.treeSelectArr = [i+1, i+2, i+3];
                //this.treeSelected = i;
            }.bind(this), 2000)
        },

        methods: {
            // 下拉树搜索
            setSelectedId(val){
                if(val instanceof Array){
                    this.$data.tree.resources.treeSelectArr = val;
                }else{
                    this.$data.form.edit.pid = val.id;
                    this.$data.form.edit.level = val.level+1;
                }
            },
            showTreeError(error){
                //Message.info(error.message);
            },

            inputChange(event){
                this.$data.form.query[event.target.name] = event.target.value;
            },

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

            },

            /////////////////////////////// create ///////////////////////////////////
            create(){
                var self = this;
                this.$data.form.edit.id = '';
                this.$data.form.edit.val = '';
                this.$data.form.edit.sort = '';
                this.$data.form.edit.name = '';
                this.$data.form.edit.descr = '';
                this.$data.title = '资源管理 - 新增';
                this.$data.dialogFormVisible = true;
                // 下拉选择
                Utils.ajax(Api.resources.listTree, null, (success) => {
                    self.$data.tree.resources.treeData = success.data;
                });
            },
            update(item){
                var self = this;
                // 下拉选择
                Utils.ajax(Api.resources.listTree, null, (success) => {
                    self.$data.dialogFormVisible = true;
                    self.$data.title = '资源管理 - 编辑';
                    self.$data.tree.resources.treeData = success.data;
                    var arr = {};
                    for (var i in item) {
                        arr[i] = item[i] + '';
                    }
                    setTimeout(function(){
                        self.$data.form.edit = arr;
                    },100)

                });



            },
            query(query){
                var pager = this.$data.pager;
                var param = this.$data.form.query;

                param.pageNo = pager.pageNo;
                param.pageSize = pager.pageSize;

                Utils.ajax(Api.resources.list, param, (success) => {
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
                var param = this.$data.form.edit;
                var url = '';
                if (type) {
                    url = Api.resources.upd;
                } else {
                    url = Api.resources.add;
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
        },

    }
</script>




<style>
    ul,li,ol{
        list-style-type: none;
        list-style:none;
        padding:0;
        margin:0;
    }
</style>

<style lang="sass" scoped>
    @import "./index";
</style>
