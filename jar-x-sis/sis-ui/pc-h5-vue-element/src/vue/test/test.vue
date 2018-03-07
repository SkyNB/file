<template>
  <div>
    <el-breadcrumb separator="/">
      <el-breadcrumb-item>控制面板</el-breadcrumb-item>
      <el-breadcrumb-item :to="{ path: '/index' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item :to="{ path: '/test' }">Form</el-breadcrumb-item>
      <el-breadcrumb-item >test</el-breadcrumb-item>
    </el-breadcrumb>
    <div class="element__body">
      <el-row :gutter="20">
        <el-col :span="2"><el-button type="primary" @click="test1">alert框</el-button></el-col>
        <el-col :span="2"><el-button type="primary" @click="test2">消息框提示</el-button></el-col>
        <el-col :span="3"><el-button type="primary" @click="test3">messageBOX框</el-button></el-col>
        <el-col :span="2"><el-button type="primary" @click="test4">loading</el-button></el-col>
        <el-col :span="3"><el-button type="primary" @click="test5">AJAX(Form Data)</el-button></el-col>
        <el-col :span="3"><el-button type="primary" @click="test6">AJAX(application/json)</el-button></el-col>
        <el-col :span="3"><el-button type="primary" @click="test7">AJAX</el-button></el-col>
      </el-row>
      <el-dialog :title="title" :visible.sync="dialogFormVisible">
        <el-row :gutter="20">
          <el-col :span="3" class="form-label">描述：</el-col>
          <el-col :span="21">
            <el-input  type="textarea" :rows="4" placeholder="描述"></el-input>
          </el-col>
        </el-row>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" >新 增</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>
<script>
import { mapState } from 'vuex'
export default {
  name: 'test_test',
  components : {
    
  },
  computed: mapState({
    'stateObject' : state => state.stateObject //简写。完整的:this.$store.state.stateObject访问
  }),
  mounted (){
    util.isLogin();//判断是否登录
  },
  data() {
    return {
      dialogFormVisible : false,
      title : '',
    }
  },
  methods : {
    test1 (){//alert框
      Dialog.alert( 'alert框!');
    },
    test2 (){//消息框提示
      //success成功按钮 ,warning 警告按钮,danger危险按钮,info信息按钮
      Dialog.message( '消息框提示!' ,'success');
    },
    test3 (){//messageBOX框
      this.$data.dialogFormVisible = true;
      this.$data.title = '动态标题 - 新增';
    },
    test4 (){//loading
      var self = this;
      self.$store.state.stateObject.fullscreenLoading = true;
      setTimeout(function(){
        self.$store.state.stateObject.fullscreenLoading = false;
      },3000)
    },
    test5 (){//ajax--Form Data
      var param = {
        username : 'wer',
      }
      util.ajax(apiConfig.test.save,param, 'Form Data',(success)=>{
        Dialog.alert( success );
      })
    },
    test6 (){//ajax--application/json
      var param = {
        username : 'wer',
      }
      util.ajax(apiConfig.test.saveJson,param,(success)=>{
        Dialog.alert( success );
      })
    },
    test7 (){//ajax--application/json
      var load = new apiConfig.ajax();
      load.url = apiConfig.test.userLogin;
      load.param = {
        userName: "aaaa",
        userPwd : 'bbbb'
      };
      load.requestType = 'Form Data'//请求方式
      util.ajax(load, (success)=>{
        Dialog.alert( success );
      });
    },
  }
}
</script>
