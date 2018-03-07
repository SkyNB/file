<template>
    <div>
        <el-breadcrumb separator="/">
            <el-breadcrumb-item>控制面板</el-breadcrumb-item>
            <el-breadcrumb-item :to="{ path: '/index' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item :to="{ path: '/table' }">数据</el-breadcrumb-item>
            <el-breadcrumb-item>人工信审</el-breadcrumb-item>
        </el-breadcrumb>

        <div class="element__body">
            <div class="row">
                <el-form :inline="true" :model="form">
                    <el-form-item label="对账日期:" class>
                        <el-date-picker :editable="false" :clearable="false" v-model="form.fromDate" type="date"
                                        placeholder="选择日期"
                                        :picker-options="pickerOptions"></el-date-picker>
                    </el-form-item>
                    <span class="split">-</span>
                    <el-date-picker :editable="false" :clearable="false" v-model="form.endDate" type="date"
                                    placeholder="选择日期"
                                    :picker-options="pickerOptions">
                    </el-date-picker>
                    <el-form-item label="确认状态:">
                        <el-select v-model="form.status" placeholder="请选择">
                            <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value"></el-option>
                        </el-select>
                    </el-form-item>

                    <el-form-item>
                        <el-button @click="query" class="query_btn" type="primary">查询</el-button>
                    </el-form-item>

                    <el-form-item>
                        <el-button @click="allOrderDetail" class="allOrderDetail_btn" type="primary">所有订单详情</el-button>
                    </el-form-item>
                </el-form>
            </div>


            <div class="table">
                <el-table :data="tableData" border>
                    <el-table-column prop="date" label="对账日期" width="120"></el-table-column>
                    <el-table-column prop="status" label="确认状态" width="120"></el-table-column>
                    <el-table-column prop="money" label="放款总额（元）" width="180"></el-table-column>
                    <el-table-column prop="count" label="订单总数" width="150"></el-table-column>
                    <!-- 加一列来源 -->
                    <el-table-column prop="orderSource" label="来源" width="150"></el-table-column>
                    <el-table-column label="操作">
                        <template slot-scope="scope">
                            <el-button v-if="scope.row.status != '已确认'" @click="goDetailCheck(scope.row)" type="text">
                                人工检查
                            </el-button>
                            <el-button v-if="scope.row.status != '已确认'" @click="confirmBill(scope.row.orderId)"
                                       type="text">完成检查
                            </el-button>
                            <el-button v-if="scope.row.status == '已确认'" @click="goDetail(scope.row)" type="text">明细
                            </el-button>
                            <el-button v-if="scope.row.status == '已确认'" @click="exportExcel(scope.row.customerLink)"
                                       type="text">导出客户Excel
                            </el-button>
                            <el-button v-if="scope.row.status == '已确认'" @click="exportExcel(scope.row.orderLink)"
                                       type="text">导出业务Excel
                            </el-button>
                            <el-button v-if="scope.row.status == '已确认'" @click="upload(scope.row)" type="text">上传回执
                            </el-button>
                            <el-button @click="downloadpackage(scope.row.assetPackageLink)" type="text">下载资产包
                            </el-button>
                            <el-button v-if="((scope.row.status == '已确认')&&(role == '柜员')&&(scope.row.loanStatus == '0'))" @click="makeLoans(scope.row)" type="text">放款
                            </el-button>
                            <span v-if="((scope.row.status == '已确认')&&(role == '柜员')&&(scope.row.loanStatus == '1'))" type="text">已放款</span>
                            <span v-if="scope.row.completeNum > -1">已处理{{ scope.row.completeNum }}条</span>
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


        <el-dialog title="上传回执" :visible.sync="dialogVisible" size="tiny" :before-close="closeDialog">
            <el-upload class="upload_file" ref="upload" drag
                       :action="importUrl"
                       :auto-upload="false"
                       :on-remove="handleRemove"
                       :on-progress="uploadProgress"
                       :on-error="uploadError"
                       :on-success="uploadSuccess"
                       :before-upload="beforeUpload">
                <i class="el-icon-upload"></i>
                <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                <!--<div class="el-upload__tip" slot="tip">只能上传jpg/png文件，且不超过500kb</div>-->
            </el-upload>
            <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="submitUpload" :disabled="disableUpload">上 传</el-button>
            </span>
        </el-dialog>

    </div>
</template>

