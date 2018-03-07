<template>
    <div>
        <el-breadcrumb separator="/">
            <el-breadcrumb-item>控制面板</el-breadcrumb-item>
            <el-breadcrumb-item :to="{ path: '/index' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item :to="{ path: '/table' }">data</el-breadcrumb-item>
            <el-breadcrumb-item>催收</el-breadcrumb-item>
        </el-breadcrumb>

        <div class="element__body">
            <div class="row1">
                <el-form :inline="true" :model="form1">
                    <el-form-item>
                        <el-select v-model="form.status" placeholder="账号类型">
                            <el-option v-for="item in accountTypeOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-input v-model="input" placeholder="账单金额"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <span class="split">-</span>
                    </el-form-item>
                    <el-form-item>
                        <el-input v-model="input" placeholder="账单金额"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-input v-model="input" placeholder="逾期天数"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <span class="split">-</span>
                    </el-form-item>
                    <el-form-item>
                        <el-input v-model="input" placeholder="逾期天数"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-date-picker :editable="false" :clearable="false" v-model="form.fromDate" type="date"
                                        placeholder="选择日期"
                                        :picker-options="pickerOptions"></el-date-picker>
                    </el-form-item>
                    <el-form-item>
                        <span class="split">-</span>
                    </el-form-item>
                    <el-form-item>
                        <el-date-picker :editable="false" :clearable="false" v-model="form.endDate" type="date"
                                        placeholder="选择日期"
                                        :picker-options="pickerOptions"></el-date-picker>
                    </el-form-item>
                </el-form>
            </div>

            <div class="row2">
                <el-form :inline="true" :model="form">
                    <el-form-item>
                        <el-select v-model="form.status" placeholder="是否拨打">
                            <el-option v-for="item in isDailOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-select v-model="form.status" placeholder="信函发送">
                            <el-option v-for="item in isDailOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-select v-model="form.status" placeholder="分配查询">
                            <el-option v-for="item in isDailOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-select v-model="form.status" placeholder="是否承诺">
                            <el-option v-for="item in isDailOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
                        </el-select>
                    </el-form-item>
                </el-form>
            </div>

            <div class="row3">
                <el-form :inline="true" :model="form">
                    <el-form-item>
                        <el-select v-model="form.status" placeholder="是否标记">
                            <el-option v-for="item in isDailOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-select v-model="form.status" placeholder="联络结果">
                            <el-option v-for="item in isDailOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-button @click>搜索</el-button>
                    </el-form-item>
                    <el-form-item>
                        <el-button @click>重置</el-button>
                    </el-form-item>
                    <el-form-item>
                        <el-button @click = taskAllot()>任务分配</el-button>
                    </el-form-item>
                </el-form>
            </div>

            <div class="table">
                <el-table :data="tableData" border style="width: 100%" max-height="250">
                    <el-table-column fixed label="全选" width="70">
                        <template slot-scope="scope">
                            <el-checkbox></el-checkbox>
                        </template>
                    </el-table-column>
                    <el-table-column prop="easiness" label="容易度" width="90"></el-table-column>
                    <el-table-column prop="cutomerName" label="姓名" width="80"></el-table-column>
                    <el-table-column prop="cardId" label="身份证号" width="120"></el-table-column>
                    <el-table-column prop="loanNumber" label="贷款号" width="140"></el-table-column>
                    <el-table-column prop="billAmount" label="账单金额" width="120"></el-table-column>
                    <el-table-column prop="borrowCycle" label="借款周期" width="120"></el-table-column>
                    <el-table-column prop="billAmount" label="挂账金额" width="120"></el-table-column>
                    <el-table-column prop="" label="催收员" width="120"></el-table-column>
                    <el-table-column prop="taskTime" label="任务创建时间" width="120"></el-table-column>
                    <el-table-column prop="" label="逾期天数" width="120"></el-table-column>
                    <el-table-column prop="commitmentTime" label="承诺时间" width="120"></el-table-column>
                    <el-table-column prop="lastTime" label="最后联系时间" width="120"></el-table-column>
                    <el-table-column prop="contactResult" label="联络结果" width="120"></el-table-column>
                    <el-table-column prop="label" label="标签" width="120"></el-table-column>
                    <el-table-column prop="orderStatus" label="信函状态" width="120"></el-table-column>
                    <el-table-column label="管制" width="80">
                        <template slot-scope="scope">
                            <el-button @click.native.prevent="underCode(scope.row)" type="text" size="small">下码</el-button>
                        </template>
                    </el-table-column>
                    <el-table-column prop="zip" label="管制码结果" width="90"></el-table-column>
                    <el-table-column prop="zip" label="通讯录调取" width="90"></el-table-column>
                    <el-table-column label="冻结" width="80">
                        <template slot-scope="scope">
                            <el-button @click.native.prevent="nonFrozen(scope.row)" type="text" size="small">未冻结</el-button>
                        </template>
                    </el-table-column>
                    <el-table-column label="放弃" width="80">
                        <template slot-scope="scope">
                            <el-button @click.native.prevent="nonQuit(scope.row)" type="text" size="small">未放弃</el-button>
                        </template>
                    </el-table-column>
                    <el-table-column prop="remarks" label="备注" width="90"></el-table-column>
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

        <div>
            <el-button type="primary" @click="test1">下码</el-button>
            <el-button type="primary" @click="test2">未冻结</el-button>
            <el-button type="primary" @click="test3">未放弃</el-button>
            <el-button type="primary" @click="test4">个人资料</el-button>
            <el-button type="primary" @click="test5">任务分配</el-button>
        </div>

        <el-dialog title="管制下码" :visible.sync="dialogVisibleForUnderCode" :before-close="closeDialog">
            <div class="row4">
                <el-form>
                    <el-form-item label="管制码:">
                        <el-select v-model="form.status" placeholder="疑似欺诈案件">
                            <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value"></el-option>
                        </el-select>
                        <el-select v-model="form.status" placeholder="非本人操作">
                            <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-input type="textarea" :rows="4" placeholder="请输入内容" v-model="textarea"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button>取消</el-button>
                        <el-button>提交</el-button>
                    </el-form-item>
                </el-form>
                <el-table :data="tableData" border style="width: 100%" max-height="150">
                    <el-table-column prop="zip" label="一级管制码" width="120"></el-table-column>
                    <el-table-column prop="zip" label="二级管制码" width="120"></el-table-column>
                    <el-table-column prop="zip" label="下码操作人" width="120"></el-table-column>
                    <el-table-column prop="zip" label="下码时间" width="120"></el-table-column>
                    <el-table-column prop="zip" label="下码备注" width="120"></el-table-column>
                    <el-table-column prop="zip" label="拨码操作人" width="120"></el-table-column>
                    <el-table-column prop="zip" label="拨码时间" width="120"></el-table-column>
                    <el-table-column prop="zip" label="拨码备注" width="120"></el-table-column>
                </el-table>
            </div>
        </el-dialog>


        <el-dialog title="账户冻结" :visible.sync="dialogVisibleForFreeze" :before-close="closeDialog">
            <div class="row4">
                <el-form>
                    <el-form-item label="冻结原因:">
                        <el-select v-model="form.status" placeholder="请选择">
                            <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-input type="textarea" :rows="4" placeholder="请输入内容" v-model="textarea"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button>取消</el-button>
                        <el-button>提交</el-button>
                    </el-form-item>
                </el-form>
                <el-table :data="tableData" border style="width: 100%" max-height="150">
                    <el-table-column prop="zip" label="借款单号" width="120"></el-table-column>
                    <el-table-column prop="zip" label="冻结原因" width="120"></el-table-column>
                    <el-table-column prop="zip" label="冻结备注" width="120"></el-table-column>
                    <el-table-column prop="zip" label="冻结人" width="120"></el-table-column>
                    <el-table-column prop="zip" label="冻结时间" width="120"></el-table-column>
                    <el-table-column prop="zip" label="解冻人" width="120"></el-table-column>
                    <el-table-column prop="zip" label="解冻备注" width="120"></el-table-column>
                    <el-table-column prop="zip" label="解冻时间" width="120"></el-table-column>
                </el-table>
            </div>
        </el-dialog>

        <el-dialog title="放弃备注" :visible.sync="dialogVisibleForQuitRemark" :before-close="closeDialog">
            <div class="row4">
                <el-form>
                    <el-form-item>
                        <span>备注</span>
                        <el-input type="textarea" :rows="4" placeholder="请输入内容" v-model="textarea"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button>取消</el-button>
                        <el-button>提交</el-button>
                    </el-form-item>
                </el-form>
                <el-table :data="tableData" border style="width: 100%" max-height="150">
                    <el-table-column prop="zip" label="借款单号" width="120"></el-table-column>
                    <el-table-column prop="zip" label="放弃操作人" width="120"></el-table-column>
                    <el-table-column prop="zip" label="放弃备注" width="120"></el-table-column>
                    <el-table-column prop="zip" label="放弃时间" width="120"></el-table-column>
                    <el-table-column prop="zip" label="取消放弃人" width="120"></el-table-column>
                    <el-table-column prop="zip" label="取消放弃备注" width="120"></el-table-column>
                    <el-table-column prop="zip" label="取消放弃时间" width="120"></el-table-column>
                </el-table>
            </div>
        </el-dialog>

        <el-dialog title="任务分配" :visible.sync="dialogVisibleForTaskAllot" :before-close="closeDialog">
            <div class="row4">
                <el-form>
                    <el-form-item label="所属人员">
                        <el-select v-model="form.status" placeholder="请选择">
                            <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <span>分配数量</span>
                        <el-input v-model="input"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button>取消</el-button>
                        <el-button>提交</el-button>
                    </el-form-item>
                </el-form>
            </div>
        </el-dialog>

    </div>
