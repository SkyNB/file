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
@Table(name="repay_info")
public class RepayInfo {

    @Id
    private Long id;

    private String borrowNid;

    private Long orderId;

    //还款次数
    private Integer repayCount;

    //还款时间
    private Date repayDate;

    //还款状态  接口返回的就是“还款正常”
    private Integer repayStatus;

    //还款金额
    private BigDecimal repayAmount;

    //更新时间
    private Date utime;

}
