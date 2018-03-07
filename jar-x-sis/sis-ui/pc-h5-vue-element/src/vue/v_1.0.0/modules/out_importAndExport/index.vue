<template>
    <div>
        <el-breadcrumb separator="/">
            <el-breadcrumb-item>控制面板</el-breadcrumb-item>
            <el-breadcrumb-item :to="{ path: '/index' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>外前置-导入导出</el-breadcrumb-item>
        </el-breadcrumb>

        <div class="element__body">

            <el-button style="font-size: 50px; position:absolute; top:120px; left:350px; width:270px; height:100px;" class="btn1" type="success" @click="upload(importUrl)" round>上传
            </el-button>

            <el-button style="font-size: 50px; position:absolute; top:350px; left:350px; width:270px; height:100px;" class="btn2" type="success" @click="downloadFile(downloadUrl)" round>下载
            </el-button>

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
                </el-upload>
                <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="submitUpload" :disabled="disableUpload">上 传</el-button>
            </span>
            </el-dialog>


            <el-dialog title="请选择时间区间" :visible.sync="down_dialogVisible" size="tiny" :before-close="down_closeDialog">
                <template>
                    <div class="block">
                        <span class="demonstration">开始时间</span>
                        <el-date-picker
                                v-model="start_date"
                                format="yyyy-MM-dd"
                                type="date"
                                placeholder="选择日期">
                        </el-date-picker>
                    </div>
                    <div class="block">
                        <span class="demonstration">结束时间</span>
                        <el-date-picker
                                v-model="end_date"
                                format="yyyy-MM-dd"
                                align="right"
                                type="date"
                                placeholder="选择日期"
                                :picker-options="pickerOptions1">
                        </el-date-picker>
                    </div>
                </template>
                <span slot="footer" class="dialog-footer">
                    <el-button @click="down_closeDialog">取 消</el-button>
                    <el-button type="primary" @click="confirm_download">确 定</el-button>
                </span>
            </el-dialog>

        </div>

    </div>
</template>

<script>
    import ElButton from "../../../../../node_modules/element-ui/packages/button/src/button";
    export default {
        name: 'out_importAndExport',
        data () {
            return {
                start_date: '',
                end_date: '',
                down_dialogVisible: false,
                dialogVisible: false,
                disableUpload: false,
                loadingTable: false,
                importUrl: Api.order.importOutFile,
                downloadUrl: Api.order.downloadOutFile,
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
            upload (){
                this.dialogVisible = true;
            },
            // 关闭对话框
            closeDialog (done) {
                done();
            },
            // 删除文件的回调
            handleRemove (file, fileList) {
                console.log(file);
                console.log(fileList);
            },
            // 上传前进行文件类型判断
            beforeUpload (file) {
                return true;
            },
            // 上传中
            uploadProgress (event, file, fileList) {

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
                var me = this;
                if(response.status == 1) {
                    alert("文件上传成功");
                } else {
                    alert("文件上传失败");
                }
            },
            submitUpload () {
                this.$refs.upload.submit();
                this.dialogVisible = false;
                this.loadingTable = true;
            },
            downloadFile (link){
                var me = this;
                me.down_dialogVisible = true;
            },
            down_closeDialog () {
                var me = this;
                me.start_date = '';
                me.end_date = '';
                me.down_dialogVisible = false;
            },
            confirm_download () {
                var me = this;
                if((me.start_date == '')||(me.end_date == '')) {
                    alert('请选择时间区间然后点击确定');
                    return;
                }
                if (new Date(me.start_date) > new Date(me.end_date)) {
                    this.$message({
                        showClose: true,
                        message: '开始时间应小于等于结束时间',
                        type: 'error'
                    })
                    return
                }
                let startDate = Utils.formatDate(me.start_date, 'YYYY-MM-DD');
                let endDate = Utils.formatDate(me.end_date, 'YYYY-MM-DD');
                window.open(me.downloadUrl+'?startDate='+startDate+'&endDate='+endDate);
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