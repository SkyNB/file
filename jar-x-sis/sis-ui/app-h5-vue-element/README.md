```
## 简介
涉及的技术栈：
- MVVM框架：   [Vue](https://github.com/vuejs/vue)
- 路由管理：   [Vue-Router](https://github.com/vuejs/vue-router)
- 状态管理：   [Vuex](https://github.com/vuejs/vuex)
- 服务端通信： [Axios](https://github.com/mzabriskie/axios)
- 组件库：     [element-ui](http://element.eleme.io/#/zh-CN)
工具支持：
- 代码检测：   [ESLint](http://eslint.org/)
- 构建工具：   [Webpack](https://webpack.js.org/)
- 包管理工具： [Npm](https://www.npmjs.com/)
- Chrome 插件：[Vue-devtools](https://github.com/vuejs/vue-devtools)

## 快速开始
> 需安装 [Node.js](https://nodejs.org/en/), 版本 > 4.0.0
> npm install
启动 :
npm run start   开发环境
npm run prod    发布环境
```

```
项目结构如下：
|-----dist              (打包后生成的文件夹)  
|-----src
|	   |----commoncss   (公共css文件夹)
|      |----commonjs    (公共js文件夹)
|      |----components  (公共组件文件夹)
|      |----fonts       (字体库文件夹)
|      |----image       (图片文件夹)
|      |----router      (路由文件夹)
|      |----vue         (vue页面文件夹)
|      |----vuex        (全局状态文件夹)
|      |----App.vue
|      |----main.js     (项目总入口)
|      |----resouse.js  (请求路径) 
|-----.babelrc
|-----.eslintrc.js
|-----.postcssrc.js
|-----index.html
|-----package.json
|-----README.md
|-----webpack.config.js
|-----webpack.prod.config.js

```
使用：
1): vue2使用：https://cn.vuejs.org/
2): element使用： http://element.eleme.io/#/zh-CN

3): 新建一个页面(配置路由)如：
	1.在src/vue/下新建页面 XXX.vue
 	2.在src/router/router.js中配：
    	1)在 vueConfig 变量中加：
    		export const vueConfig = {
				vue : {
					test : {
						tree              : resolve => require.ensure([], () => resolve(require('@/vue/test/tree')), 'vue/test/tree'),
					}
				}
			}
		2)在Router下加： 
		    routes: [
			    {path: '/',component : vueConfig.login},//登陆
			    {
					path: '/',
					component : PageTransition,
					children: [
					    {path: 'tree',            component : vueConfig.vue.test.tree},  //Tree 树形控件
					]
			    }
			]

4): 添加全局JS和Css 如：
   在main.js中配：
   //js
   import Util from '@/commonjs/util.js'
   window.util = Util;
   //使用：uitl.isLogin();

   //css
   require('@/commoncss/mui.css');


5): 状态管理用法(vuex)：src/vuex/store.js配：
	let store = new Vuex.Store({
		state : {
			stateObject : {
				isShowTemp : false
		    }
		}

	页面使用 .vue 
	computed: mapState({
	    'stateObject' : state => state.stateObject //简写。完整的:this.$store.state.stateObject访问
	}),
	mounted (){
       this.$store.state.stateObject.isShowTemp = false; //代码中使用(js)
    },

    //template 中使用(html)
	<nav class="navbar navbar-default navbar-fixed-top" :class="stateObject.headerTheme.theme">

6): apiConfig公共请求库使用：
	var param = {
		username : 'wer',
	}
	//Form Data格式
	util.ajax(apiConfig.test.save,param, 'Form Data',(success)=>{
		Dialog.alert( success );
	})
	//JSON格式
	util.ajax(apiConfig.test.save,param,(success)=>{
		Dialog.alert( success );
	})
7): 处理JSON跨域问题(webpack.config.js):
	访问如：http://139.196.253.143:8801/agent/gateway/getData;
	var config = {
	    ...
	    devServer: {
	        ...
	        proxy: {//本地代理
	            '/agent/gateway*': {
	                target: 'http://139.196.253.143:8801',
	                secure: false
	            }
	        }
	    }
	    ...
	}
```






