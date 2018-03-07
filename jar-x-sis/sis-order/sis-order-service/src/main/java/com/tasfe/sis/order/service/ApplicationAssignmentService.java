package com.tasfe.sis.order.service;

import com.tasfe.sis.order.api.dto.OrderItemDTO;
import org.springframework.core.annotation.Order;

/**
 * Created by dongruixi on 2017/12/16.
 */
public interface ApplicationAssignmentService {
    //获取订单，并将订单绑定给该人员
    OrderItemDTO getOrder(Long accountId) throws Exception;

    Boolean pushUserOrder(Long accountId,OrderItemDTO orderItemDTO);

}
