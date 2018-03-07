package com.tasfe.sis.order.entity;

import com.tasfe.framework.crud.api.annotation.Sharding;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 请来模块开发人员来认领
 * Created by Lait on 2017/7/27.
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="bi_order_summary")
public class Bill {

    //主键id
    @Id
    private Long id;

    //日期
    private Date batchDate;

    //确认状态
    private Integer confirmStatus;

    //总金额
    private BigDecimal totalAmount;

    //订单数量
    private Long orderNum;

    //确认人id
    private Long confirmUser;

    //银行客户号
    private String customerCode;

    //创建时间
    private Date ctime;

    //更改时间
    private Date utime;

    //来源
    private String orderSource;

    //放款状态
    private String loanStatus;

    //是否处于关闭状态
    private String lock;

    //账单编号
    private String billNo;

}