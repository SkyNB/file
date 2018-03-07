//import Ajax from '../vue/v_1.0.0/resources/js/ajax'

//所有请求路径参数配置入口baseURL: 'http://127.0.0.1:8082',
var dss = resss__;
var domain = res__;
var dom = ress__;
var dtss = ressss__;
export default {
    // ajax: Ajax,
    serverUrl: '192.168.33.36:8280',
    test: {
        save: domain + "/userstest/save",
        saveJson: domain + "/userstest/saveJson"
    },
    user: {
        add: domain + '/user/add',
        del: domain + '/user/del',
        upd: domain + '/user/upd',
        list: domain + '/user/list',
        query: domain + '/user/query',
        login: domain + "/user/login",
        logout: domain + "/user/logout",
        menu: domain + "/user/menu",
        userLogin: domain + "/note/user/userLogin",
        // 登录
        login: domain + '/auth/login',
        // 验证session是否有效
        validSession: domain + '/auth/session'
    },
    bill: {
        // 查询对账单列表
        list: domain + '/bill/list',
        // 确认账单
        confirmBill: domain + '/bill/confirmBill',
        // 账单导入
        importOrder: domain + '/bill/importBill',
        // 处理账单
        handleOrder: domain + '/bill/handleBill'
    },
    order: {
        // 查询账单列表
        orderList: domain + '/order/list',
        // 查询处理状态
        queryStatus: domain + '/order/queryTaskStatus',
        // 查询已处理的条数
        completeNum: domain + '/order/queryCompleteNumber',
        // 改变订单审核状态
        updateOrderStatus: domain + '/order/updateCheckStatus',
        // 全部确认被初步审核的单子
        confirmPlotOrder: domain + '/order/confirmAllPrePlotOrders',
        // 查询对账单列表
        list:'/order/orders',
        //
        orderlist: domain + '/order/orderItems',
        // 请求所有的区域
        allRegion: domain + '/order/allRegion',
        // 请求所有订单详情
        allOrderDetail: domain + '/order/allOrderDetail',
        // 请求订单详情
        queryOrderDetail: domain + '/order/queryOrder',
        // 请求借款人信息
        queryPersonDetail: domain + '/order/queryPersonDetail',
        // 更改订单状态
        updateStatus: domain + '/order/updateOrderStatus',
        // 内前置上传文件
        importInnerFile: domain + '/order/importInnerFile',
        // 外前置上传文件
        importOutFile: domain + '/order/importOutFile',
        // 外前置下载文件
        downloadOutFile: domain + '/order/downOutFile',
        // 内前置下载文件
        downloadInnerFile: domain + '/order/downInnerFile',
        // 获取操作列表
        getOperationCase: domain + '/order/allOperationCase',
        // 根据条件查询订单
        queryOrderByCondition: domain + '/order/queryOrderByCondition',
        // 分配订单
        getToBeAssignedOrder: domain + '/order/getToBeAssignedOrder',
        //获取延迟处理订单
        getDelayHandleOrders: domain + '/order/getDelayHandleOrders',
        //增加或修改备注
        addOrUpdateMark: domain + '/order/addOrUpdateMark',
        // 个人征信
        queryCredit: domain + '/order/queryCredit'
    },
    role: {
        add: domain + '/role/add',
        del: domain + '/role/del',
        upd: domain + '/role/upd',
        list: domain + '/role/list',
        query: domain + '/role/query',
        selectRole: domain + '/role/selectRole',
        listResourcesByRoleId:domain + '/role/listResourcesByRoleId'
    },
    resources: {
        add: domain + '/resources/add',
        del: domain + '/resources/del',
        upd: domain + '/resources/upd',
        list: domain + '/resources/list',
        query: domain + '/resources/query',
        listTree: domain + '/resources/listTree'
    },
    auth: {
        add: domain + '/auth/add',
        del: domain + '/auth/del',
        upd: domain + '/auth/upd',
        list: domain + '/auth/list',
        query: domain + '/auth/query'
    },
    account: {
        add: domain + '/account/add',
        del: domain + '/account/del',
        upd: domain + '/account/upd',
        list: domain + '/account/list',
        paging:domain + '/account/paging',
        query: domain + '/account/query',
        login: domain + '/account/login'
    },
    collection: {
        list: domain + '/collectionOrder/list'
    },
    contract: {
        get: dss + '/contract/get'
    },
    imageUrl: {
        id_card_url: dtss
    }
};