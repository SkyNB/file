package com.tasfe.sis.application.model.dto;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * Created by Rich on 2017/11/07.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderDTO implements Serializable {

    private Long id;

    //系统处理序列好
    private String sn;

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
    
    private String score;

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

    private String bankCode;

    private String loanUsage;
}
