package com.tasfe.sis.order.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by hefusang on 2017/12/7.
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="person_info")
public class PersonInfo {

    @Id
    private Long id;

    //借款人姓名
    private String renter;

    //手机号
    private String cellphone;

    //身份证号
    private String idCardNum;

    //1:男，2:女
    private Integer gender;//男的1  女的0

    //出生年月日
    private Date birthday;

    //省/直辖市
    private String region;

    //县/区
    private String country;

    //1:已婚，0:未婚
    private Integer married;

    //1:本地，0:非本地
    private Integer isLocal;

    //配偶姓名
    private String spouse;

    //配偶身份证ID
    private String spouseIdCardNum;

    //户籍
    private  String census;

    //邮政编码（常用联系方式）
    private String zipcode;

    //联系电话（常用联系方式）
    private String tel;

    //家庭地址
    private String homeAddress;

    //邮政编码（家庭）
    private String homeZipCode;

    //联系电话（家庭）
    private String homeTel;

    //所在公司
    private String company;

    //学历
    private String educational;//

    //所在行业
    private String industry;

    //工作地址
    private String officeAddress;

    //邮政编码（公司）
    private String officeZipCode;

    //联系电话（公司）
    private String offieTel;

    //正面身份证照片地址(ftp)
    private String photoURL0;

    //反面身份证照片地址(ftp)
    private String photoURL1;

}
