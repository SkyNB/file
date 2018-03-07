<template>
    <div>
        <el-breadcrumb separator="/">
            <el-breadcrumb-item>控制面板</el-breadcrumb-item>
            <el-breadcrumb-item :to="{ path: '/index' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>订单明细</el-breadcrumb-item>
        </el-breadcrumb>
        <div class="element__body">

            <div class="table">
                <h1>待处理订单:
                    <span class="orderColor">{{total}}</span>
                </h1>
                <BR/>
                <el-table :data="tableData" border>
                    <el-table-column label="订单编号" width="200">
                        <template slot-scope="scope">
                            <span class="light_color">{{ scope.row.orderId }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="renter" label="姓名" width="90"></el-table-column>
                    <el-table-column label="身份证号" width="150">
                        <template slot-scope="scope">
                            <el-tooltip :content="scope.row.idCard" placement="top-start">
                                <span class="light_color">{{ scope.row.idCardHide }}</span>
                            </el-tooltip>
                        </template>
                    </el-table-column>
                    <el-table-column label="手机号" width="120">
                        <template slot-scope="scope">
                            <el-tooltip :content="scope.row.phone" placement="top-start">
                                <span class="light_color">{{ scope.row.phoneHide }}</span>
                            </el-tooltip>
                        </template>
                    </el-table-column>
                    <el-table-column prop="money" label="申请金额(元)" width="120"></el-table-column>
                    <el-table-column prop="address" label="家庭地址" width="260"></el-table-column>
                    <!-- 加一列状态 -->
                    <el-table-column prop="operation" label="审核状态" width="260"></el-table-column>
                    <el-table-column label="操作">
                        <template slot-scope="scope">
                            <el-button  @click="tel_check(scope.row.id)" type="text">电核</el-button>
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            <el-button  @click="delay_confirm(scope.row.id)" type="text">延期处理</el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </div>


            <div class="table">
                <h1>延迟处理订单</h1>
                <BR/>
                <el-table :data="delay_tableData" border>
                    <el-table-column label="订单编号" width="200">
                        <template slot-scope="scope">
                            <span class="light_color">{{ scope.row.orderId }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="renter" label="姓名" width="90"></el-table-column>
                    <el-table-column label="身份证号" width="150">
                        <template slot-scope="scope">
                            <el-tooltip :content="scope.row.idCard" placement="top-start">
                                <span class="light_color">{{ scope.row.idCardHide }}</span>
                            </el-tooltip>
                        </template>
                    </el-table-column>
                    <el-table-column label="手机号" width="120">
                        <template slot-scope="scope">
                            <el-tooltip :content="scope.row.phone" placement="top-start">
                                <span class="light_color">{{ scope.row.phoneHide }}</span>
                            </el-tooltip>
                        </template>
                    </el-table-column>
                    <el-table-column prop="money" label="申请金额(元)" width="120"></el-table-column>
                    <el-table-column prop="address" label="家庭地址" width="260"></el-table-column>
                    <!-- 加一列状态 -->
                    <el-table-column prop="operation" label="审核状态" width="260"></el-table-column>
                    <el-table-column label="操作">
                        <template slot-scope="scope">
                            <el-button  @click="tel_check(scope.row.id)" type="text">电核</el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: 'investigating',
        data () {
            return {
                formLabelWidth: '100px',
                tableData: [],
                delay_tableData: []
            }
        },
        created () {
            const me = this;
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
            me.queryOrderByRound();
            // 获取待处理订单的个数
            var sessionCode = Utils.getSession('token');
            var param = {sessionCode:sessionCode,pageSeq:"1",pageSize:"20",status:"-99"};
            Utils.ajax(Api.order.queryOrderByCondition, param, (res) => {
                if (+res.status == 1) {
                    me.total = res.data.total;
                }
            });
        },

        methods: {
            handleSizeChange (val) {
                const me = this;
                me.pageSize = val;
                me.query({
                });
            },
            handleCurrentChange (val) {
                const me = this;
                me.currentPage = val;
                me.query({
                });
            },
            tel_check (id) {
                Utils.setSession('current_order_id', id);
                window.location.href = ress__+ '/#/' + 'tel_check';
            },
            // 选中,获取可以放款和拒绝放款的数据
            goBack () {
                router.go(-1)
            },
            //
            delay_confirm (orderId) {
                var me = this;
                var sessionCode = Utils.getSession('token');
                var item = {
                    sessionCode: sessionCode,
                    orderId: orderId,
                    status: '99'
                };
                Utils.ajax(Api.order.updateStatus, item, (res) => {
                    if(+res.status == 1) {
                        me.$message({
                            showClose: true,
                            message: '成功更新状态',
                            type: 'success'
                        });
                        me.queryOrderByRound();
                    }
                });
            },
            //循环调用
            queryOrderByRound(){
                var me = this;
                var sessionCode = Utils.getSession('token');
                var item = {
                    sessionCode: sessionCode
                };
                Utils.ajax(Api.order.getToBeAssignedOrder, item, (res) => {
                    me.tableData = [];
                    if ((+res.status == 1)&&(+res.data != null)&&(+res.data != '')) {
                        var data = res.data;
                        data.idCardHide = (data.idCardNum == null)?data.idCardNum:data.idCardNum.replace(/^(\d{6})\d+(\w{4})$/, '$1****$2');
                        data.phoneHide = (data.cellphone == null)?data.cellphone:data.cellphone.replace(/^(\d{3})\d+(\d{4})$/, '$1****$2');
                        data.debitAccountHide = (data.debitaccount == null)?data.debitaccount:data.debitaccount.replace(/^(\d{6})\d+(\d{3})$/, '$1****$2');
                        data.loadAccountHide = (data.loadaccount == null)?data.loadaccount:data.loadaccount.replace(/^(\d{6})\d+(\d{3})$/, '$1****$2');
                        data.addressHide = (data.homeadr == null)?data.homeadr:data.homeadr.replace(/^(.{6}).*!/, '$1****');
                        var operation = '';
                        if (item.checkStatus == '0') {
                            operation = '已拒绝'
                        } else if(item.checkStatus == '1') {
                            operation = '已通过';
                        } else {
                            operation = '待审批';
                        }
                        me.tableData.push({
                            id: data.id,
                            orderId: data.borrowNid,
                            renter: data.renter,
                            idCard: data.idCardNum,
                            idCardHide: data.idCardHide,
                            phone: data.cellphone,
                            phoneHide: data.phoneHide,
                            money: data.amount,
                            address: data.homeAddress,
                            checkStatus: data.checkStatus,
                            operation: operation
                        });
                    }
                });
                //
                Utils.ajax(Api.order.getDelayHandleOrders, {}, (res) => {
                    if ((+res.status == 1)&&(+res.data != null)&&(+res.data != '')) {
                        var data = res.data;
                        me.delay_tableData = [];
                        if((data != null) && (data instanceof Array)) {
                            data.forEach((temptItem) => {
                                temptItem.idCardHide = (temptItem.idCardNum == null)?temptItem.idCardNum:temptItem.idCardNum.replace(/^(\d{6})\d+(\w{4})$/, '$1****$2');
                                temptItem.phoneHide = (temptItem.cellphone == null)?temptItem.cellphone:temptItem.cellphone.replace(/^(\d{3})\d+(\d{4})$/, '$1****$2');
                                temptItem.debitAccountHide = (temptItem.debitaccount == null)?temptItem.debitaccount:temptItem.debitaccount.replace(/^(\d{6})\d+(\d{3})$/, '$1****$2');
                                temptItem.loadAccountHide = (temptItem.loadaccount == null)?temptItem.loadaccount:temptItem.loadaccount.replace(/^(\d{6})\d+(\d{3})$/, '$1****$2');
                                temptItem.addressHide = (temptItem.homeadr == null)?temptItem.homeadr:temptItem.homeadr.replace(/^(.{6}).*!/, '$1****');
                                var operation = '延迟处理';
                                me.delay_tableData.push({
                                    id: temptItem.id,
                                    orderId: temptItem.borrowNid,
                                    renter: temptItem.renter,
                                    idCard: temptItem.idCardNum,
                                    idCardHide: temptItem.idCardHide,
                                    phone: temptItem.cellphone,
                                    phoneHide: temptItem.phoneHide,
                                    money: temptItem.amount,
                                    address: temptItem.homeAddress,
                                    checkStatus: temptItem.checkStatus,
                                    operation: operation
                                });
                            })
                        }
                    }
                });

            }
        }
    }
</script>

<style lang="sass" scoped>
    @import "./index";
</style>
