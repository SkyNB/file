package com.tasfe.sis.user.api.dto;

import lombok.Data;

/**
 * Created by Lait on 2017/7/27.
 */
@Data
public class UserResisteDTO {

    // 基本信息
    private String name;
    private Integer age;


    //地址信息
    private String province;
    private String city;
    private String area;
    private String adress;
    private String code;
}
