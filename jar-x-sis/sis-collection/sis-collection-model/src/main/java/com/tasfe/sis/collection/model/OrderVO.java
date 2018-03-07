package com.tasfe.sis.collection.model;

import lombok.Data;

import java.sql.Timestamp;

/**
 * Created by hefusang on 2017/9/20.
 */
@Data
public class OrderVO {

    private String easiness;

    private String loanNumber;

    private String billAmount;

    private String borrowCycle;

    private String amountCredit;

    private Timestamp taskTime;

    private Timestamp loanTime;

    private Timestamp repayTime;

    private Timestamp commitmentTime;

    private String orderStatus;

    private String remarks;

    private String label;

    private String contactResult;

    private Timestamp lastTime;

    //客户资料
    private String cutomerName;

    private String cardId;

    private String registerPhone;

    private Timestamp registerTime;

    private String phoneModel;

    private String adress;

}
