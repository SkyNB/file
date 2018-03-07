package com.tasfe.sis.user.model.vo;

import lombok.Data;

/**
 * Created by Lait on 2017/7/27.
 * view object
 */
@Data
public class UserRegisteVO {

    // 基本信息
    private String name;
    private Integer age;
    private Integer sex;
    private String cellPhone;



    //地址信息
    private String province;
    private String city;
    private String area;
    private String adress;
    private String code;



    // 账户信息
    private Long uid;
    private String account;
    private String pwd;
}
