package com.tasfe.sis.bank.model;

/**
 * Created by Lait on 2017/8/8.
 */
public enum TradeStatus {
    WITHDRAWAL_SUBMITTED("已提交银行！"),
    WITHDRAWAL_SUCCESS("转账成功！"),
    WITHDRAWAL_FAIL("转账失败！"),
    RETURN_TICKET("转账退票！"),
    VERIFY_FAILURE("签名验证失败！");

    private String descr;

    TradeStatus() {
    }

    TradeStatus(String descr) {
        this.descr = descr;
    }

    public String getDescr() {
        return descr;
    }
}
