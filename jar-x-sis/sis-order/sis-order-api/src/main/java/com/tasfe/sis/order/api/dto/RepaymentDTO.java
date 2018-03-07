package com.tasfe.sis.order.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by dongruixi on 2017/12/11.
 */
@Data
public class RepaymentDTO implements Serializable {
    private Long id;

    private String borrowNid;

    private Long orderId;

    //还款次数
    private int repayCount;

    //还款时间
    private Date repayDate;

    //还款状态
    private Integer repayStatus;

    //还款金额
    private BigDecimal repayAmount;

    //更新时间
    private Date utime;
}
