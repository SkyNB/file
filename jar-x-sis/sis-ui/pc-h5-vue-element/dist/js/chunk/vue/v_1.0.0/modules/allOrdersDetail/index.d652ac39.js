webpackJsonp([20],{210:function(e,t,a){a(340);var n=a(51)(a(244),a(309),"data-v-2736ec46",null);n.options.__file="F:\\Workspaces\\Java\\Project\\Mobanker\\jar-x-sis\\sis-ui\\pc-h5-vue-element\\src\\vue\\v_1.0.0\\modules\\allOrdersDetail\\index.vue",n.esModule&&Object.keys(n.esModule).some(function(e){return"default"!==e&&"__esModule"!==e})&&console.error("named exports are not supported in *.vue files."),n.options.functional&&console.error("[vue-loader] index.vue: functional components are not supported with templates, they should use render functions."),e.exports=n.exports},244:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0}),t.default={name:"allOrdersDetail",data:function(){return{fromDate:"",endDate:"",query_name:"",query_id_card:"",query_cell_phone:"",orderDate:"",orderStatus:"",formLabelWidth:"100px",pickerOptions:{disabledDate:function(e){return e.getTime()>Date.now()}},tableData:[],total:0,currentPage:1,pageSize:20,basePageSize:20,pages:[20,50,100],status_process:[{value:"0",label:"拒绝"},{value:"1",label:"通过"}],check_status:"",role_id:""}},created:function(){var e=this;e.orderDate=Utils.getSession("orderDate"),e.orderStatus=Utils.getSession("orderStatus"),e.isChecked=Utils.getSession("isChecked"),e.detailStatus=Utils.getSession("detailStatus"),Utils.getSession("token")||(window.location.href=ress__+"/#/login");var t=Utils.getSession("token"),a={sessionCode:t};Utils.ajax(Api.user.validSession,a,function(e){if(1!=+e.status)return void(window.location.href=ress__+"/#/login")})},methods:{handleSizeChange:function(e){var t=this;t.pageSize=e,t.query({})},handleCurrentChange:function(e){var t=this;t.currentPage=e,t.query({})},tel_check:function(e){Utils.setSession("current_order_id",e),window.location.href=ress__+"/#/tel_check"},goBack:function(){router.go(-1)},query:function(){var e=this,t=this;if(""==t.check_status)return void Dialog.alert("请选择订单状态");var a=Utils.getSession("token"),n="",r="";""!=t.fromDate&&(n=Utils.formatDate(new Date(t.fromDate),"YYYY-MM-DD")),""!=t.endDate&&(r=Utils.formatDate(new Date(t.endDate),"YYYY-MM-DD"));var o={fromDate:n,endDate:r,name:t.query_name,idCardNum:t.query_id_card,cellPhone:t.query_cell_phone,pageSeq:t.currentPage,pageSize:t.pageSize,sessionCode:a,status:t.check_status};Utils.ajax(Api.order.queryOrderByCondition,o,function(t){var a=e;if(1==+t.status){var n=t.data.orderItems;a.total=t.data.total,a.tableData=[],n instanceof Array&&n.forEach(function(e){e.idCardHide=null==e.idCardNum?e.idCardNum:e.idCardNum.replace(/^(\d{6})\d+(\w{4})$/,"$1****$2"),e.phoneHide=null==e.cellphone?e.cellphone:e.cellphone.replace(/^(\d{3})\d+(\d{4})$/,"$1****$2"),e.debitAccountHide=null==e.debitaccount?e.debitaccount:e.debitaccount.replace(/^(\d{6})\d+(\d{3})$/,"$1****$2"),e.loadAccountHide=null==e.loadaccount?e.loadaccount:e.loadaccount.replace(/^(\d{6})\d+(\d{3})$/,"$1****$2"),e.addressHide=null==e.homeadr?e.homeadr:e.homeadr.replace(/^(.{6}).*!/,"$1****");var t="";t="0"==e.checkStatus?"已拒绝":"1"==e.checkStatus?"已通过":"待审批",a.tableData.push({id:e.id,orderId:e.borrowNid,renter:e.renter,idCard:e.idCardNum,idCardHide:e.idCardHide,phone:e.cellphone,phoneHide:e.phoneHide,money:e.amount,address:e.homeAddress,checkStatus:e.checkStatus,operation:t})})}})},reset:function(){var e=this;e.fromDate="",e.endDate="",e.query_name="",e.query_id_card="",e.query_cell_phone="",e.check_status=""}}}},271:function(e,t,a){t=e.exports=a(87)(!0),t.push([e.i,'\n@charset "UTF-8";\n.order_list[data-v-2736ec46] {\n  margin: 20px;\n}\n.form .row[data-v-2736ec46] {\n  margin-top: 20px;\n}\n.form .row span[data-v-2736ec46] {\n    width: 70px;\n    display: inline-block;\n}\n.form .row .split[data-v-2736ec46] {\n    width: 30px;\n    text-align: center;\n}\n.form .row .label_status[data-v-2736ec46], .form .row .query_btn[data-v-2736ec46] {\n    margin-left: 30px;\n}\n.form .row input[data-v-2736ec46] {\n    cursor: pointer;\n}\n.table[data-v-2736ec46] {\n  margin-top: 20px;\n}\n.pagination[data-v-2736ec46] {\n  margin-top: 20px;\n  text-align: right;\n}\n.order_detail_table table[data-v-2736ec46] {\n  width: 100%;\n  border-color: #dfe6ec;\n  border-width: 1px;\n}\n.order_detail_table table td[data-v-2736ec46], .order_detail_table table th[data-v-2736ec46] {\n    padding: 3px 5px;\n}\n.checkbox_status[data-v-2736ec46] {\n  float: left;\n  margin-top: 15px;\n}\n.process_select[data-v-2736ec46] {\n  width: 150px;\n  height: 36px;\n  border-radius: 4px;\n}\n.btn1[data-v-2736ec46] {\n  margin-left: 12px;\n}\n.billNo[data-v-2736ec46] {\n  font-family: "\\5B8B\\4F53";\n  font-size: larger;\n  color: gold;\n}\n.light_color[data-v-2736ec46] {\n  color: #1fa7d4;\n}\n',"",{version:3,sources:["F:/Workspaces/Java/Project/Mobanker/jar-x-sis/sis-ui/pc-h5-vue-element/src/vue/v_1.0.0/modules/allOrdersDetail/index.vue"],names:[],mappings:";AAAA,iBAAiB;AACjB;EACE,aAAa;CAAE;AAEjB;EACE,iBAAiB;CAAE;AACnB;IACE,YAAY;IACZ,sBAAsB;CAAE;AAC1B;IACE,YAAY;IACZ,mBAAmB;CAAE;AACvB;IACE,kBAAkB;CAAE;AACtB;IACE,gBAAgB;CAAE;AAEtB;EACE,iBAAiB;CAAE;AAErB;EACE,iBAAiB;EACjB,kBAAkB;CAAE;AAEtB;EACE,YAAY;EACZ,sBAAsB;EACtB,kBAAkB;CAAE;AACpB;IACE,iBAAiB;CAAE;AAEvB;EACE,YAAY;EACZ,iBAAiB;CAAE;AAErB;EACE,aAAa;EACb,aAAa;EACb,mBAAmB;CAAE;AAEvB;EACE,kBAAkB;CAAE;AAEtB;EACE,0BAAkB;EAClB,kBAAkB;EAClB,YAAY;CAAE;AAEhB;EACE,eAAe;CAAE",file:"index.vue",sourcesContent:['@charset "UTF-8";\n.order_list {\n  margin: 20px; }\n\n.form .row {\n  margin-top: 20px; }\n  .form .row span {\n    width: 70px;\n    display: inline-block; }\n  .form .row .split {\n    width: 30px;\n    text-align: center; }\n  .form .row .label_status, .form .row .query_btn {\n    margin-left: 30px; }\n  .form .row input {\n    cursor: pointer; }\n\n.table {\n  margin-top: 20px; }\n\n.pagination {\n  margin-top: 20px;\n  text-align: right; }\n\n.order_detail_table table {\n  width: 100%;\n  border-color: #dfe6ec;\n  border-width: 1px; }\n  .order_detail_table table td, .order_detail_table table th {\n    padding: 3px 5px; }\n\n.checkbox_status {\n  float: left;\n  margin-top: 15px; }\n\n.process_select {\n  width: 150px;\n  height: 36px;\n  border-radius: 4px; }\n\n.btn1 {\n  margin-left: 12px; }\n\n.billNo {\n  font-family: "宋体";\n  font-size: larger;\n  color: gold; }\n\n.light_color {\n  color: #1fa7d4; }\n'],sourceRoot:""}])},309:function(e,t,a){e.exports={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[a("el-breadcrumb",{attrs:{separator:"/"}},[a("el-breadcrumb-item",[e._v("控制面板")]),e._v(" "),a("el-breadcrumb-item",{attrs:{to:{path:"/index"}}},[e._v("首页")]),e._v(" "),a("el-breadcrumb-item",[e._v("所有订单")])],1),e._v(" "),a("div",{staticClass:"element__body"},[a("div",{staticClass:"row"},[a("el-form",{attrs:{inline:!0}},[a("el-form-item",{attrs:{label:"申请日期:"}},[a("el-date-picker",{attrs:{format:"yyyy-MM-dd",type:"date",placeholder:"选择日期"},model:{value:e.fromDate,callback:function(t){e.fromDate=t},expression:"fromDate"}})],1),e._v(" "),a("span",{staticClass:"split"},[e._v("-")]),e._v(" "),a("el-date-picker",{attrs:{format:"yyyy-MM-dd",type:"date",placeholder:"选择日期"},model:{value:e.endDate,callback:function(t){e.endDate=t},expression:"endDate"}}),e._v(" "),a("el-form-item",{attrs:{label:"姓名:"}},[a("el-input",{attrs:{"auto-complete":"off"},model:{value:e.query_name,callback:function(t){e.query_name=t},expression:"query_name"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"身份证号:"}},[a("el-input",{attrs:{"auto-complete":"off"},model:{value:e.query_id_card,callback:function(t){e.query_id_card=t},expression:"query_id_card"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"手机号:"}},[a("el-input",{attrs:{"auto-complete":"off"},model:{value:e.query_cell_phone,callback:function(t){e.query_cell_phone=t},expression:"query_cell_phone"}})],1),e._v(" "),a("el-select",{staticClass:"process_select",attrs:{placeholder:"请选择审核状态"},model:{value:e.check_status,callback:function(t){e.check_status=t},expression:"check_status"}},e._l(e.status_process,function(e){return a("el-option",{key:e.value,attrs:{label:e.label,value:e.value}})})),e._v(" "),a("el-form-item",[a("el-button",{staticClass:"query_btn",attrs:{type:"primary"},on:{click:e.query}},[e._v("查询")])],1),e._v(" "),a("el-form-item",[a("el-button",{staticClass:"reset_btn",attrs:{type:"primary"},on:{click:e.reset}},[e._v("重置")])],1)],1)],1),e._v(" "),a("div",{staticClass:"table"},[a("el-table",{attrs:{data:e.tableData,border:""}},[a("el-table-column",{attrs:{label:"订单编号",width:"200"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("span",{staticClass:"light_color"},[e._v(e._s(t.row.orderId))])]}}])}),e._v(" "),a("el-table-column",{attrs:{prop:"renter",label:"姓名",width:"90"}}),e._v(" "),a("el-table-column",{attrs:{label:"身份证号",width:"150"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-tooltip",{attrs:{content:t.row.idCard,placement:"top-start"}},[a("span",{staticClass:"light_color"},[e._v(e._s(t.row.idCardHide))])])]}}])}),e._v(" "),a("el-table-column",{attrs:{label:"手机号",width:"120"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-tooltip",{attrs:{content:t.row.phone,placement:"top-start"}},[a("span",{staticClass:"light_color"},[e._v(e._s(t.row.phoneHide))])])]}}])}),e._v(" "),a("el-table-column",{attrs:{prop:"money",label:"申请金额(元)",width:"120"}}),e._v(" "),a("el-table-column",{attrs:{prop:"address",label:"家庭地址",width:"260"}}),e._v(" "),a("el-table-column",{attrs:{prop:"operation",label:"审核状态",width:"260"}}),e._v(" "),a("el-table-column",{attrs:{label:"操作"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-button",{attrs:{type:"text"},on:{click:function(a){e.tel_check(t.row.id)}}},[e._v("电核")])]}}])})],1)],1),e._v(" "),a("div",{staticClass:"pagination"},[a("el-pagination",{attrs:{"current-page":e.currentPage,"page-sizes":e.pages,"page-size":e.pageSize,layout:"total, sizes, prev, pager, next, jumper",total:+e.total},on:{"size-change":e.handleSizeChange,"current-change":e.handleCurrentChange}})],1)])],1)},staticRenderFns:[]},e.exports.render._withStripped=!0},340:function(e,t,a){var n=a(271);"string"==typeof n&&(n=[[e.i,n,""]]),n.locals&&(e.exports=n.locals);a(88)("0cf73311",n,!1)}});