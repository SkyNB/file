webpackJsonp([17],{213:function(e,t,o){o(364);var a=o(51)(o(247),o(331),"data-v-e3d7d8d2",null);a.options.__file="F:\\Workspaces\\Java\\Project\\Mobanker\\jar-x-sis\\sis-ui\\pc-h5-vue-element\\src\\vue\\v_1.0.0\\modules\\bill\\index.vue",a.esModule&&Object.keys(a.esModule).some(function(e){return"default"!==e&&"__esModule"!==e})&&console.error("named exports are not supported in *.vue files."),a.options.functional&&console.error("[vue-loader] index.vue: functional components are not supported with templates, they should use render functions."),e.exports=a.exports},247:function(e,t,o){"use strict";Object.defineProperty(t,"__esModule",{value:!0}),t.default={name:"bill",data:function(){return{dialogVisible:!1,disableUpload:!1,pager:{pageNo:1,pageSize:10,totalSize:0},dataList:[],dataListItem:{id:"",name:"",status:"0",descr:""},form:{fromDate:"",endDate:"",status:""},pickerOptions:{disabledDate:function(e){return e.getTime()>Date.now()}},options:[{value:"",label:"全部"},{value:0,label:"未确认"},{value:1,label:"已确认"}],tableData:[],confirmAble:!1,loadingInstance:null,cacheOrderId:-1,cacheRow:null,importUrl:Api.importOrder,loadingTable:!1}},created:function(){Utils.getSession("token")||(window.location.href=ress__+"/#/login");var e=Utils.getSession("token"),t={sessionCode:e};Utils.ajax(Api.user.validSession,t,function(e){if(1!=+e.status)return void(window.location.href=ress__+"/#/login")});var o=this;if(o.confirmAble=2==+Utils.getSession("loginType"),o.form.fromDate=Utils.getSession("billQuery-fromDate"),o.form.endDate=Utils.getSession("billQuery-endDate"),o.role=Utils.getSession("role"),o.form.fromDate)o.form.status=Utils.getSession("billQuery-status"),Utils.removeSession("billQuery-fromDate"),Utils.removeSession("billQuery-endDate"),Utils.removeSession("billQuery-status");else{var a=new Date,s=Utils.formatDate(a,"YYYY/MM/DD"),r=a.getTime()-6048e5;r=Utils.formatDate(new Date(r),"YYYY/MM/DD"),o.form.fromDate=r,o.form.endDate=s}o.getList(o.form.fromDate,o.form.endDate,o.form.status),window.UNIQUE=Math.random().toString(36).substr(2).toUpperCase()},methods:{sizeChange:function(e){this.$data.pager.pageSize=e,this.query()},currentChange:function(e){this.$data.pager.pageNo=e,this.query()},exportExcel:function(e){window.open(e)},downloadpackage:function(e){window.open(e)},goDetail:function(e){var t=this;Utils.setSession("isChecked","1"),Utils.setSession("detailStatus","1"),t.reGoDetail(e),window.location.href=ress__+"/#/order"},goDetailCheck:function(e){var t=this;Utils.setSession("detailStatus","0"),Utils.setSession("isChecked","0"),t.reGoDetail(e),window.location.href=ress__+"/#/order"},reGoDetail:function(e){var t=this;Utils.setSession("orderDate",e.date),Utils.setSession("orderStatus",e.status),t.form.fromDate=t.form.fromDate instanceof Date?Utils.formatDate(t.form.fromDate,"YYYY/MM/DD"):t.form.fromDate,t.form.endDate=t.form.endDate instanceof Date?Utils.formatDate(t.form.endDate,"YYYY/MM/DD"):t.form.endDate,Utils.setSession("billQuery-fromDate",t.form.fromDate),Utils.setSession("billQuery-endDate",t.form.endDate),Utils.setSession("billQuery-status",t.form.status),Utils.setSession("billQuery-account",e.orderId)},query:function(){var e=this,t=e.form.fromDate,o=e.form.endDate,a=e.form.status;if(t instanceof Date&&(t=Utils.formatDate(t,"YYYY/MM/DD")),o instanceof Date&&(o=Utils.formatDate(o,"YYYY/MM/DD")),new Date(t)>new Date(o))return void this.$message({showClose:!0,message:"开始时间应小于等于结束时间",type:"error"});this.getList(t,o,a)},allOrderDetail:function(){window.location.href=ress__+"/#/allOrdersDetail"},confirmBill:function(e){var t=this,o=Utils.getSession("token");Utils.getSession("customerCode");Utils.ajax(Api.bill.confirmBill,{sessionCode:o,account:e},function(e){console.log(e),1==+e.status?(t.$message({showClose:!0,message:"确认成功",type:"success"}),t.query()):t.$message({showClose:!0,message:e.msg||"确认失败",type:"error"})})},getList:function(e,t,o){var a=this,s=Utils.getSession("token"),r=Utils.getSession("customerCode"),n={customerCode:r,dateFrom:e,dateTo:t,sessionCode:s,confirmStatus:o};Utils.ajax(Api.bill.list,n,function(e){if(1==+e.status){a.tableData=[],a.loadingTable=!1;var t=e.data;t&&t instanceof Array&&t.forEach(function(e){a.tableData.push({date:e.date,orderId:e.summaryOrderId,completeNum:-1,permissionSet:e.permissionSet,status:e.status,money:e.amount,count:e.quantity,orderSource:e.orderSource,confirm:"已确认"===e.status,loanStatus:e.loanStatus,customerLink:res__+"/order/exportCustomers?billNumber="+e.summaryOrderId+"&date="+e.date+"&sessionCode="+s,orderLink:res__+"/order/exportOrderItems?billNumber="+e.summaryOrderId+"&date="+e.date+"&sessionCode="+s,assetPackageLink:res__+"/order/downloadAssetPackage?billNumber="+e.summaryOrderId+"&date="+e.date+"&sessionCode="+s})})}else a.$message({showClose:!0,message:e.msg||"数据请求失败",type:"error"})})},closeDialog:function(e){e()},upload:function(e){e.orderId&&(this.dialogVisible=!0,this.cacheOrderId=e.orderId,this.cacheRow=e),console.log(e)},handleRemove:function(e,t){console.log(e),console.log(t)},submitUpload:function(){this.disableUpload=!0,this.$refs.upload.submit(),this.dialogVisible=!1,this.loadingTable=!0},beforeUpload:function(e){var t="application/vnd.ms-excel"===e.type||"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"===e.type;return t||this.$message.error("上传的文件只能是 EXCEL 格式!"),t},uploadProgress:function(e,t,o){this.disableUpload=!0},uploadError:function(e,t,o){this.disableUpload=!1,this.loadingTable=!1,this.$message.error("文件上传失败!"),console.log(e)},uploadSuccess:function(e,t,o){var a=this;this.disableUpload=!1,this.$http.post(Api.handleOrder,{account:this.cacheOrderId,incr_index:e.data}).then(function(t){1==+t.data.status?a.getStatus(e.data,window.UNIQUE):a.$message.error("文件处理错误!")})},getStatus:function(e,t){var o=this;this.$http.post(Api.queryStatus,{index:e}).then(function(a){console.log(a.data),0==+a.data.data?t===window.UNIQUE&&setTimeout(function(){o.getStatus(e,t)},2e3):1==+a.data.data&&o.$http.post(Api.completeNum,{index:e}).then(function(e){console.log(e.data),console.log(e.data.data),o.cacheRow&&o.cacheRow instanceof Object&&(o.cacheRow.completeNum=e.data.data),o.loadingTable=!1,o.$message.success("文件上传成功!"),o.dialogVisible=!1,o.$refs.upload.clearFiles(),console.log(e.data)})})},makeLoans:function(e){}}}},295:function(e,t,o){t=e.exports=o(87)(!0),t.push([e.i,"\n.bill_list[data-v-e3d7d8d2] {\n  margin: 20px;\n}\n.form .row[data-v-e3d7d8d2] {\n  margin-top: 20px;\n}\n.form .row span[data-v-e3d7d8d2] {\n    width: 70px;\n    display: inline-block;\n}\n.form .row .split[data-v-e3d7d8d2] {\n    width: 30px;\n    text-align: center;\n}\n.form .row .label_status[data-v-e3d7d8d2], .form .row .query_btn[data-v-e3d7d8d2] {\n    margin-left: 30px;\n}\n.form .row input[data-v-e3d7d8d2] {\n    cursor: pointer;\n}\n.table[data-v-e3d7d8d2] {\n  margin-top: 20px;\n}\n.pagination[data-v-e3d7d8d2] {\n  margin-top: 20px;\n  text-align: right;\n  display: none;\n}\n.upload_file .el-upload[data-v-e3d7d8d2], .upload_file .el-upload-dragger[data-v-e3d7d8d2] {\n  width: 100%;\n}\n","",{version:3,sources:["F:/Workspaces/Java/Project/Mobanker/jar-x-sis/sis-ui/pc-h5-vue-element/src/vue/v_1.0.0/modules/bill/index.vue"],names:[],mappings:";AAAA;EACE,aAAa;CAAE;AAEjB;EACE,iBAAiB;CAAE;AACnB;IACE,YAAY;IACZ,sBAAsB;CAAE;AAC1B;IACE,YAAY;IACZ,mBAAmB;CAAE;AACvB;IACE,kBAAkB;CAAE;AACtB;IACE,gBAAgB;CAAE;AAEtB;EACE,iBAAiB;CAAE;AAErB;EACE,iBAAiB;EACjB,kBAAkB;EAClB,cAAc;CAAE;AAElB;EACE,YAAY;CAAE",file:"index.vue",sourcesContent:[".bill_list {\n  margin: 20px; }\n\n.form .row {\n  margin-top: 20px; }\n  .form .row span {\n    width: 70px;\n    display: inline-block; }\n  .form .row .split {\n    width: 30px;\n    text-align: center; }\n  .form .row .label_status, .form .row .query_btn {\n    margin-left: 30px; }\n  .form .row input {\n    cursor: pointer; }\n\n.table {\n  margin-top: 20px; }\n\n.pagination {\n  margin-top: 20px;\n  text-align: right;\n  display: none; }\n\n.upload_file .el-upload, .upload_file .el-upload-dragger {\n  width: 100%; }\n"],sourceRoot:""}])},331:function(e,t,o){e.exports={render:function(){var e=this,t=e.$createElement,o=e._self._c||t;return o("div",[o("el-breadcrumb",{attrs:{separator:"/"}},[o("el-breadcrumb-item",[e._v("控制面板")]),e._v(" "),o("el-breadcrumb-item",{attrs:{to:{path:"/index"}}},[e._v("首页")]),e._v(" "),o("el-breadcrumb-item",{attrs:{to:{path:"/table"}}},[e._v("数据")]),e._v(" "),o("el-breadcrumb-item",[e._v("人工信审")])],1),e._v(" "),o("div",{staticClass:"element__body"},[o("div",{staticClass:"row"},[o("el-form",{attrs:{inline:!0,model:e.form}},[o("el-form-item",{attrs:{label:"对账日期:"}},[o("el-date-picker",{attrs:{editable:!1,clearable:!1,type:"date",placeholder:"选择日期","picker-options":e.pickerOptions},model:{value:e.form.fromDate,callback:function(t){e.$set(e.form,"fromDate",t)},expression:"form.fromDate"}})],1),e._v(" "),o("span",{staticClass:"split"},[e._v("-")]),e._v(" "),o("el-date-picker",{attrs:{editable:!1,clearable:!1,type:"date",placeholder:"选择日期","picker-options":e.pickerOptions},model:{value:e.form.endDate,callback:function(t){e.$set(e.form,"endDate",t)},expression:"form.endDate"}}),e._v(" "),o("el-form-item",{attrs:{label:"确认状态:"}},[o("el-select",{attrs:{placeholder:"请选择"},model:{value:e.form.status,callback:function(t){e.$set(e.form,"status",t)},expression:"form.status"}},e._l(e.options,function(e){return o("el-option",{key:e.value,attrs:{label:e.label,value:e.value}})}))],1),e._v(" "),o("el-form-item",[o("el-button",{staticClass:"query_btn",attrs:{type:"primary"},on:{click:e.query}},[e._v("查询")])],1),e._v(" "),o("el-form-item",[o("el-button",{staticClass:"allOrderDetail_btn",attrs:{type:"primary"},on:{click:e.allOrderDetail}},[e._v("所有订单详情")])],1)],1)],1),e._v(" "),o("div",{staticClass:"table"},[o("el-table",{attrs:{data:e.tableData,border:""}},[o("el-table-column",{attrs:{prop:"date",label:"对账日期",width:"120"}}),e._v(" "),o("el-table-column",{attrs:{prop:"status",label:"确认状态",width:"120"}}),e._v(" "),o("el-table-column",{attrs:{prop:"money",label:"放款总额（元）",width:"180"}}),e._v(" "),o("el-table-column",{attrs:{prop:"count",label:"订单总数",width:"150"}}),e._v(" "),o("el-table-column",{attrs:{prop:"orderSource",label:"来源",width:"150"}}),e._v(" "),o("el-table-column",{attrs:{label:"操作"},scopedSlots:e._u([{key:"default",fn:function(t){return["已确认"!=t.row.status?o("el-button",{attrs:{type:"text"},on:{click:function(o){e.goDetailCheck(t.row)}}},[e._v("\n                            人工检查\n                        ")]):e._e(),e._v(" "),"已确认"!=t.row.status?o("el-button",{attrs:{type:"text"},on:{click:function(o){e.confirmBill(t.row.orderId)}}},[e._v("完成检查\n                        ")]):e._e(),e._v(" "),"已确认"==t.row.status?o("el-button",{attrs:{type:"text"},on:{click:function(o){e.goDetail(t.row)}}},[e._v("明细\n                        ")]):e._e(),e._v(" "),"已确认"==t.row.status?o("el-button",{attrs:{type:"text"},on:{click:function(o){e.exportExcel(t.row.customerLink)}}},[e._v("导出客户Excel\n                        ")]):e._e(),e._v(" "),"已确认"==t.row.status?o("el-button",{attrs:{type:"text"},on:{click:function(o){e.exportExcel(t.row.orderLink)}}},[e._v("导出业务Excel\n                        ")]):e._e(),e._v(" "),"已确认"==t.row.status?o("el-button",{attrs:{type:"text"},on:{click:function(o){e.upload(t.row)}}},[e._v("上传回执\n                        ")]):e._e(),e._v(" "),o("el-button",{attrs:{type:"text"},on:{click:function(o){e.downloadpackage(t.row.assetPackageLink)}}},[e._v("下载资产包\n                        ")]),e._v(" "),"已确认"==t.row.status&&"柜员"==e.role&&"0"==t.row.loanStatus?o("el-button",{attrs:{type:"text"},on:{click:function(o){e.makeLoans(t.row)}}},[e._v("放款\n                        ")]):e._e(),e._v(" "),"已确认"==t.row.status&&"柜员"==e.role&&"1"==t.row.loanStatus?o("span",{attrs:{type:"text"}},[e._v("已放款")]):e._e(),e._v(" "),t.row.completeNum>-1?o("span",[e._v("已处理"+e._s(t.row.completeNum)+"条")]):e._e()]}}])})],1),e._v(" "),o("el-pagination",{attrs:{"current-page":e.pager.pageNo,"page-sizes":[10,20,50],"page-size":e.pager.pageSize,layout:"sizes, prev, pager, next ,total,  jumper",total:e.pager.totalSize},on:{"size-change":e.sizeChange,"current-change":e.currentChange}})],1)]),e._v(" "),o("el-dialog",{attrs:{title:"上传回执",visible:e.dialogVisible,size:"tiny","before-close":e.closeDialog},on:{"update:visible":function(t){e.dialogVisible=t}}},[o("el-upload",{ref:"upload",staticClass:"upload_file",attrs:{drag:"",action:e.importUrl,"auto-upload":!1,"on-remove":e.handleRemove,"on-progress":e.uploadProgress,"on-error":e.uploadError,"on-success":e.uploadSuccess,"before-upload":e.beforeUpload}},[o("i",{staticClass:"el-icon-upload"}),e._v(" "),o("div",{staticClass:"el-upload__text"},[e._v("将文件拖到此处，或"),o("em",[e._v("点击上传")])])]),e._v(" "),o("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[o("el-button",{on:{click:function(t){e.dialogVisible=!1}}},[e._v("取 消")]),e._v(" "),o("el-button",{attrs:{type:"primary",disabled:e.disableUpload},on:{click:e.submitUpload}},[e._v("上 传")])],1)],1)],1)},staticRenderFns:[]},e.exports.render._withStripped=!0},364:function(e,t,o){var a=o(295);"string"==typeof a&&(a=[[e.i,a,""]]),a.locals&&(e.exports=a.locals);o(88)("13c5e654",a,!1)}});