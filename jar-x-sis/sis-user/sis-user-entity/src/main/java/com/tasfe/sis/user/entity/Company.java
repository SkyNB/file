package com.tasfe.sis.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Table;

/**
 * Created by Lait on 2017/7/12.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="company")
public class Company {
    private Long id;

    // 用户id
    private Long uid;

    // 公司名称
    private String name;

    // 手机
    private String cellphone;

    // 电话
    private String telephone;

    // 法人
    private String legalName;

    // 营业执照
    private String licence;

    // 公司性质或类型（个人0/股份1/外企3）
    private Integer type;
}
