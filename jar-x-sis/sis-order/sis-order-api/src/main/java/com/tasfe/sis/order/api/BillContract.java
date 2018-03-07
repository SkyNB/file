package com.tasfe.sis.order.api;

/**
 * Created by dongruixi on 2017/11/8.
 */
public interface BillContract {
    Long getBillId(String sn) throws Exception;
    String getBillOpening(String bankCode,String orderSource) throws Exception;
}
