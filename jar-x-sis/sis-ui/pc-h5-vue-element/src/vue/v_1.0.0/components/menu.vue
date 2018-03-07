<template>
    <aside id="sidebar_left" :class="stateObject.sidebarTheme.theme">
        <!--:uniqueOpened=true -->
        <el-menu v-if="defaultActive" :defaultActive="defaultActive" class="el-menu-vertical-demo" :abbr="defaultActive"
                 :theme="stateObject.sidebarTheme.theme">
            <template v-if="menuItems && menuItems.length > 0" v-for="(item,idx) in menuItems">
                <el-submenu :index="''+(idx+1)" :title="item.name">
                    <template slot="title"><i :class="item.icon"></i><span class="sidebar-title">{{ item.name }}</span>
                    </template>
                    <el-menu-item-group>
                        <template v-if="item.children" v-for="(subItem,idx2) in item.children">
                            <router-link :to="subItem.path" :title="subItem.name">
                                <el-menu-item :index="(idx+1)+'-'+(idx2+1)"><i :class="subItem.icon"></i><span
                                        class="sidebar-title">{{ subItem.name }}</span></el-menu-item>
                            </router-link>
                        </template>
                    </el-menu-item-group>
                </el-submenu>
            </template>
        </el-menu>
    </aside>
</template>
<script>
    import {mapState} from 'vuex'

    // 判断是否登录
    if (!Utils.getSession('token')) {
        window.location.href = ress__+ '/#/' + 'login'
    }
    //验证session是否有效
    var sessionCode = Utils.getSession('token');
    var item = {
        sessionCode: sessionCode
    };
    Utils.ajax(Api.user.validSession, item, (res) => {
        if (+res.status != 1) {
            window.location.href = ress__+ '/#/' + 'login'
            return;
        }
    });

    export default {
        name: 'menuDiv',
        data () {
            return {
                defaultActive: "",//默认选中
                menuItems: [],//菜单对角
                manageName: '', //
            }
        },
        created (){//对象被创建时

        },
        mounted () {
            var localName = location.href.split("#/");
            if (localName[1]) { //#/foo
                this.$data.localName = "/" + localName[1];
            }
            //请求动态获取menu菜单数据
            this.init();
        },
        computed: mapState({
            'stateObject': state => state.stateObject //简写。完整的:this.$store.state.stateObject访问
        }),
        methods: {
            handleOpen(key, keyPath) {
                console.log(key, keyPath);
            },
            handleClose(key, keyPath) {
                console.log(key, keyPath);
            },
            init (callback){
                var data;
                var username = Utils.getSession('username');
                var role_id = Utils.getSession('role');
                if(username === 'admin') {
                    data = {
                        "name": "农商后台管理",
                        "children": [{
                            "name": "公共配置",
                            "path": "#/",
                            "icon": "el-icon-setting",
                            "children": [{
                                "name": "账号管理",
                                "path": "/account",
                                "icon": "el-icon-document"
                            }]
                        }]
                    }
                } /*else {
                    data = {
                        "name": "农商后台管理",
                        "children": [{
                            "name": "公共配置",
                            "path": "#/",
                            "icon": "el-icon-setting",
                            "children": [{
                                "name": "个人征信",
                                "path": "/preremission",
                                "icon": "el-icon-document"
                            },{
                                "name": "内前置-导入导出",
                                "path": "/importAndExport",
                                "icon": "el-icon-document"
                            }]
                        }]
                    }
                }*/
                if(role_id == '1'){
                    data = {
                        "name": "农商后台管理",
                        "children": [{
                            "name": "账单管理",
                            "path": "#/",
                            "icon": "el-icon-setting",
                            "children": [{
                                "name": "待分配订单",
                                "path": "/investigating",
                                "icon": "el-icon-document"
                            }]
                        }]
                    }
                } else if(role_id == '2') {
                    data = {
                        "name": "农商后台管理",
                        "children": [{
                            "name": "账单管理",
                            "path": "#/",
                            "icon": "el-icon-setting",
                            "children": [{
                                "name": "所有订单",
                                "path": "/allOrdersDetail",
                                "icon": "el-icon-document"
                            },{
                                "name": "外前置-导入导出",
                                "path": "/out_importAndExport",
                                "icon": "el-icon-document"
                            }]
                        }]
                    }
                }
                var self = this;
                this.$store.state.stateObject.manageName = data.name;
                if (data.children) {
                    self.$data.menuItems = self.setDefPage(data.children, true);//设置默认页面
                }
                callback && callback();
            },
            setDefPage (d, flag){//设置默认页面
                var localName = location.href.split("#/");
                if (localName[1]) { //#/foo
                    this.$data.localName = "/" + localName[1];
                }
                d = d || this.$data.menuItems || [];
                if (d && d.length > 0) {
                    for (var i = 0; i < d.length; i++) {
                        var item = d[i].children;
                        item.index = (i + 1);
                        if (flag) {//第一次进入默认选中
                            var isFlag = false;
                            for (var j = 0; j < item.length; j++) {
                                item[j].index = item.index + "-" + (j + 1);
                                if (this.$data.localName == item[j].path) {
                                    isFlag = item[j].checked = true;
                                    this.$data.defaultActive = "" + item[j].index;
                                } else {
                                    item[j].checked = false;
                                }
                            }
                            d[i].muneIcon = isFlag ? true : false;
                        } else {//点击之后清除所有状态
                            for (var j = 0; j < item.length; j++) {
                                item[j].index = item.index + "-" + (j + 1);
                                item[j].checked = false;
                            }
                        }
                    }
                }
                if (!this.$data.defaultActive) {
                    this.$data.defaultActive = '1-0';//默认展开第一项
                }
                return d;
            }
        }
    }
</script>
<style scoped>

</style>