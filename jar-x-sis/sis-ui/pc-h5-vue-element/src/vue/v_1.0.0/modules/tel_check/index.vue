<template>
    <div>
        <el-breadcrumb separator="/">
            <el-breadcrumb-item>控制面板</el-breadcrumb-item>
            <el-breadcrumb-item :to="{ path: '/index' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>电核</el-breadcrumb-item>
        </el-breadcrumb>

        <div class="element__body">

            <h1>风控评分</h1>
            <el-row>
                <el-col :span="12"><div class="grid-content bg-purple">
                    <span>风控评分</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span>{{detail.extScore==''?'无':detail.extScore}}</span>
                </div></el-col>
            </el-row>
            <hr />

            <h1>身份证信息</h1>
            <el-row>
                <el-col :span="12"><div class="grid-content bg-purple">
                    <span>姓名</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span>{{detail.renter==''?'无':detail.renter}}</span>
                </div></el-col>
                <el-col :span="12"><div class="grid-content bg-purple-light">
                    <span>身份证号</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span>{{detail.idCardNum==''?'无':detail.idCardNum}}</span>
                </div></el-col>
            </el-row>
            <hr />

            <h1>基本信息</h1>
            <el-row>
                <el-col :span="12"><div class="grid-content bg-purple">
                    <span>姓名</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span>{{detail.renter==''?'无':detail.renter}}</span>
                </div></el-col>
                <el-col :span="12"><div class="grid-content bg-purple-light">
                    <span>性别</span>&nbsp;&nbsp;&nbsp;&nbsp;<span>{{(detail.gender=='1')?'男':'女'}}</span>
                </div></el-col>
            </el-row>
            <el-row>
                <el-col :span="12"><div class="grid-content bg-purple">
                    <span>出生年月日</span>&nbsp;&nbsp;&nbsp;&nbsp;<span>{{detail.birthday==''?'无':detail.birthday}}</span>
                </div></el-col>
                <el-col :span="12"><div class="grid-content bg-purple-light">
                    <span>省/直辖市</span>&nbsp;&nbsp;&nbsp;&nbsp;<span>{{detail.region==''?'无':detail.region}}</span>
                </div></el-col>
            </el-row>
            <el-row>
                <el-col :span="12"><div class="grid-content bg-purple">
                    <span>县/区</span>&nbsp;&nbsp;&nbsp;&nbsp;<span>{{detail.country==''?'无':detail.country}}</span>
                </div></el-col>
                <el-col :span="12"><div class="grid-content bg-purple-light">
                    <span>是否已婚</span>&nbsp;&nbsp;&nbsp;&nbsp;<span>{{(detail.married=='1')?'已婚':'未婚'}}</span>
                </div></el-col>
            </el-row>
            <el-row>
                <el-col :span="12"><div class="grid-content bg-purple">
                    <span>是否本地</span>&nbsp;&nbsp;&nbsp;&nbsp;<span>{{detail.isLocal=='1'?'本地':'非本地'}}</span>
                </div></el-col>
            </el-row>
            <hr />


            <h1>配偶信息</h1>
            <el-row>
                <el-col :span="12"><div class="grid-content bg-purple">
                    <span>配偶姓名</span>&nbsp;&nbsp;&nbsp;&nbsp;<span>{{detail.spouse==''?'无':detail.spouse}}</span>
                </div></el-col>
                <el-col :span="12"><div class="grid-content bg-purple-light">
                    <span>配偶身份证号</span>&nbsp;&nbsp;&nbsp;&nbsp;<span>{{detail.spouseIdCardNum==''?'无':detail.spouseIdCardNum}}</span>
                </div></el-col>
            </el-row>
            <hr />


            <h1>联系方式</h1>
            <el-row>
                <el-col :span="12"><div class="grid-content bg-purple">
                    <span>户籍地址</span>&nbsp;&nbsp;&nbsp;&nbsp;<span>{{detail.census==''?'无':detail.census}}</span>
                </div></el-col>
                <el-col :span="12"><div class="grid-content bg-purple-light">
                    <span>联系电话</span>&nbsp;&nbsp;&nbsp;&nbsp;<span>{{detail.tel==''?'无':detail.tel}}</span>
                </div></el-col>
            </el-row>
            <el-row>
                <el-col :span="12"><div class="grid-content bg-purple">
                    <span>家庭地址</span>&nbsp;&nbsp;&nbsp;&nbsp;<span>{{detail.homeAddress==''?'无':detail.homeAddress}}</span>
                </div></el-col>
                <el-col :span="12"><div class="grid-content bg-purple-light">
                    <span>家庭联系电话</span>&nbsp;&nbsp;&nbsp;&nbsp;<span>{{detail.homeTel==''?'无':detail.homeTel}}</span>
                </div></el-col>
            </el-row>
            <el-row>
                <el-col :span="12"><div class="grid-content bg-purple">
                    <span>邮政编码</span>&nbsp;&nbsp;&nbsp;&nbsp;<span>{{detail.zipcode==''?'无':detail.zipcode}}</span>
                </div></el-col>
                <el-col :span="12"><div class="grid-content bg-purple-light">
                    <span>住宅邮政编码</span>&nbsp;&nbsp;&nbsp;&nbsp;<span>{{detail.homeZipcode==''?'无':detail.homeZipcode}}</span>
                </div></el-col>
            </el-row>
            <el-row>
                <el-col :span="12"><div class="grid-content bg-purple">
                    <span>手机号</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span>{{detail.cellphone==''?'无':detail.cellphone}}</span>
                </div></el-col>
            </el-row>
            <hr />

            <h1>工作及资历信息</h1>
            <el-row>
                <el-col :span="12"><div class="grid-content bg-purple">
                    <span>工作单位</span>&nbsp;&nbsp;&nbsp;&nbsp;<span>{{detail.company==''?'无':detail.company}}</span>
                </div></el-col>
                <el-col :span="12"><div class="grid-content bg-purple-light">
                    <span>行业</span>&nbsp;&nbsp;&nbsp;&nbsp;<span>{{detail.industry==''?'无':detail.industry}}</span>
                </div></el-col>
            </el-row>
            <el-row>
                <el-col :span="12"><div class="grid-content bg-purple">
                    <span>公司邮编</span>&nbsp;&nbsp;&nbsp;&nbsp;<span>{{detail.officeZipcode==''?'无':detail.officeZipcode}}</span>
                </div></el-col>
                <el-col :span="12"><div class="grid-content bg-purple-light">
                    <span>单位地址</span>&nbsp;&nbsp;&nbsp;&nbsp;<span>{{detail.officeAddress==''?'无':detail.officeAddress}}</span>
                </div></el-col>
            </el-row>
            <el-row>
                <el-col :span="12"><div class="grid-content bg-purple">
                    <span>单位电话</span>&nbsp;&nbsp;&nbsp;&nbsp;<span>{{detail.officeTel==''?'无':detail.officeTel}}</span>
                </div></el-col>
                <el-col :span="12"><div class="grid-content bg-purple-light">
                    <span>学历</span>&nbsp;&nbsp;&nbsp;&nbsp;<span>{{detail.educational==''?'无':detail.educational}}</span>
                </div></el-col>
            </el-row>
            <hr />


            <h1>其他信息</h1>
            <el-row>
                <el-col :span="12"><div class="grid-content bg-purple">
                    <span>订单号</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span>{{detail.borrowNid==''?'无':detail.borrowNid}}</span>
                </div></el-col>
                <el-col :span="12"><div class="grid-content bg-purple-light">
                    <span>期数</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span>{{detail.period==''?'无':detail.period}}</span>
                </div></el-col>
            </el-row>
            <el-row>
                <el-col :span="12"><div class="grid-content bg-purple">
                    <span>借款金额</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span>{{detail.amount==''?'无':detail.amount}}</span>
                </div></el-col>
                <el-col :span="12"><div class="grid-content bg-purple-light">
                    <span>月利率</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span>{{detail.rate==''?'无':detail.rate}}</span>
                </div></el-col>
            </el-row>
            <el-row>
                <el-col :span="12"><div class="grid-content bg-purple">
                    <span>协议地址</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span><p>无</p></span>
                </div></el-col>
                <el-col :span="12"><div class="grid-content bg-purple-light">
                    <span>借款银行卡号</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span>{{detail.loanBankCard==''?'无':detail.loanBankCard}}</span>
                </div></el-col>
            </el-row>
            <el-row>
                <el-col :span="12"><div class="grid-content bg-purple">
                    <span>还款银行卡号</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span>{{detail.repayBankCard==''?'无':detail.repayBankCard}}</span>
                </div></el-col>
                <el-col :span="12"><div class="grid-content bg-purple-light">
                    <span>贷款用途</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span>{{detail.loanUsage==''?'无':detail.loanUsage}}</span>
                </div></el-col>
            </el-row>
            <hr />


            <h1>身份证正反面照片</h1>
            <BR/>
            <img :src="detail.photoUrl0" height="180px" width="300px" id="cardPositive" @click="cardPositive()"/>
            <img :src="detail.photoUrl1" height="180px" width="300px" id="cardBack" @click="cardBack()"/>
            <hr />


            <h1>备注</h1>
            <BR/>
            <span v-show="mark_is_show" style=" border-radius: 4px; border:1px solid #000">{{detail.description}}</span>&nbsp;&nbsp;&nbsp;
            <el-button v-show="edit_is_show" type="info" @click="editMark()">编辑</el-button>
            <el-button v-show="add_mark_is_show" type="info" @click="editMark()" align="center">添加备注</el-button>
            <hr />
            <BR/>

            <el-button v-if="detail.loanDay == null" type="danger" @click="refuseOrder()" round>拒绝</el-button>
            <el-button v-if="detail.loanDay == null" type="success" @click="updateOrderStatus('1')" round>通过</el-button>
            <el-button type="success" @click="returnList()" round>返回</el-button>

            <el-dialog title="检查确认" :visible.sync="dialogShowCheck">
                <form class="form">
                    <div class="row" float:left>
                        <span class="label_status">状态</span>
                        <el-select v-model="check_status" clearable>
                            <el-option v-for="item in check_status_process" :key="item.value" :label="item.label" :value="item.value"></el-option>
                        </el-select>
                        <el-button float:right @click="confirmRefuseStatus()">确认</el-button>
                    </div>
                </form>
            </el-dialog>

            <el-dialog
                    title="备注"
                    :visible.sync="mark_dialogShowCheck"
                    width="30%"
                    :before-close="handleClose">
                <textarea v-model="show_description" rows="3" cols="100" placeholder="请输入内容" autosize="true"
                          maxlength="200" onchange="this.value=this.value.substring(0, 50)"
                          onkeydown="this.value=this.value.substring(0, 50)" onkeyup="this.value=this.value.substring(0, 50)">{{detail.description}}</textarea>
                <span slot="footer" class="dialog-footer">
                <el-button @click="mark_dialogShowCheck = false">取 消</el-button>
                <el-button type="primary" @click="confirmMark()">确 定</el-button>
                </span>
            </el-dialog>
        </div>

    </div>
