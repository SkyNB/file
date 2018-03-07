import Vue from 'vue'
import Router from 'vue-router'
import PageTransition from '@/vue/v_1.0.0/components/PageTransition' //默认

Vue.use(Router);

/*按需加载*/
export const vueConfig = {
    index: resolve => require.ensure([], () => resolve(require('@/vue/v_1.0.0/index')), 'vue/v_1.0.0/index'),//首页
    login: resolve => require.ensure([], () => resolve(require('@/vue/v_1.0.0/login')), 'vue/v_1.0.0/login'),//登陆
    tpl: {
        header_tpl: resolve => require.ensure([], () => resolve(require('@/vue/v_1.0.0/components/header')), 'vue/v_1.0.0/components/header'),//头部
        menu_tpl: resolve => require.ensure([], () => resolve(require('@/vue/v_1.0.0/components/menu')), 'vue/v_1.0.0/components/menu'),//左测菜单
        skin_tpl: resolve => require.ensure([], () => resolve(require('@/vue/v_1.0.0/components/skin')), 'vue/v_1.0.0/components/skin'),//右测
    },
    vue: {
        test: {
            tree: resolve => require.ensure([], () => resolve(require('@/vue/test/tree')), 'vue/test/tree'),
            table: resolve => require.ensure([], () => resolve(require('@/vue/test/table')), 'vue/test/table'),
            datetimePicker: resolve => require.ensure([], () => resolve(require('@/vue/test/datetimePicker')), 'vue/test/datetimePicker'),
            transfer: resolve => require.ensure([], () => resolve(require('@/vue/test/transfer')), 'vue/test/transfer'),
            test: resolve => require.ensure([], () => resolve(require('@/vue/test/test')), 'vue/test/test')
        },
        sis: {
            bill: resolve => require.ensure([], () => resolve(require('@/vue/v_1.0.0/modules/bill/index')), 'vue/v_1.0.0/modules/bill/index'),
            allOrdersDetail: resolve => require.ensure([], () => resolve(require('@/vue/v_1.0.0/modules/allOrdersDetail/index')), 'vue/v_1.0.0/modules/allOrdersDetail/index'),
            order: resolve => require.ensure([], () => resolve(require('@/vue/v_1.0.0/modules/order/index')), 'vue/v_1.0.0/modules/order/index'),
            role: resolve => require.ensure([], () => resolve(require('@/vue/v_1.0.0/modules/role/index')), 'vue/v_1.0.0/modules/role/index'),
            resources: resolve => require.ensure([], () => resolve(require('@/vue/v_1.0.0/modules/resources/index')), 'vue/v_1.0.0/modules/resources/index'),
            user: resolve => require.ensure([], () => resolve(require('@/vue/v_1.0.0/modules/user/index')), 'vue/v_1.0.0/modules/user/index'),
            account: resolve => require.ensure([], () => resolve(require('@/vue/v_1.0.0/modules/account/index')), 'vue/v_1.0.0/modules/account/index'),
            auth: resolve => require.ensure([], () => resolve(require('@/vue/v_1.0.0/modules/auth/index')), 'vue/v_1.0.0/modules/auth/index'),
            collection: resolve => require.ensure([], () => resolve(require('@/vue/v_1.0.0/modules/collection/index')), 'vue/v_1.0.0/modules/collection/index'),
            preremission: resolve => require.ensure([], () => resolve(require('@/vue/v_1.0.0/modules/preremission/index')), 'vue/v_1.0.0/modules/preremission/index'),
            authorization: resolve => require.ensure([], () => resolve(require('@/vue/v_1.0.0/modules/authorization/index')), 'vue/v_1.0.0/modules/authorization/index'),
            sync_credit: resolve => require.ensure([], () => resolve(require('@/vue/v_1.0.0/modules/sync_credit/index')), 'vue/v_1.0.0/modules/sync_credit/index'),
            black_list: resolve => require.ensure([], () => resolve(require('@/vue/v_1.0.0/modules/black_list/index')), 'vue/v_1.0.0/modules/black_list/index'),
            order_list: resolve => require.ensure([], () => resolve(require('@/vue/v_1.0.0/modules/order_list/index')), 'vue/v_1.0.0/modules/order_list/index'),
            tel_check: resolve => require.ensure([], () => resolve(require('@/vue/v_1.0.0/modules/tel_check/index')), 'vue/v_1.0.0/modules/tel_check/index'),
            importAndExport: resolve => require.ensure([], () => resolve(require('@/vue/v_1.0.0/modules/importAndExport/index')), 'vue/v_1.0.0/modules/importAndExport/index'),
            out_importAndExport: resolve => require.ensure([], () => resolve(require('@/vue/v_1.0.0/modules/out_importAndExport/index')), 'vue/v_1.0.0/modules/out_importAndExport/index'),
            investigating: resolve => require.ensure([], () => resolve(require('@/vue/v_1.0.0/modules/investigating/index')), 'vue/v_1.0.0/modules/investigating/index'),
            credit_account_manager: resolve => require.ensure([], () => resolve(require('@/vue/v_1.0.0/modules/credit_account_manager/index')), 'vue/v_1.0.0/modules/credit_account_manager/index')
        }
    }
}
//多渠道加载不同模板
// var aaa = window.location.href;
// function fn__( obj ){
// 	var aa = aaa.split("component=")[1]
// 	return obj[aa];
// }
export default new Router({
    routes: [
        {
            path: '/',
            component: PageTransition,
            children: [
                //登陆
                {path: '', component: vueConfig.login},
                //登陆
                {path: 'login', component: vueConfig.login},
                //首页
                {path: 'index', component: vueConfig.index},
                //Tree 树形控件
                {path: 'tree', component: vueConfig.vue.test.tree},
                //Table 表格
                {path: 'table', component: vueConfig.vue.test.table},
                //日期时间选择器
                {path: 'datetimePicker', component: vueConfig.vue.test.datetimePicker},
                //穿梭框
                {path: 'transfer', component: vueConfig.vue.test.transfer},
                {path: 'test', component: vueConfig.vue.test.test},

                {path: 'bill', component: vueConfig.vue.sis.bill},
                {path: 'allOrdersDetail', component: vueConfig.vue.sis.allOrdersDetail},
                {path: 'order', component: vueConfig.vue.sis.order},
                {path: 'role', component: vueConfig.vue.sis.role},
                {path: 'resources', component: vueConfig.vue.sis.resources},
                {path: 'user', component: vueConfig.vue.sis.user},
                {path: 'account', component: vueConfig.vue.sis.account},
                {path: 'auth', component: vueConfig.vue.sis.auth},
                {path: 'collection', component: vueConfig.vue.sis.collection},
                {path: 'preremission', component: vueConfig.vue.sis.preremission},
                {path: 'authorization', component: vueConfig.vue.sis.authorization},
                {path: 'sync_credit', component: vueConfig.vue.sis.sync_credit},
                {path: 'black_list', component: vueConfig.vue.sis.black_list},
                {path: 'order_list', component: vueConfig.vue.sis.order_list},
                {path: 'tel_check', component: vueConfig.vue.sis.tel_check},
                {path: 'importAndExport', component: vueConfig.vue.sis.importAndExport},
                {path: 'out_importAndExport', component: vueConfig.vue.sis.out_importAndExport},
                {path: 'investigating', component: vueConfig.vue.sis.investigating},
                {path: 'credit_account_manager', component: vueConfig.vue.sis.credit_account_manager}
            ]
        }
    ]
})
