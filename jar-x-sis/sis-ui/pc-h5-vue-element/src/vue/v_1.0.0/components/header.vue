<template>
  <nav class="navbar navbar-default navbar-fixed-top" :class="stateObject.headerTheme.theme">
    <div class="navbar-left dark" :class="stateObject.headerTheme.theme">
      <el-col :span="18" class="navbar_parent"><span class="navbar-brand text-uppercase">{{stateObject.manageName}}</span></el-col>
      <el-col :span="6"  class="navbar_parent sildmenu_icon"><i id="sildmenu_icon" @click="toggleMenu"  class="el-icon-d-arrow-left"></i></el-col>
    </div>
    <div class="navbar-logo">
        <img src="./../resources/img/bank_logo.png" height=60/>
        <span style="color:block">和林县联社</span>
    </div>
    <div class="navbar-right">
      <div class="navbar-right_img"><img src="./../resources/img/header.jpg" alt="avatar" width="70" /></div>
      <div float:right style="width: 280px;">
        <span>角色：</span><span>{{role_name}}</span><br/>
        <span>姓名：</span><span>{{name}}</span>
        <a href="javascript:void(0)" @click="toDropdownMenu"><i class="el-icon-arrow-down"></i></a>
      </div>

      </div>
  </nav>
</template>

<script>
import { mapState } from 'vuex'
export default {
  name: 'headerDiv',
  data () {
    return {
        role_name:'',
        name:''
    }
  },
    created (){
      var me = this;
      var role_id = Utils.getSession('role');
      me.name = Utils.getSession('username');
      me.role_name = name;
      if(role_id == '1') {
          me.role_name = '调查员';
      } else if(role_id == '2') {
          me.role_name = '审核人员';
      }
    },
  mounted () {
    window.VM__ = this;
  },
  computed: mapState({
    'stateObject' : state => state.stateObject //简写。完整的:this.$store.state.stateObject访问
  }),
  methods: {
    toggleMenu ( e ){//开、关左侧菜单
      this.$store.state.stateObject.isShowSort = !this.$store.state.stateObject.isShowSort;
    },
    toDropdownMenu ( e ){
      Dialog.alert("您确定要退出登陆吗？",{
        confirm : true,
        callback : function( txt ){
          if( txt === 'confirm'){//点击确定按钮
          window.location.href = '#/';
          }
        }
      })
    }
  }
}
</script>
<style scoped>
	
</style>