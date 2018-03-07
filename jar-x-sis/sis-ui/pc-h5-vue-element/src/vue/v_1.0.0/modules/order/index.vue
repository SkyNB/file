<template>
    <div>
        <el-breadcrumb separator="/">
            <el-breadcrumb-item>控制面板</el-breadcrumb-item>
            <el-breadcrumb-item :to="{ path: '/index' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item :to="{ path: '/table' }">data</el-breadcrumb-item>
            <el-breadcrumb-item>订单明细</el-breadcrumb-item>
        </el-breadcrumb>
        <div class="element__body">
            <div class="row">
                <el-form :inline="true" :model="form">
                    <el-form-item label="姓名:" class>
                        <el-input v-model="form.name" auto-complete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="手机号:">
                        <el-input v-model="form.phone" auto-complete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="身份证:">
                        <el-input v-model="form.idCard" auto-complete="off"></el-input>
                    </el-form-item>
                    <el-select v-model="age_processStatus" placeholder="请选择年龄段" class="process_select">
                        <el-option
                                v-for="item in age_process"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                        </el-option>
                    </el-select>
                    <el-select v-model="region_processStatus" placeholder="请选择区域" class="process_select">
                        <el-option
                                v-for="item in region_process"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                        </el-option>
                    </el-select>
                    <el-form-item v-if="detailStatus !='1'">
                        <el-select v-model="processStatus" placeholder="请选择" class="process_select">
                            <el-option
                                    v-for="item in processes"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="风控评分:">
                        <el-select v-model="risk_score" placeholder="-- 请选择 --" class="process_select">
                            <el-option
                                    v-for="item in riskScore_process"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="性别:">
                        <el-select v-model="order_sex" placeholder="-- 请选择 --" class="process_select">
                            <el-option
                                    v-for="item in sex_process"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="工作:" class>
                        <el-input v-model="form.work" auto-complete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="贷款额度:" class>
                        <el-input v-model="form.loanLimit" auto-complete="off"></el-input>
                    </el-form-item>
                    <el-button class="btn1" @click="search" type="primary">查询</el-button>
                    <el-button class="btn2" @click="reset" type="primary">重置</el-button>
                    <el-button v-if="detailStatus !='1' && role == '主任'" @click="confirmAllPlotOrders" class="query_btn">全部确认</el-button>
                </el-form>
            </div>

            <div class="table">
                <el-table :data="tableData" border>
                    <el-table-column label="订单编号" width="200">
                        <template slot-scope="scope">
                            <span class="light_color">{{ scope.row.orderId }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="name" label="姓名" width="90"></el-table-column>
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
                    <el-table-column prop="fromDate" label="合同开始日期" width="130"></el-table-column>
                    <el-table-column prop="endDate" label="合同到期日期" width="130"></el-table-column>
                    <!-- 加一列状态 -->
                    <el-table-column label="审核状态" width="130">
                        <template slot-scope="scope">
                            <el-tooltip :content="scope.row.resusalEx" placement="top-start"
                                        v-if="(scope.row.resusalEx !='') && (scope.row.resusalEx !=null)">
                                <span>{{scope.row.operation}}</span>
                            </el-tooltip>
                            <span v-if="(scope.row.resusalEx == '')||(scope.row.resusalEx == null)">{{scope.row.operation}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="操作">
                        <template slot-scope="scope">
                            <el-button @click="showDetail(scope.row.detail)" type="text">详细</el-button>
                            <el-button
                                    v-if="(role == '信贷员') && (isChecked =='0') && ((scope.row.checkStatus ==null) || (scope.row.checkStatus ==''))"
                                    @click="showCheck(scope.row.detail)" type="text">检查
                            </el-button>
                            <el-button
                                    v-if="(role == '主任') && (isChecked =='0') && (scope.row.checkStatus == '1')"
                                    @click="showCheck(scope.row.detail)" type="text">复核
                            </el-button>
                            <span v-if="(isChecked=='0')&&(((permissionSet=='1')&&(scope.row.checkStatus!=null)&&(scope.row.checkStatus!=''))||((permissionSet=='2')&&((scope.row.checkStatus=='2')||(scope.row.checkStatus=='3'))))">已操作</span>
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
        <el-dialog title="订单详情" :visible.sync="dialogTableVisible">
            <div class="order_detail_table">
                <table width="100%" cellspacing="0" cellpadding="0" border="1">
                    <tbody>
                    <tr>
                        <th>姓名</th>
                        <td>{{ detail.name }}</td>
                        <th>手机号</th>
                        <td>
                            <el-tooltip :content="detail.phone" placement="top-start">
                                <span>{{ detail.phoneHide }}</span>
                            </el-tooltip>
                        </td>
                        <th>身份证号</th>
                        <td>
                            <el-tooltip :content="detail.idCard" placement="top-start">
                                <span>{{ detail.idCardHide }}</span>
                            </el-tooltip>
                        </td>
                    </tr>
                    <tr>
                        <th>家庭地址</th>
                        <td colspan="3">
                            <el-tooltip :content="detail.address" placement="top-start">
                                <span>{{ detail.addressHide }}</span>
                            </el-tooltip>
                        </td>
                        <th>婚姻状况</th>
                        <td>{{ detail.marriage }}</td>
                    </tr>
                    <tr>
                        <th>扣款账号</th>
                        <td colspan="2">
                            <el-tooltip :content="detail.debitAccount" placement="top-start">
                                <span>{{ detail.debitAccountHide }}</span>
                            </el-tooltip>
                        </td>
                        <th>放款账号</th>
                        <td colspan="2">
                            <el-tooltip :content="detail.loanAccount" placement="top-start">
                                <span>{{ detail.loadAccountHide }}</span>
                            </el-tooltip>
                        </td>
                    </tr>
                    <tr>
                        <th>申请金额</th>
                        <td>{{ detail.applyMoney }}</td>
                        <th>贷款利率</th>
                        <td>{{ detail.loanRate }}&permil;</td>
                        <th>逾期浮动利率</th>
                        <td>{{ detail.overRate }}%</td>
                    </tr>
                    <tr>
                        <th>开始日期</th>
                        <td colspan="2">{{ detail.startDate }}</td>
                        <th>结束日期</th>
                        <td colspan="2">{{ detail.endDate }}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </el-dialog>

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
        name: 'orderList',
        data () {
            return {
                orderDate: '',
                orderStatus: '',
                fromDate: '',
                endDate: '',
                status: '请选择',
                dialogTableVisible: false,
                formLabelWidth: '100px',
                form: {
                    name: '',
                    phone: '',
                    idCard: ''
                },
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
                allRegion: [],
                checkList: [],
                processStatus: '5',
                processes: [],
                age_processStatus: '0',
                region_processStatus: '',
                risk_score: '',
                order_sex:  '',
            }
        },
        created () {
            const me = this
            me.confirmAble = +Utils.getSession('loginType') === 2 // 判断用户权限
            me.orderDate = Utils.getSession('orderDate')
            me.orderStatus = Utils.getSession('orderStatus')
            me.account = Utils.getSession('billQuery-account')
            me.permissionSet = Utils.getSession('permissionSet')
            me.isChecked = Utils.getSession('isChecked')
            me.detailStatus = Utils.getSession('detailStatus')
            me.role = Utils.getSession('role')
            const status = me.processStatus
            me.processes = [{
                value: '1',
                label: '进度：初步通过'
            }, {
                value: '2',
                label: '进度：初步拒绝'
            }, {
                value: '3',
                label: '进度：复核通过'
            }, {
                value: '4',
                label: '进度：复核拒绝'
            }, {
                value: '5',
                label: '状态：可以放款'
            }]
            me.age_process= [{
                value: '1',
                label: '18～35'
            }, {
                value: '2',
                label: '35～50'
            }, {
                value: '3',
                label: '50以上'
            }, {
                value: '0',
                label: '不限年龄段'
            }]
            // 风控评分
            me.riskScore_process = [{
                value:  '1',
                label:  '60～70'
            },{
                value:  '2',
                label:  '70～80'
            },{
                value:  '3',
                label:  '80～90'
            },{
                value:  '4',
                label:  '90～100'
            }]
            //  性别
            me.sex_process = [{
                value: '0',
                label: '男'
            },{
                value:  '1',
                label:  '女'
            }]
            me.region_process= [{
                value: '0',
                label: '不限地域'
            }]
            // 判断是否登录
            if (!Utils.getSession('token')) {
                window.location.href = ress__+ '/#/' + 'login'
            }
            //验证session是否有效
            var sessionCode = Utils.getSession('token');
            var item = {
                sessionCode: sessionCode
            };
            Utils.ajax(Api.user.validSession, item, (res) => {
                if (+res.status != 1) {
                    window.location.href = ress__+ '/#/' + 'login'
                    return;
                }
            });
            me.getListData({
                account: me.account,
                canLoan: status
            })
            me.getLoanResualCause()
            me.getAllRegion()

        },
        methods: {
            handleSizeChange (val) {
                const me = this
                me.pageSize = val
                me.getListData({
                    account: me.account
                })
            },
            handleCurrentChange (val) {
                const me = this
                me.currentPage = val
                const status = me.processStatus
                me.getListData({
                    account: me.account,
                    canLoan: status
                })
            },
            goLoan () {
                if (this.checkList.length === 2) {
                    this.checkList.shift()
                }
            },
            getListData (opts) {
                const me = this
                let param = {
                    customerCode: Utils.getSession('customerCode'),
                    pageSeq: me.currentPage,
                    pageSize: me.pageSize,
                    sessionCode: Utils.getSession('token'),
                    renter: me.form.name,
                    cellPhone: me.form.phone,
                    idCardNo: me.form.idCard,
                    age: me.form.age_processStatus,
                    region: me.form.region_processStatus,
                    risk_score: me.form.riskScore_process,
                    order_sex: me.form.sex_process
                }
                Utils.merge(param, opts);
                Utils.ajax(Api.order.orderlist, param,(res) => {
                    const me = this;
                    if(+res.status === 1) {
                        var data = res.data.orderItems;
                        me.total = res.data.total;
                        me.tableData = []
                        if (data instanceof Array) {
                            data.forEach((item) => {
                                item.idCardHide = item.idcardno // .replace(/^(\d{6})\d+(\w{4})$/, '$1****$2')
                                item.phoneHide = item.cellphone // .replace(/^(\d{3})\d+(\d{4})$/, '$1****$2')
                                item.debitAccountHide = item.debitaccount // .replace(/^(\d{6})\d+(\d{3})$/, '$1****$2')
                                item.loadAccountHide = item.loadaccount // .replace(/^(\d{6})\d+(\d{3})$/, '$1****$2')
                                item.addressHide = item.homeadr // .replace(/^(.{6}).*!/, '$1****')
                                var operation = ''
                                if (item.checkStatus === '0') {
                                    operation = '信贷员已通过'
                                } else if (item.checkStatus === '1') {
                                    operation = '信贷员已拒绝'
                                } else if (item.checkStatus === '2') {
                                    operation = '主任已通过'
                                } else if (item.checkStatus === '3') {
                                    operation = '主任已拒绝'
                                }
                                if (operation === '') {
                                    operation = '正常'
                                }
                                me.tableData.push({
                                    orderId: item.billNo,
                                    status: item.status === 0 ? '正常' : '退单',
                                    name: item.renter,
                                    idCard: item.idcardno,
                                    idCardHide: item.idCardHide,
                                    phone: item.cellphone,
                                    phoneHide: item.phoneHide,
                                    money: item.iamount,
                                    fromDate: Utils.formatDate(new Date(item.startdate), 'YYYY-MM-DD'),
                                    endDate: Utils.formatDate(new Date(item.expiredate), 'YYYY-MM-DD'),
                                    detail: item,
                                    checkStatus: item.checkStatus,
                                    operation: operation,
                                    resusalEx: item.resusalEx
                                })
                            })
                        }
                    }
                });
            },
            showDetail (detail) {
                const me = this
                me.detail = {
                    name: detail.renter,
                    phone: detail.cellphone,
                    phoneHide: detail.phoneHide,
                    idCard: detail.idcardno,
                    idCardHide: detail.idCardHide,
                    address: detail.homeadr,
                    addressHide: detail.addressHide,
                    marriage: detail.married,
                    debitAccount: detail.debitaccount,
                    debitAccountHide: detail.debitAccountHide,
                    loanAccount: detail.loadaccount,
                    loadAccountHide: detail.loadAccountHide,
                    applyMoney: detail.iamount,
                    loanRate: detail.loanRate,
                    overRate: detail.overdueRange,
                    startDate: Utils.formatDate(new Date(detail.startdate), 'YYYY-MM-DD'),
                    endDate: Utils.formatDate(new Date(detail.expiredate), 'YYYY-MM-DD')
                }
                me.dialogTableVisible = true
            },
            // 检查弹出框
            showCheck (detail) {
                const me = this
                me.detail = {}
                Utils.setSession('wait-update-id', detail.id)
                me.dialogShowCheck = true
            },
            // 选中,获取可以放款和拒绝放款的数据
            goBack () {
                router.go(-1)
            },
            confirmAllPlotOrders () {
                const me = this
                Utils.ajax(Api.order.confirmPlotOrder, {
                    account: me.account
                },(res) => {
                    if (+res.status === 1) {
                        me.$message({
                            showClose: true,
                            message: '确认成功',
                            type: 'success'
                        })
                        me.search()
                    } else {
                        me.$message({
                            showClose: true,
                            message: res.msg || '确认失败',
                            type: 'error'
                        })
                    }
                })

            },
            search () {
                const me = this
                me.form.name = me.form.name.trim()
                me.form.phone = me.form.phone.trim()
                me.form.idCard = me.form.idCard.trim()
                const status = me.processStatus
                me.getListData({
                    renter: me.form.name,
                    cellPhone: me.form.phone,
                    idCardNo: me.form.idCard,
                    account: me.account,
                    canLoan: status,
                    age: me.age_processStatus,
                    region: me.region_processStatus
                })
            },
            confirmOrder () {
                const me = this
                const sessionCode = Utils.getSession('sessionCode')
                const customerCode = Utils.getSession('customerCode')
                me.$http.post(Api.confirmOrder, {
                    customerCode: customerCode,
                    confirmDate: me.orderDate,
                    sessionCode: sessionCode
                }).then((res) => {
                    if (+res.data.status === 1) {
                        me.$message({
                            showClose: true,
                            message: '确认成功',
                            type: 'success'
                        })
                        me.orderStatus = '已确认'
                    } else {
                        me.$message({
                            showClose: true,
                            message: res.data.msg || '确认失败',
                            type: 'error'
                        })
                    }
                })
            },
            reset () {
                const me = this
                me.form.name = ''
                me.form.phone = ''
                me.form.idCard = ''
                me.checkList = []
                me.getListData({
                    account: me.account
                })
                me.region_processStatus = '0'
                me.age_processStatus = '0'
            },
            updateOrderStatus () {
                const me = this
                var temptStatus = '-1'
                var role = Utils.getSession('role')
                var billId = Utils.getSession('wait-update-id')
                const sessionCode = Utils.getSession('token')
                var refusalId = null
                if ((me.status === '') || (me.status === '请选择')) {
                    me.detail = {}
                    me.$message({
                        showClose: true,
                        message: '请选择其中一选项',
                        type: 'error'
                    })
                    return
                }
                if (me.status === '1') {
                    if (role === '信贷员') {
                        temptStatus = '0'
                    } else {
                        temptStatus = '2'
                    }
                } else if (me.status === '2') {
                    if (role === '信贷员') {
                        temptStatus = '1'
                    } else {
                        temptStatus = '3'
                    }
                    refusalId = '2'
                } else if (me.status === '3') {
                    if (role === '信贷员') {
                        temptStatus = '1'
                    } else {
                        temptStatus = '3'
                    }
                    refusalId = '3'
                }
                me.detail = {}
                me.dialogShowCheck = false

                Utils.ajax(Api.order.updateOrderStatus, {
                    id: billId,
                    status: temptStatus,
                    refusalId: refusalId,
                    sessionCode: sessionCode,
                    account: me.account
                },(res) => {
                    const reqStatus = me.processStatus
                    me.getListData({
                        account: me.account,
                        canLoan: reqStatus
                    })
                })



            },
            getLoanResualCause () {
                Utils.ajax(Api.order.getRefusalCase, {},(res) => {
                    const me = this
                    if((res.error == '00000000')&&(res.status == '1')) {
                        var data = res.data;
                        Utils.setSession('refuse-loan-cause', data)
                        me.refusalCauses = data
                    } else {
                        this.$message.error('请求拒绝放款原因接口失败!')
                    }
                })
            },
            getAllRegion () {
                Utils.ajax(Api.order.allRegion, {},(res) => {
                    const me = this
                    if((res.error == '00000000')&&(res.status == '1')) {
                        var data = res.data;
                        if(data && data instanceof Array) {
                            data.forEach((item) => {
                                me.region_process.push({
                                    label: item.name,
                                    value: item.id
                                })
                            })
                            me.region_process.push({
                                label: '不限地域',
                                value: '0'
                            })
                            me.region_processStatus = '0'
                        }
                        Utils.setSession('region_process', me.region_process)
                    } else {
                        this.$message.error('请求所有接口区域接口失败!')
                    }
                })
            }
        }
    }
</script>

<style lang="sass" scoped>
    @import "./index";
</style>