</template>

<script>
    import ElButton from "../../../../../node_modules/element-ui/packages/button/src/button";
    window.positiveStatus = true;
    window.backStatus = true;
    export default {
        components: {ElButton},
        name: 'tel_check',
        data () {
            return {
                show_description: '',
                add_mark_is_show: false,
                edit_is_show: true,
                mark_is_show: true,
                agreement_url_string: '',//协议地址
                orderCheckStatus: '',//订单审核状态
                form: {
                    fromDate: '',
                    endDate: '',
                    status: ''
                },
                order_id: '',
                check_status_process: [],
                detail: {},
                dialogShowCheck: false,
                mark_dialogShowCheck: false,
                check_status:''
            }
        },
        created () {
            const me = this;
            me.order_id = Utils.getSession("current_order_id");
            //获取所有拒绝原因
            me.getAllOperationCase();
            //获取订单详情
            me.getOrderDetail(me.order_id);
            //获取契约
            me.getContract(me.order_id);
        },
        methods: {
            refuseOrder() {
                var me = this;
                me.dialogShowCheck = true;
            },
            //编辑备注
            editMark() {
                var me = this;
                me.mark_dialogShowCheck = true;
            },

            //  身份证正反面放大
            cardPositive(){
                if(positiveStatus == true){
                    document.getElementById("cardPositive").setAttribute("height","300");
                    document.getElementById("cardPositive").setAttribute("width","500");
                    positiveStatus = false;
                }else {
                    document.getElementById("cardPositive").setAttribute("height","180");
                    document.getElementById("cardPositive").setAttribute("width","300");
                    positiveStatus = true;
                }
            },
            cardBack(){
                if(backStatus == true){
                    document.getElementById("cardBack").setAttribute("height","300");
                    document.getElementById("cardBack").setAttribute("width","500");
                    backStatus = false;
                }else{
                    document.getElementById("cardBack").setAttribute("height","180");
                    document.getElementById("cardBack").setAttribute("width","300");
                    backStatus = true;
                }
            },

            //  返回
            returnList(){
                window.location.href = ress__+ '/#/' + 'allOrdersDetail'
            },

            //确认备注
            confirmMark() {
                var me = this;
                if(me.show_description == '') {
                    alert("内容不能为空");
                    return;
                }
                me.detail.description = me.show_description;
                var orderId = Utils.getSession("current_order_id");
                var item = {
                    description: me.show_description,
                    orderId: orderId
                };
                me.mark_dialogShowCheck = false;
                me.edit_is_show = true;
                me.add_mark_is_show = false;
                me.mark_is_show = true;
                Utils.ajax(Api.order.addOrUpdateMark, item,(res) => {
                    if(+res.status != 1) {
                        me.$message({
                            showClose: true,
                            message: '成功更新备注',
                            type: 'success'
                        });
                    }
                });
            },
            handleClose(){
                var me = this;
                me.mark_dialogShowCheck = false;
            },
    getAllOperationCase () {
        var me = this;
        Utils.ajax(Api.order.getOperationCase, {},(res) => {
            if (res.status == 1) {
                var data = res.data;
                if(data instanceof  Array) {
                    data.forEach((item) => {
                        if(item.id != '1') {
                            me.check_status_process.push({
                                label: item.refuseEx,
                                value: item.id
                            });
                        }
                    });
                }
            }
        });
    },
            confirmRefuseStatus(){
                var me = this;
                if(me.check_status == '') {
                    Dialog.alert( '请选择拒绝原因');
                    return;
                }
                me.updateOrderStatus(me.check_status);
            },
            getOrderDetail (order_id) {
                Utils.ajax(Api.order.queryOrderDetail, {
                    orderId: order_id
                },(res) => {
                    if (res.status == 1) {
                        var data = res.data;
                        var me = this;
                        me.detail = data;
                        //是否显示备注
                        //  出生日期格式化
                        data.birthday = Utils.formatDate(new Date(data.birthday), 'YYYY-MM-DD');
                        // 月利率
                        var formatDate = data.rate*1000;
                        data.rate = formatDate.toFixed(1) + "‰";

                        if(data.description === undefined) {
                            me.add_mark_is_show = true;
                            me.edit_is_show = false;
                            me.mark_is_show = false;
                        } else {
                            me.add_mark_is_show = false;
                            me.edit_is_show = true;
                            me.mark_is_show = true;
                        }
                        //身份证照片
                        if(data.photoUrl0 != null) {
                            me.detail.photoUrl0 = Api.imageUrl.id_card_url + '/' + data.photoUrl0;
                        }
                        if(data.photoUrl1 != null) {
                            me.detail.photoUrl1 = Api.imageUrl.id_card_url + '/' + data.photoUrl1;
                        }
                        //获取协议
                        var url = Api.contract.get + '?orderNo=' + me.detail.borrowNid;
                        var agreementString = '';
                        $.get(url, function(result){
                            if(result != null) {
                                if(result.data != null) {
                                    var tempArr = result.data;
                                    if(tempArr instanceof  Array) {
                                        tempArr.forEach((i) => {
                                            agreementString = agreementString + "<a href='"+i.signUrl+"'>"+i.description+"</a>"+"&nbsp;&nbsp;&nbsp;";
                                        });
                                        if(agreementString != '') {
                                            $("p").html(agreementString);
                                        }
                                    }
                                }
                            }
                        });
                    }
                });
            },
            updateOrderStatus (status) {
                var me = this;
                var orderId = Utils.getSession("current_order_id");
                var sessionCode = Utils.getSession('token');
                Utils.ajax(Api.user.validSession, {
                    sessionCode: sessionCode
                }, (res) => {
                    if (+res.status != 1) {
                        window.location.href = ress__+ '/#/' + 'login'
                        return;
                    }
                });
                Utils.ajax(Api.order.updateStatus, {
                    status: status,
                    orderId: orderId,
                    sessionCode: sessionCode
                },(res) => {
                    me.$message({
                        showClose: true,
                        message: '成功更新状态',
                        type: 'success'
                    });
                    var role_id = Utils.getSession('role');
                    if(role_id == '1') {
                        window.location.href = ress__+ '/#/' + 'investigating';
                    } else if(role_id == '2') {
                        window.location.href = ress__+ '/#/' + 'allOrdersDetail';
                    }
                })
            },
        }
    }

</script>


<style lang="sass" scoped>
    @import './index.scss';
    .upload_file {
        .el-upload, .el-upload-dragger{
            width: 100%;
        }
    }
</style>
