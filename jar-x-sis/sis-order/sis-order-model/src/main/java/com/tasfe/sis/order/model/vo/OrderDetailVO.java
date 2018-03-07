package com.tasfe.sis.order.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderDetailVO {

    //订单号
    private String borrowNid;
    //借款人
    private String renter;

    //身份证
    private String idCardNum;

    //手机号
    private String cellphone;

    //性别
    private Integer gender;

    //出生日
    private String birthday;

    //省份、直辖市
    private String region;

    //县、区
    private String country;

    //是否已经结婚
    private Integer married;

    //是否是本地（和林）
    private Integer isLocal;

    //配偶
    private String spouse;

    //配偶身份证号
    private String spouseIdCardNum;

    //户籍
    private String census;

    //邮编
    private String zipcode;

    //电话
    private String tel;

    //家庭地址
    private String homeAddress;

    //家庭邮编
    private String homeZipcode;

    //家庭电话
    private String homeTel;

    //公司
    private String company;

    //学历
    private String educational;

    //所在行业
    private String industry;

    //公司地址
    private String officeAddress;

    //公司邮编
    private String officeZipcode;

    //公司电话
    private String offieTel;

    //身份证正面照片地址
    private String photoUrl0;

    //身份证背面照片地址
    private String photoUrl1;

    //协议地址
    private String agreementUrl;

    //期数
    private Integer period;

    //金额
    private BigDecimal amount;

    //月利率
    private BigDecimal rate;

    //借款银行卡号
    private String loanBankCard;

    //还款银行卡号
    private String repayBankCard;

    //渠道
    private String channel;

    //审核状态
    /*
        进件  -99
        决策通过  -1
        决策拒绝 -2
        审核通过 1
        审核失败 0
     */
    private Integer checkStatus;

    //机构号
    private String bankCode;

    //决策结果
    private String decisionResult;

    //分控评分
    private String riskScore;

    //实际放款时间
    private Date loanDay;

    //合同号
    private String contractNo;

    //审批时间
    private Date checkTime;

    //更新时间
    private Date utime;

    //创建时间
    private Date ctime;

    //贷款用途
    private String loanUsage;

    //订单分配人员id
    private Long allocateUserId;
    
    private String description;
    
    //外部风控评分
    private String extScore;

}
