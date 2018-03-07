var path = require("path");
//var glob = require("glob");
var webpack = require("webpack");
var HtmlWebpackPlugin = require('html-webpack-plugin');
//css独立打包
var ExtractTextPlugin = require('extract-text-webpack-plugin');
var CommonsChunkPlugin = webpack.optimize.CommonsChunkPlugin;
var CopyWebpackPlugin = require('copy-webpack-plugin');
//自动打开浏览器 
//px转rem
var px2rem       = require('postcss-px2rem'); 
//自动加前缀如 -web-,-o-
var precss       = require('precss');
var autoprefixer = require('autoprefixer');  //自动加前缀如 -web-,-o-

var extractCSS = new ExtractTextPlugin('css/[name].css?[contenthash]');
var OptimizeCSSPlugin = require('optimize-css-assets-webpack-plugin');
var config = {
    devtool: '#source-map',
    entry: {
        app: './src/main.js',
        'vendor': ['vue','vuex','vue-router','jquery','element-ui'], //es6语法
    },
    output: { //配置打包结果， path 定义了输出的文件夹，filename则定义了打包结果文件的名称
        path: path.join(__dirname,"./dist"), // 输出文件的保存路径
        filename: 'js/[name].js', //只要再加上hash这个参数就可以了
        chunkFilename: "js/chunk/[name].[chunkHash:8].js"
    },
    resolve: {
        //自动扩展文件后缀名，意味着我们require模块可以省略不写后缀名
        extensions: ['.js', '.vue', '.json'],
        alias: {
            'vue': 'vue/dist/vue.js',    //不加会报runtime-only错误
            '@': path.join(__dirname,'./src') //掉用模板时间用到
        }
    },
    module: {
        rules: [
            {test: /\.vue$/,loader: 'vue-loader'},
            {test: /\.js$/,loader: 'babel-loader?presets=es2015',exclude: /node_modules/},
            {
                test: /\.scss|\.css$/,
                use: ExtractTextPlugin.extract({
                    fallback: 'style-loader?minimize',
                    use: ['css-loader?minimize','sass-loader?minimize','postcss-loader?minimize']
                })
            },
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
                    return [precss, autoprefixer];
                }
            }
        }),
        new webpack.optimize.UglifyJsPlugin({// 压缩代码
            minimize: true,
            output: {
                comments: false,  // remove all comments
            },
            compress: {
                warnings: false
            }
            //,sourceMap: true
        }),
        extractCSS,
        new OptimizeCSSPlugin(),
        //多个 html共用一个js文件(chunk)
        new CommonsChunkPlugin({
            name: ['vendor','common'],
            minChunks: Infinity
        }),
        //生成html文件
        new HtmlWebpackPlugin({
            filename: 'index.html',
            template: 'index.html',//启动页面
            inject: true//要把script插入到标签里
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
