<template>
    <div>
        <el-breadcrumb separator="/">
            <el-breadcrumb-item>控制面板</el-breadcrumb-item>
            <el-breadcrumb-item :to="{ path: '/index' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item :to="{ path: '/table' }">数据</el-breadcrumb-item>
            <el-breadcrumb-item>黑名单</el-breadcrumb-item>
        </el-breadcrumb>

        <div class="element__body">

            <el-form :inline="true" :model="form">
                <el-form-item label="" class>
                    <el-input v-model="form.name" placeholder="姓名" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="" class>
                    <el-input v-model="form.phone" placeholder="手机号" auto-complete="off"></el-input>
                </el-form-item>
                <el-select v-model="form.marriage" placeholder="是否已婚" class="process_select">
                    <el-option
                            v-for="item in marriage_process"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                    </el-option>
                </el-select>
                <el-form-item label="" class>
                    <el-input v-model="form.idCard" placeholder="身份证" auto-complete="off"></el-input>
                </el-form-item>

                <el-button class="search_btn" @click="search" type="primary">搜索</el-button>
                <el-button class="reset_btn" @click="reset" type="primary">重置</el-button>
                <el-button class="bulkLoad_btn" @click="bulk_load" type="primary">批量导入</el-button>
            </el-form>


            <div class="table">
                <el-table :data="tableData" border>
                    <el-table-column prop="name" label="客户姓名" width="120"></el-table-column>
                    <el-table-column prop="name" label="银行卡号" width="120"></el-table-column>
                    <el-table-column prop="name" label="证件类型" width="120"></el-table-column>
                    <el-table-column prop="name" label="身份证号" width="120"></el-table-column>
                    <el-table-column prop="name" label="电话" width="120"></el-table-column>
                    <el-table-column prop="name" label="家庭地址" width="120"></el-table-column>
                    <el-table-column prop="name" label="户籍所在地" width="120"></el-table-column>
                    <el-table-column prop="name" label="婚姻状况" width="120"></el-table-column>
                    <el-table-column prop="name" label="发证机关" width="120"></el-table-column>
                    <el-table-column prop="name" label="是否授信" width="120"></el-table-column>
                </el-table>
            </div>



        </div>

    </div>
</template>

<script>
    export default {
        name: 'order_list',
        data () {
            return {
                pager: {
                    pageNo: 1,//第几页
                    pageSize: 10,//每页多少条
                    totalSize: 0, // 共多少条
                },
                form: {
                    fromDate: '',
                    endDate: '',
                    status: ''
                }
            }
        },
        created () {

        },
        methods: {

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
