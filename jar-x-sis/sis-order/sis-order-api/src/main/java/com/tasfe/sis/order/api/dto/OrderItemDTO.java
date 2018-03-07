package com.tasfe.sis.order.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by dongruixi on 2017/11/8.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderItemDTO implements Serializable {
    private Long id;

    private String borrowNid;

    private String renter;

    private String idCardNum;

    private String cellphone;

    private Integer gender;

    private Date birthday;

    private String region;

    private String country;

    private Integer married;

    private Integer isLocal;

    private String spouse;

    private String spouseIdCardNum;

    private String census;

    private String zipcode;

    private String tel;

    private String homeAddress;

    private String homeZipcode;

    private String homeTel;

    private String company;

    private String educational;

    private String industry;

    private String officeAddress;

    private String officeZipcode;

    private String offieTel;

    private String photoUrl0;

    private String photoUrl1;

    private String agreementUrl;

    private Integer period;

    private BigDecimal amount;

    private BigDecimal rate;

    private String loanBankCard;

    private String repayBankCard;

    private String channel;

    private Integer checkStatus;

    private String bankCode;

    private String decisionResult;

    private String riskScore;
    
    private String extScore;

    private Date loanDay;

    private Date utime;

    private Date ctime;

    private String loanUsage;

    private Long allocateUserId;
    
    //订单状态 open 打开  close 关闭
    private String contractStatus;
}
