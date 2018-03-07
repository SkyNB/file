<template>
    <div>
        <el-breadcrumb separator="/">
            <el-breadcrumb-item>控制面板</el-breadcrumb-item>
            <el-breadcrumb-item :to="{ path: '/index' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>黑名单</el-breadcrumb-item>
        </el-breadcrumb>

        <div class="element__body">

            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

            <el-button class="btn1" @click="add()" type="primary">账号添加</el-button>

            <div class="table">
                <el-table :data="tableData" border>
                    <el-table-column prop="account" label="账号"></el-table-column>
                    <el-table-column prop="password" label="密码"></el-table-column>
                    <el-table-column label="操作" width="200">
                        <template slot-scope="scope">
                            <el-button @click="del(scope.row)" type="text">删除</el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </div>


            <el-dialog title="个人征信账号管理-添加" :visible.sync="dialogFormVisible">
                <el-row :gutter="20">
                    <el-col :span="3" class="form-label">账号：</el-col>
                    <el-col :span="21">
                        <el-input v-model="add_account" placeholder="账号"></el-input>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="3" class="form-label">密码：</el-col>
                    <el-col :span="21">
                        <el-input v-model="add_password" placeholder="密码"></el-input>
                    </el-col>
                </el-row>

                <div slot="footer" class="dialog-footer">
                    <el-button @click="dialogFormVisible = false">取 消</el-button>
                    <el-button type="primary" @click="save(0)">新 增</el-button>
                </div>
            </el-dialog>


        </div>

    </div>
</template>

<script>
    export default {
        name: 'credit_account_manager',
        data () {
            return {
                pager: {
                    pageNo: 1,//第几页
                    pageSize: 10,//每页多少条
                    totalSize: 0, // 共多少条
                },
                tableData: [],
                add_account: '',
                add_password: '',
                dialogFormVisible: false,
                form: {
                    fromDate: '',
                    endDate: '',
                    status: ''
                }
            }
        },
        created () {
            var me = this;
            me.query();
        },
        methods: {
            query(){

            },
            del (id){

            },
            add () {
                var me = this;
                me.dialogFormVisible = true;
            },
            save(){
                var me = this;
                me.dialogFormVisible = false;
            }
        }
    }

</script>


<style lang="sass" scoped>
    @import './index.scss';
    .upload_file{
        .el-upload, .el-upload-dragger{
            width: 100%;

    }
    }
</style>
