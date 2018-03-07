package com.tasfe.sis.order.service;

import com.tasfe.sis.order.model.ConfirmBillDto;
import com.tasfe.sis.order.model.QueryBillDto;
import com.tasfe.sis.order.model.vo.BillVO;

import java.util.List;

/**
 * Created by hefusang on 2017/8/10.
 */
public interface BillService {

    public List<BillVO> queryBill(QueryBillDto queryBillDto) throws Exception;

    public boolean confirmBill(ConfirmBillDto confirmBillDto) throws Exception;

    public void payment(String account) throws Exception;

}
