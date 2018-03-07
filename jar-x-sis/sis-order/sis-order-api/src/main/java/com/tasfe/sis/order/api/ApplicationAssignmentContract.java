package com.tasfe.sis.order.api;

import com.tasfe.sis.order.api.dto.OrderItemDTO;

/**
 * Created by dongruixi on 2017/12/16.
 */
public interface ApplicationAssignmentContract {

    //增加总待处理订单队列
    Boolean pushOrder(OrderItemDTO orderItemDTO);

    //增加调查员待处理订单队列
    Boolean pushUserOrder(Long accountId,OrderItemDTO orderItemDTO);

    //初始化队列，从数据库获取待处理订单，并放入队列中。
    Boolean initQueue() throws Exception;
}
