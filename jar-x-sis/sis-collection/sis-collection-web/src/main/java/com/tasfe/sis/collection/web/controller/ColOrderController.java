package com.tasfe.sis.collection.web.controller;

import com.tasfe.sis.base.model.response.ResponseData;
import com.tasfe.sis.collection.entity.Order;
import com.tasfe.sis.collection.model.QueryOrderInfos;
import com.tasfe.sis.collection.service.ColOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hefusang on 2017/9/19.
 */
@RestController
@RequestMapping("collectionOrder")
public class ColOrderController {

    @Autowired
    private ColOrderService colOrderService;

    @RequestMapping(value = "/list", method = RequestMethod.POST, produces = "application/json")
    public ResponseData<List<Order>> getOrders(@RequestBody QueryOrderInfos queryOrderDto) throws Exception {
        ResponseData responseData = new ResponseData();
        Map<String, Object> resMap = colOrderService.queryOrder(queryOrderDto);
        responseData.setStatus("1");
        responseData.setError("00000000");
        responseData.setData(resMap);
        return responseData;
    }

}
