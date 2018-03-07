var path = require("path");
//var glob = require("glob");
var webpack = require("webpack");
var HtmlWebpackPlugin = require('html-webpack-plugin');
//css独立打包
var ExtractTextPlugin = require('extract-text-webpack-plugin');
//自动打开浏览器
var OpenBrowserPlugin = require('open-browser-webpack-plugin'); 
var CommonsChunkPlugin = webpack.optimize.CommonsChunkPlugin;
var CopyWebpackPlugin = require('copy-webpack-plugin');
//px转rem
var px2rem       = require('postcss-px2rem'); 
//自动加前缀如 -web-,-o-
var precss       = require('precss');
var autoprefixer = require('autoprefixer');  //自动加前缀如 -web-,-o-

var config = {
    devtool: '#cheap-module-eval-source-map',
    entry: {
        app: './src/main.js',
        'vendor': ['vue','vuex','vue-router','jquery','element-ui']
    },
    output: { //配置打包结果， path 定义了输出的文件夹，filename则定义了打包结果文件的名称
        path: path.join(__dirname,"./dist"), // 输出文件的保存路径
        filename: '[name].js', //只要再加上hash这个参数就可以了
        chunkFilename: "chunk/[name].[chunkHash:8].js"
    },
    resolve: {
        //自动扩展文件后缀名，意味着我们require模块可以省略不写后缀名
        extensions: ['.js', '.vue', '.json'],
        alias: {
            //'vue$': 'vue/dist/vue.esm.js',    //不加会报runtime-only错误
            'vue': 'vue/dist/vue.js',    //不加会报runtime-only错误
            '@': path.join(__dirname,'./src') //掉用模板时间用到
        }
    },
    devServer: {
        hot: true,
        inline: true,//实时刷新
        //colors: true,  //终端中输出结果为彩色`
        host: "0.0.0.0",
        port: 3456,
        contentBase: './public',
        historyApiFallback: true,
        disableHostCheck: true,//新版的webpack-dev-server出于安全考虑，默认检查hostname，如果hostname不是配置内的，将中断访问。
        proxy: {//本地代理
            // '/userstest/*': {
            //     target: 'http://localhost:8280/',
            //     secure: false
            // },
            '/**': {
                target: 'http://127.0.0.1:8280',
                secure: false
            }
        }
    },
    module: {
        rules: [
            {
                test: /\.(js|vue)$/,
                loader: 'eslint-loader',
                enforce: 'pre',
                include: [path.join(__dirname,'./src')],
                options: {
                  formatter: require('eslint-friendly-formatter')
                }
            },
            {test: /\.vue$/,loader: 'vue-loader'},
            {test: /\.js$/,loader: 'babel-loader?presets=es2015',exclude: /node_modules/},
            {test: /\.css$|\.scss$/,loader: 'style-loader!css-loader!postcss-loader'},// SASS
            {test: /\.jpe?g$|\.gif$|\.png$/, loader: 'url-loader?limit=8192&name=image/[hash:8].[name].[ext]'},
            {test: /\.svg$|\.woff$|\.ttf$|\.eot$/, loader: 'url-loader?limit=8192&name=font/[hash:8].[name].[ext]'},
            {test: /\.json$/,loader: 'json-loader'}// JSON
        ]
    },
    plugins: [
        new webpack.LoaderOptionsPlugin({
            options: {
                postcss: function () {
                    //remUnit = 16 ， 16px = 1rem
                    //remPrecision = 8 ,保留8位小数
                    //autoprefixer 自动加前缀如 -web-,-o-
                    //return [precss, autoprefixer,px2rem({remUnit:16,remPrecision:8})];
                    //开发时取消
                    return [precss, autoprefixer];
                }
            }
        }),
        //将css成生文件，而非内联
        new ExtractTextPlugin("styles.css"),//css独立打包
        //多个 html共用一个js文件(chunk)
        new CommonsChunkPlugin({
            name: ['vendor','common'],
            minChunks: Infinity
        }),
        new webpack.HotModuleReplacementPlugin(),  //代码热替换
        //生成html文件
        new HtmlWebpackPlugin({
            filename: 'index.html',
            template: 'index.html',//启动页面
            inject: true//要把script插入到标签里
        }),
        new OpenBrowserPlugin({    //自动打开浏览器
            url: 'http://localhost:3456'
        }),
        //调用模块的别名
        new webpack.ProvidePlugin({
            $: "jquery",
            jQuery: "jquery",
            "window.jQuery": "jquery"
        }),
        new CopyWebpackPlugin(
            [{ from: './src/resouse.js', to: 'resouse.js'}]
        )
      ]
}
module.exports = config;
