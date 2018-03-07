package com.tasfe.sis.order.service;


import com.tasfe.sis.order.entity.OrderCheckLog;

/**
 * Created by hefusang on 2017/8/8.
 */
public interface CheckLogService {

    public OrderCheckLog selectCheckLogByOrderId(String orderId) throws Exception;

}
