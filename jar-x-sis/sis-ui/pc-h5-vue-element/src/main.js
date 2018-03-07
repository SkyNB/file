import App from './App'
import Vue from 'vue'
import Vuex from 'vuex'
import Router from './router/router'
import Store from './vuex/store'

import Dialog from '@/vue/v_1.0.0/resources/js/dialog.js'
import Utils from '@/vue/v_1.0.0/resources/js/util.js'
import Api from '@/api/api_1.0.0.js'
import Ajax from '@/vue/v_1.0.0/resources/js/ajax'

//element-ui
//http://element.eleme.io/#/zh-CN/component/installation
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-default/index.css';
Vue.use(ElementUI);
require('@/vue/v_1.0.0/resources/css/common.css');


//commonjs
window.Utils = Utils;
window.Api = Api;
window.Ajax = Ajax;
window.Dialog = new Dialog();

// 引入 ECharts 主模块
// window.echarts = require('echarts/lib/echarts');

// require('echarts/lib/chart/bar');// 引入柱状图
// require('echarts/lib/chart/line');// 引入折线图
// require('echarts/lib/chart/pie');// 引入饼图
// // 引入提示框和标题组件
// require('echarts/lib/component/tooltip');
// require('echarts/lib/component/title');
// require('echarts/lib/component/legend');

Vue.use(Vuex);
window.vm = new Vue({
	el: '#app',
    router : Router,
	store : Store,
	template: '<App/>',
	components: { App }
});


/*Vue.prototype.$http = Axios.create({
    baseURL: Api.domain,
    timeout: 20000
})

let loadingInstance = null

Vue.prototype.$http.interceptors.request.use(res => {
    loadingInstance = Vue.prototype.$loading({
        fullscreen: true,
        lock: true,
        text: '努力加载中...'
    })
    return res
}, error => {
    return Promise.reject(error)
})

Vue.prototype.$http.interceptors.response.use(res => {
    loadingInstance.close()
    if (+res.data.status === 0 && res.data.error === '42000205') {
        router.replace('/')
    }
    return res
}, error => {
    loadingInstance.close()
    Vue.prototype.$message({
        showClose: true,
        message: '网络错误，请稍后重试',
        type: 'error'
    })
    return Promise.reject(error)
});*/

// router.beforeEach((to, from, next) => {

//   	next();   // 进行下一个钩子函数
// })