</template>

<script>
//    import ElFormItem from "../../../../../node_modules/element-ui/packages/form/src/form-item";
    export default {
        components: {ElFormItem},
        name: 'bill',
        data () {
            return {
                tableData: [],
                dialogVisible: false,
                dialogVisibleForUnderCode: false,
                dialogVisibleForFreeze: false,
                dialogVisibleForQuitRemark: false,
                dialogVisibleForTaskAllot: false,
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
                accountTypeOptions: [{
                    value: '0',
                    label: '类型一'
                },{
                    value: '1',
                    label: '类型二'
                }],
                isDailOptions: [{
                    value: '0',
                    label: '是'
                },{
                    value: '1',
                    label: '否'
                }],
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
                Utils.go('')
                return
            }
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
                window.open(window.location.host+link);
            },
            goDetail (item) {
                const me = this
                Utils.setSession('isChecked', '1')
                Utils.setSession('detailStatus', '1')
                me.reGoDetail(item)
                window.location.href = res__ + '/#/' + 'order'
            },
            goDetailCheck (item) {
                const me = this
                Utils.setSession('detailStatus', '0')
                Utils.setSession('isChecked', '0')
                me.reGoDetail(item)
                window.location.href = res__ + '/#/' + 'order'
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
                var me = this;
                var pager = this.$data.pager;
                Utils.ajax(Api.collection.list, {
                    pageNo: pager.pageNo,
                    pageSize: pager.pageSize
                }, (res) => {
                    if (+res.status === 1) {
                        var data = res.data;
                        me.tableData = data['list'];
                        pager.totalSize = data['count'];
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
                done()
            },
            // 打开上传文件弹框
            upload (row) {
                if (row.orderId) {
                    this.dialogVisible = true
                    this.cacheOrderId = row.orderId
                    this.cacheRow = row
                }
                console.log(row)
            },
            // 删除文件的回调
            handleRemove (file, fileList) {
                console.log(file)
                console.log(fileList)
            },
            // 提交上传
            submitUpload () {
                this.disableUpload = true
                this.$refs.upload.submit()
                this.dialogVisible = false
                this.loadingTable = true
            },
            // 上传前进行文件类型判断
            beforeUpload (file) {
                const isEXCEL = file.type === 'application/vnd.ms-excel' || file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
                if (!isEXCEL) {
                    this.$message.error('上传的文件只能是 EXCEL 格式!')
                }
                return isEXCEL
            },
            // 上传中
            uploadProgress (event, file, fileList) {
                this.disableUpload = true
            },
            // 上传失败
            uploadError (err, file, fileList) {
                this.disableUpload = false
                this.loadingTable = false
                this.$message.error('文件上传失败!')
                console.log(err)
            },
            // 上传成功
            uploadSuccess (response, file, fileList) {
                this.disableUpload = false
                this.$http.post(Api.handleOrder, {
                    account: this.cacheOrderId,
                    incr_index: response.data
                }).then(res => {
                    if (+res.data.status === 1) {
                        this.getStatus(response.data, window.UNIQUE)
                    } else {
                        this.$message.error('文件处理错误!')
                    }
                })
            },
            getStatus (index, unique) {
                this.$http.post(Api.queryStatus, {
                    index: index
                }).then(res => {
                    console.log(res.data)
                    if (+res.data.data === 0) {
                        if (unique === window.UNIQUE) {
                            setTimeout(() => {
                                this.getStatus(index, unique)
                            }, 2000)
                        }
                    } else if (+res.data.data === 1) {
                        // 获取处理条数
                        this.$http.post(Api.completeNum, {
                            index: index
                        }).then(response => {
                            console.log(response.data)
                            console.log(response.data.data)
                            if (this.cacheRow && this.cacheRow instanceof Object) {
                                this.cacheRow.completeNum = response.data.data
                            }
                            this.loadingTable = false
                            this.$message.success('文件上传成功!');
                            // 关闭弹框
                            this.dialogVisible = false
                            this.$refs.upload.clearFiles()
                            console.log(response.data)
                        })
                    }
                })
            },
            underCode (item) {
                var me = this;
                me.dialogVisibleForUnderCode = true;
            },
            nonFrozen () {
                var me = this;
                me.dialogVisibleForFreeze = true;
            },
            nonQuit () {
                var me = this;
                me.dialogVisibleForQuitRemark = true;
            },
            test4 () {
                window.location.href = res__ + '/#/' + 'order'
            },
            taskAllot () {
                var me = this;
                me.dialogVisibleForTaskAllot = true;
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
