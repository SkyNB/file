package com.tasfe.sis.collection.service;

import com.tasfe.sis.collection.model.QueryOrderInfos;

import java.util.List;
import java.util.Map;

/**
 * Created by hefusang on 2017/9/19.
 */
public interface ColOrderService {

    public Map<String, Object> queryOrder(QueryOrderInfos queryOrderInfosu) throws Exception;

}
