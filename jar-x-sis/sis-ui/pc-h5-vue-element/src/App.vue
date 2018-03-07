<script>
import { mapState } from 'vuex'
import { vueConfig } from '@/router/router'
export default {
  data () {
    return {
        isLogin : false
    }
  },
  components : {
    'login' : vueConfig.login,
    'header_tpl' : vueConfig.tpl.header_tpl,
    'menu_tpl'   : vueConfig.tpl.menu_tpl,
    'skin_tpl'   : vueConfig.tpl.skin_tpl
  },
  computed: mapState({
    'stateObject' : state => state.stateObject //简写。完整的:this.$store.state.stateObject访问
  }),
  mounted (){
    window.VM__ = this;
    var token = sessionStorage.getItem('token');
    this.$data.isLogin = token ? true : false;
  },
  methods : {
    
  }
}
</script>
<template>
  <div>
    <div id="app" :class="stateObject.isShowSort ? '' : 'sb-l-m'">
      <template v-if="isLogin">
        <header_tpl></header_tpl>
        <menu_tpl   ref="menu_tpl"></menu_tpl>
        <skin_tpl  ></skin_tpl>
      </template>
      <section class="content" id="appBody" v-loading.fullscreen = "stateObject.fullscreenLoading" element-loading-text="拼命加载中">
        <router-view></router-view>
      </section>
    </div>
  </div>
</template>
