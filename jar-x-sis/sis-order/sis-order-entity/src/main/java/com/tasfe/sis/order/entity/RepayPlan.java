package com.tasfe.sis.order.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="repay_plan")
public class RepayPlan {

    @Id
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
