package com.tasfe.sis.order.service;

import com.tasfe.sis.order.entity.OrderOperationCase;

/**
 * Created by hefusang on 2017/8/10.
 */
public interface RefusalService {

    public OrderOperationCase selectByPrimaryKey(String key) throws Exception;

}
