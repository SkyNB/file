package com.tasfe.sis.application.web.controller;

import com.tasfe.sis.application.model.dto.OrderDTO;
import com.tasfe.sis.application.service.ApplicationService;
import com.tasfe.sis.base.model.response.ResponseData;
import com.tasfe.sis.order.api.ApplicationAssignmentContract;
import com.tasfe.sis.order.api.OrderContract;
import com.tasfe.sis.order.api.dto.OrderItemDTO;
import com.tasfe.sis.order.service.ApplicationAssignmentService;
import com.tasfe.sis.order.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by dongruixi on 2017/11/7.
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    private static Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    ApplicationService applicationService;

    @Resource
    OrderContract orderContract;

    @Resource
    ApplicationAssignmentContract applicationAssignmentContract;

    @RequestMapping(value = "/new", method = RequestMethod.POST, produces = "application/json",consumes="application/json;charset=utf-8")
    public ResponseData<String> newOrder(@RequestBody OrderDTO orderDTO) throws Exception {
        ResponseData responseData = new ResponseData();
        try{
            String sn = applicationService.newOrder(orderDTO);
            responseData.setData(sn);
            return responseData;
        }
        catch (Exception ex){
            LOGGER.error("ERR",ex);
        }
        responseData.setError("ERR");
        return responseData;

    }

    @RequestMapping(value = "/pushUser", method = RequestMethod.GET)
    public ResponseData<String> pushUserOrder(@RequestParam Long accountId,@RequestParam String borrowNid) throws Exception{
        OrderItemDTO dto = orderContract.getOrder(borrowNid);
        applicationAssignmentContract.pushUserOrder(accountId,dto);

        return new ResponseData<>().setCode("0").setData("");
    }
}
