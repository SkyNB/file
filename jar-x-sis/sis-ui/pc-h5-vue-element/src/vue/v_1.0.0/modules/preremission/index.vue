<template>
    <div>
        <el-breadcrumb separator="/">
            <el-breadcrumb-item>控制面板</el-breadcrumb-item>
            <el-breadcrumb-item :to="{ path: '/index' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>所有订单</el-breadcrumb-item>
        </el-breadcrumb>
        <div class="element__body">

            <div class="row">
                <el-form :inline="true">

                    <el-form-item label="姓名:">
                        <el-input v-model="query_name" auto-complete="off"></el-input>
                    </el-form-item>

                    <el-form-item label="身份证号:">
                        <el-input v-model="query_id_card" auto-complete="off"></el-input>
                    </el-form-item>

                    <el-form-item>
                        <el-button @click="query" class="query_btn" type="primary">查询</el-button>
                    </el-form-item>

                </el-form>
            </div>
			<div id="htmldisplay">
				<iframe :src='credit_url' height='1000px' width='720px'></iframe>
			</div>
        </div>

    </div>
</template>

<script>
    export default {
        name: 'preremission',
        data () {
            return {
            	credit_url:''
            }
        },
        created () {
            const me = this;
            me.orderDate = Utils.getSession('orderDate');
            me.orderStatus = Utils.getSession('orderStatus');
            me.isChecked = Utils.getSession('isChecked');
            me.detailStatus = Utils.getSession('detailStatus');
            // 判断是否登录
            if (!Utils.getSession('token')) {
                window.location.href = ress__+ '/#/' + 'login';
            }
            //验证session是否有效
            var sessionCode = Utils.getSession('token');
            var item = {
                sessionCode: sessionCode
            };
            Utils.ajax(Api.user.validSession, item, (res) => {
                if (+res.status != 1) {
                    window.location.href = ress__+ '/#/' + 'login';
                    return;
                }
            });
        },
        methods: {
            query(){
                var me = this;
                var item = {
                    name: me.query_name,
                    idCard: me.query_id_card
                }
                Utils.ajax(Api.order.queryCredit, item, (res) => {
                    if(res.status == 1) {
                    	me.credit_url = res.data;
                    	return;
                    }
                });
            }
        }
    }
</script>

<style lang="sass" scoped>
    @import "./index";
</style>
