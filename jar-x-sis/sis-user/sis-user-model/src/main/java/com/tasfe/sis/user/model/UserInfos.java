package com.tasfe.sis.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * Created by Lait on 2017/7/27.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserInfos {
    private Integer id;

    private Integer customerid;

    private String name;

    private String pwd;

    private String legalname;

    private Integer logintype;

    private Integer status;

    private Date createTime;

    private Date updateTime;
}
