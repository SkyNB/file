package com.tasfe.sis.order.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by dongruixi on 2017/12/12.
 */
@Data
public class RepayPlanDTO implements Serializable {

    private  Long id;

    private String borrowNid;

    private Long orderId;

    //编号
    private String sn;

    //计划还款日
    private Date planDate;

    //计划还款金额
    private BigDecimal planAmount;

    //已还金额
    private BigDecimal amount;

    //已转逾期金额
    private BigDecimal overdueAmount;

    //更新时间
    private Date utime;

}