<script>
    export default {
        name: 'bill',
        data () {
            return {
                dialogVisible: false,
                // 禁用上传按钮
                disableUpload: false,
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
                    fromDate: '',
                    endDate: '',
                    status: ''
                },

                pickerOptions: {
                    disabledDate (time) {
                        return time.getTime() > Date.now()
                    }
                },
                options: [{
                    value: '',
                    label: '全部'
                }, {
                    value: 0,
                    label: '未确认'
                }, {
                    value: 1,
                    label: '已确认'
                }],

                tableData: [],
                confirmAble: false,
                loadingInstance: null,
                cacheOrderId: -1,
                // 缓存要处理的数据
                cacheRow: null,
                importUrl: Api.importOrder,
                // 显示loading
                loadingTable: false
            }
        },
        created () {
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


            const me = this;
            // 判断用户权限
            me.confirmAble = +Utils.getSession('loginType') === 2
            // 获取查询状态
            // 保存查询状态
            me.form.fromDate = Utils.getSession('billQuery-fromDate')
            me.form.endDate = Utils.getSession('billQuery-endDate')
            me.role = Utils.getSession('role')
            if (me.form.fromDate) {
                me.form.status = Utils.getSession('billQuery-status')
                Utils.removeSession('billQuery-fromDate')
                Utils.removeSession('billQuery-endDate')
                Utils.removeSession('billQuery-status')
            } else {
                const today = new Date()
                let endDate = Utils.formatDate(today, 'YYYY/MM/DD')
                let fromDate = today.getTime() - 1000 * 60 * 60 * 24 * 7
                fromDate = Utils.formatDate(new Date(fromDate), 'YYYY/MM/DD')
                me.form.fromDate = fromDate
                me.form.endDate = endDate
            }
            me.getList(me.form.fromDate, me.form.endDate, me.form.status);
            // 创建唯一标识符
            window.UNIQUE = Math.random().toString(36).substr(2).toUpperCase()
        },
        methods: {
            //分页
            sizeChange(val){
                this.$data.pager.pageSize = val;
                this.query();
            },
            currentChange(val){
                this.$data.pager.pageNo = val;
                this.query();
            },
            exportExcel (link) {
                window.open(link);
            },
            downloadpackage (link) {
                window.open(link);
            },
            goDetail (item) {
                const me = this
                Utils.setSession('isChecked', '1')
                Utils.setSession('detailStatus', '1')
                me.reGoDetail(item)
                window.location.href = ress__+ '/#/' + 'order'
            },
            goDetailCheck (item) {
                const me = this
                Utils.setSession('detailStatus', '0')
                Utils.setSession('isChecked', '0')
                me.reGoDetail(item)
                window.location.href = ress__+ '/#/' + 'order'
            },
            reGoDetail (item) {
                const me = this
                Utils.setSession('orderDate', item.date)
                Utils.setSession('orderStatus', item.status)
                // 保存查询状态
                me.form.fromDate = me.form.fromDate instanceof Date ? Utils.formatDate(me.form.fromDate, 'YYYY/MM/DD') : me.form.fromDate
                me.form.endDate = me.form.endDate instanceof Date ? Utils.formatDate(me.form.endDate, 'YYYY/MM/DD') : me.form.endDate
                Utils.setSession('billQuery-fromDate', me.form.fromDate)
                Utils.setSession('billQuery-endDate', me.form.endDate)
                Utils.setSession('billQuery-status', me.form.status)
                Utils.setSession('billQuery-account', item.orderId)
            },
            query () {
                var me = this;
                let fromDate = me.form.fromDate
                let endDate = me.form.endDate
                const status = me.form.status
                const format = 'YYYY/MM/DD'
                if (fromDate instanceof Date) {
                    fromDate = Utils.formatDate(fromDate, format)
                }
                if (endDate instanceof Date) {
                    endDate = Utils.formatDate(endDate, format)
                }
                if (new Date(fromDate) > new Date(endDate)) {
                    this.$message({
                        showClose: true,
                        message: '开始时间应小于等于结束时间',
                        type: 'error'
                    })
                    return
                }
                this.getList(fromDate, endDate, status)
            },
            allOrderDetail () {
                window.location.href = ress__+ '/#/' + 'allOrdersDetail'
            },
            confirmBill (account) {
                const me = this
                const sessionCode = Utils.getSession('token')
                const customerCode = Utils.getSession('customerCode')
                Utils.ajax(Api.bill.confirmBill, {
                    sessionCode: sessionCode,
                    account: account
                },(res) => {
                    console.log(res)
                    if (+res.status === 1) {
                        me.$message({
                            showClose: true,
                            message: '确认成功',
                            type: 'success'
                        })
                        me.query()
                    } else {
                        me.$message({
                            showClose: true,
                            message: res.msg || '确认失败',
                            type: 'error'
                        })
                    }
                });
            },
            getList (fromDate, endDate, status) {
                var me = this
                var sessionCode = Utils.getSession('token')
                var customerCode = Utils.getSession('customerCode')
                var item = {
                    customerCode: customerCode,
                    dateFrom: fromDate,
                    dateTo: endDate,
                    sessionCode: sessionCode,
                    confirmStatus: status
                };
                Utils.ajax(Api.bill.list, item, (res) => {
                    if (+res.status === 1) {
                        me.tableData = []
                        me.loadingTable = false
                        let data = res.data
                        if (data && data instanceof Array) {
                            data.forEach((item) => {
                                me.tableData.push({
                                    date: item.date,
                                    orderId: item.summaryOrderId,
                                    completeNum: -1,
                                    permissionSet: item.permissionSet,
                                    status: item.status,
                                    money: item.amount,
                                    count: item.quantity,
                                    orderSource: item.orderSource,
                                    confirm: item.status === '已确认',
                                    loanStatus: item.loanStatus,
                                    customerLink: res__+`/order/exportCustomers?billNumber=${item.summaryOrderId}&date=${item.date}&sessionCode=${sessionCode}`,
                                    orderLink: res__+`/order/exportOrderItems?billNumber=${item.summaryOrderId}&date=${item.date}&sessionCode=${sessionCode}`,
                                    assetPackageLink: res__+`/order/downloadAssetPackage?billNumber=${item.summaryOrderId}&date=${item.date}&sessionCode=${sessionCode}`
                                })
                            })
                        }
                    } else {
                        me.$message({
                            showClose: true,
                            message: res.msg || '数据请求失败',
                            type: 'error'
                        })
                    }
                });
            },
            // 关闭对话框
            closeDialog (done) {
                done();
            },
            // 打开上传文件弹框
            upload (row) {
                if (row.orderId) {
                    this.dialogVisible = true;
                    this.cacheOrderId = row.orderId;
                    this.cacheRow = row;
                }
                console.log(row)
            },
            // 删除文件的回调
            handleRemove (file, fileList) {
                console.log(file);
                console.log(fileList);
            },
            // 提交上传
            submitUpload () {
                this.disableUpload = true;
                this.$refs.upload.submit();
                this.dialogVisible = false;
                this.loadingTable = true;
            },
            // 上传前进行文件类型判断
            beforeUpload (file) {
                const isEXCEL = file.type === 'application/vnd.ms-excel' || file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet';
                if (!isEXCEL) {
                    this.$message.error('上传的文件只能是 EXCEL 格式!');
                }
                return isEXCEL;
            },
            // 上传中
            uploadProgress (event, file, fileList) {
                this.disableUpload = true;
            },
            // 上传失败
            uploadError (err, file, fileList) {
                this.disableUpload = false;
                this.loadingTable = false;
                this.$message.error('文件上传失败!');
                console.log(err);
            },
            // 上传成功
            uploadSuccess (response, file, fileList) {
                this.disableUpload = false;
                this.$http.post(Api.handleOrder, {
                    account: this.cacheOrderId,
                    incr_index: response.data
                }).then(res => {
                    if (+res.data.status === 1) {
                        this.getStatus(response.data, window.UNIQUE);
                    } else {
                        this.$message.error('文件处理错误!');
                    }
                })
            },
            getStatus (index, unique) {
                this.$http.post(Api.queryStatus, {
                    index: index
                }).then(res => {
                    console.log(res.data);
                    if (+res.data.data === 0) {
                        if (unique === window.UNIQUE) {
                            setTimeout(() => {
                                this.getStatus(index, unique);
                            }, 2000)
                        }
                    } else if (+res.data.data === 1) {
                        // 获取处理条数
                        this.$http.post(Api.completeNum, {
                            index: index
                        }).then(response => {
                            console.log(response.data);
                            console.log(response.data.data);
                            if (this.cacheRow && this.cacheRow instanceof Object) {
                                this.cacheRow.completeNum = response.data.data;
                            }
                            this.loadingTable = false;
                            this.$message.success('文件上传成功!');
                            // 关闭弹框
                            this.dialogVisible = false;
                            this.$refs.upload.clearFiles();
                            console.log(response.data);
                        })
                    }
                })
            },
            makeLoans (item) {



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
