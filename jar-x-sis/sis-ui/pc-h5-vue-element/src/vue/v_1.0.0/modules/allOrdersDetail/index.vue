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
                    <el-form-item label="申请日期:" class>
                        <el-date-picker
                                v-model="fromDate"
                                format="yyyy-MM-dd"
                                type="date"
                                placeholder="选择日期">
                        </el-date-picker>
                    </el-form-item>
                    <span class="split">-</span>
                    <el-date-picker
                            v-model="endDate"
                            format="yyyy-MM-dd"
                            type="date"
                            placeholder="选择日期">
                    </el-date-picker>
                    </el-date-picker>
                    <el-form-item label="姓名:">
                        <el-input v-model="query_name" auto-complete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="身份证号:">
                        <el-input v-model="query_id_card" auto-complete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="手机号:">
                        <el-input v-model="query_cell_phone" auto-complete="off"></el-input>
                    </el-form-item>

                    <el-select v-model="check_status" placeholder="请选择审核状态" class="process_select">
                        <el-option
                                v-for="item in status_process"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                        </el-option>
                    </el-select>

                    <el-form-item>
                        <el-button @click="query" class="query_btn" type="primary">查询</el-button>
                    </el-form-item>

                    <el-form-item>
                        <el-button @click="reset" class="reset_btn" type="primary">重置</el-button>
                    </el-form-item>

                </el-form>
            </div>

            <div class="table">
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
                        </template>
                    </el-table-column>
                </el-table>
            </div>

            <div class="pagination">
                <el-pagination
                        @size-change="handleSizeChange"
                        @current-change="handleCurrentChange"
                        :current-page="currentPage"
                        :page-sizes="pages"
                        :page-size="pageSize"
                        layout="total, sizes, prev, pager, next, jumper"
                        :total="+total">
                </el-pagination>
            </div>
        </div>

    </div>
</template>

<script>
    export default {
        name: 'allOrdersDetail',
        data () {
            return {
                //查询条件变量
                fromDate: '',
                endDate: '',
                query_name: '',
                query_id_card: '',
                query_cell_phone: '',
                //
                orderDate: '',
                orderStatus: '',
                formLabelWidth: '100px',
                pickerOptions: {
                    disabledDate (time) {
                        return time.getTime() > Date.now()
                    }
                },
                tableData: [],
                total: 0,
                //页数变量
                currentPage: 1,
                pageSize: 20,
                basePageSize: 20,
                pages: [20, 50, 100],
                //审核状态
                status_process: [{
                    value: '0',
                    label: '拒绝'
                }, {
                    value: '1',
                    label: '通过'
                }],
                check_status: '',
                //角色id
                role_id: ''
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
            //根据条件查询
            query() {
                var me = this;
                if(me.check_status == '') {
                    Dialog.alert( '请选择订单状态');
                    return;
                }
                var sessionCode = Utils.getSession('token');
                var temptFromDate = '';
                var temptEndDate = '';
                if(me.fromDate != '') {
                    temptFromDate = Utils.formatDate(new Date(me.fromDate), 'YYYY-MM-DD');
                }
                if(me.endDate != '') {
                    temptEndDate = Utils.formatDate(new Date(me.endDate), 'YYYY-MM-DD');
                }
                var item = {
                    fromDate: temptFromDate,
                    endDate: temptEndDate,
                    name: me.query_name,
                    idCardNum: me.query_id_card,
                    cellPhone: me.query_cell_phone,
                    pageSeq: me.currentPage,
                    pageSize: me.pageSize,
                    sessionCode: sessionCode,
                    status: me.check_status
                };
                Utils.ajax(Api.order.queryOrderByCondition, item, (res) => {
                    var me = this;
                    if (+res.status == 1) {
                        var data = res.data.orderItems;
                        me.total = res.data.total;
                        me.tableData = [];
                        if (data instanceof Array) {
                            data.forEach((item) => {
                                item.idCardHide = (item.idCardNum == null)?item.idCardNum:item.idCardNum.replace(/^(\d{6})\d+(\w{4})$/, '$1****$2');
                                item.phoneHide = (item.cellphone == null)?item.cellphone:item.cellphone.replace(/^(\d{3})\d+(\d{4})$/, '$1****$2');
                                item.debitAccountHide = (item.debitaccount == null)?item.debitaccount:item.debitaccount.replace(/^(\d{6})\d+(\d{3})$/, '$1****$2');
                                item.loadAccountHide = (item.loadaccount == null)?item.loadaccount:item.loadaccount.replace(/^(\d{6})\d+(\d{3})$/, '$1****$2');
                                item.addressHide = (item.homeadr == null)?item.homeadr:item.homeadr.replace(/^(.{6}).*!/, '$1****');
                                var operation = '';
                                if (item.checkStatus == '0') {
                                    operation = '已拒绝'
                                } else if(item.checkStatus == '1') {
                                    operation = '已通过';
                                } else {
                                    operation = '待审批';
                                }
                                me.tableData.push({
                                    id: item.id,
                                    orderId: item.borrowNid,
                                    renter: item.renter,
                                    idCard: item.idCardNum,
                                    idCardHide: item.idCardHide,
                                    phone: item.cellphone,
                                    phoneHide: item.phoneHide,
                                    money: item.amount,
                                    address: item.homeAddress,
                                    checkStatus: item.checkStatus,
                                    operation: operation
                                })
                            })
                        }
                    }
                });
            },
            //重置查询条件
            reset() {
                var me = this;
                me.fromDate = '';
                me.endDate = '';
                me.query_name = '';
                me.query_id_card = '';
                me.query_cell_phone= '';
                me.check_status = '';
            }
        }
    }
</script>

<style lang="sass" scoped>
    @import "./index";
</style>
