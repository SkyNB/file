package com.tasfe.sis.order.api;

import com.tasfe.sis.base.exception.BizException;
import com.tasfe.sis.order.api.dto.*;
import com.tasfe.sis.order.model.vo.OrderDetailsVO;
import com.tasfe.sis.order.model.vo.BillVO;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 订单(对账单)服务.<br>
 * 外部接口,对外提供服务
 */
public interface OrderContract {

//     getOrders();

    Long newOrder(OrderItemDTO orderItemDTO) throws Exception;

    Long updateOrderDecision(OrderItemDTO orderItemDTO) throws Exception;

    Long updateOrderPhotos(Long orderId, String photoUrl0, String photoUrl1) throws Exception;

    /**
     * 获取日期后审批完成的订单的审批
     * @param date
     * @return
     * @throws Exception
     */
    List<OrderItemDTO> getApprovedOrderItems(Date date) throws Exception;

    List<OrderItemDTO> getLoanedOrderItems(Date date) throws Exception;

    List<RepaymentDTO> getRepayment(Date date) throws Exception;

    List<RepayPlanDTO> getRepayPlan(Long orderId) throws Exception;

    OrderItemDTO getOrder(String borrowNid) throws Exception;
    OrderItemDTO getOrder(Long orderId) throws Exception;

}
