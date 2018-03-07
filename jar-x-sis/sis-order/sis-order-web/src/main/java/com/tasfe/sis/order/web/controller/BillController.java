package com.tasfe.sis.order.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tasfe.sis.base.model.response.ResponseData;
import com.tasfe.sis.order.model.ConfirmBillDto;
import com.tasfe.sis.order.model.QueryBillDto;
import com.tasfe.sis.order.model.vo.BillVO;
import com.tasfe.sis.order.service.BillService;
import com.tasfe.sis.user.service.SessionService;

/**
 * Created by Lait on 2017/8/8.
 */
@RestController
@RequestMapping("bill")
public class BillController {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private BillService billService;

    @RequestMapping(value = "/list", method = RequestMethod.POST, produces = "application/json")
    public ResponseData<List<BillVO>> getOrders(@RequestBody QueryBillDto queryBillDto) throws Exception {
        ResponseData responseData = new ResponseData();
        Long userId = null;
        try {
            userId = sessionService.validateSessionCode(queryBillDto.getSessionCode());
        } catch (Exception e) {
            e.printStackTrace();
            responseData.setMsg("sessioncode失效");
            responseData.setStatus("0");
            return responseData;
        }
        if (userId == null) {
             responseData.setMsg("sessionCode过期");
             responseData.setStatus("0");
             return responseData;
        }
        List<BillVO> bills = billService.queryBill(queryBillDto);
        responseData.setData(bills);
        responseData.setStatus("1");
        responseData.setError("00000000");
        return responseData;
    }

    @RequestMapping(value = "/confirmBill", method = RequestMethod.POST, produces = "application/json")
    public ResponseData<Boolean> confirmBill(@RequestBody ConfirmBillDto confirmBillDto) throws Exception {
        ResponseData responseData = new ResponseData();
        Long userId = sessionService.validateSessionCode(confirmBillDto.getSessionCode());
        if (userId == null) {
            responseData.setMsg("sessionCode失效");
            responseData.setStatus("0");
            return responseData;
        }
        try {
            boolean res = billService.confirmBill(confirmBillDto);
            if(res == true) {
                responseData.setStatus("1");
                responseData.setError("00000000");
            } else {
                responseData.setStatus("0");
                responseData.setMsg("此账单下还有待复审的订单，不能完成检查");
            }
            return responseData;
        } catch (Exception e) {
            e.printStackTrace();
            responseData.setStatus("0");
            return responseData.setMsg("更改失败");
        }
    }

    @RequestMapping(value = "/payment", method = RequestMethod.POST, produces = "application/json")
    public ResponseData<Boolean> payment(@RequestBody ConfirmBillDto confirmBillDto) throws Exception {
        ResponseData responseData = new ResponseData();
        billService.payment(confirmBillDto.getAccount());
        responseData.setError("00000000");
        responseData.setStatus("1");
        return responseData;
    }

}
