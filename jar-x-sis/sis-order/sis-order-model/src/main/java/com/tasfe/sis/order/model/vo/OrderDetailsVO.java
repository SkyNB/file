package com.tasfe.sis.order.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Lait on 2017/7/27.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderDetailsVO {

    private Long id;
    // 请写注释
    private Date batchDate;

    private String userId;

    private String borrowNid;

    private String renter;

    private String idcardno;

    private String cellphone;

    private String homeadr;

    private BigDecimal iamount;

    private Date startdate;

    private Date expiredate;

    private String debitaccount;

    private String loadaccount;

    private String married;

    private Integer periodDays;

    private String signId;

    private String agreement;

    private String billNo;

    private Integer status;

    private Date createTime;

    private Date updateTime;
}
