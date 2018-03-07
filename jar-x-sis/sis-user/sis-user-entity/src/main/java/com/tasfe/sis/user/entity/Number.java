package com.tasfe.sis.user.entity;

import lombok.Data;

import javax.persistence.Id;

/**
 * 号码
 * Created by Lait on 2017/8/28.
 */
@Data
public class Number {
    @Id
    private Long id;
    //关联实体id(用户/公司/单位等)
    private Long rid;
    // 手机号0，电话1
    private Integer typ;
    // 号码
    private String number;
    // 号码所在市
    private String city;
}
