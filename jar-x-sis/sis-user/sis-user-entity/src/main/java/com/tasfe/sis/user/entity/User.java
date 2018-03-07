package com.tasfe.sis.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by Lait on 2017/7/11.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="hlj_users")
public class User {

    @Id
    private Long id;

    private Long customerId;

    private String name;

    private String pwd;

    private String legalName;

    private Integer loginType;

    private Integer status;

    private Date ctime;

    private Date utime;

    private Integer permissionSet;



/*

    // 真实名称
    private String name;
    // 年龄
    private int age;
    // 性别
    private int sex;
    // 昵称或别名或第二名
    private String nick;
    // 身份证
    private String idcard;
    // 出生地/户籍所在地/第一地址/户口本所在地/
    private String adress;
    // 创建时间
    private Date ctime;
    // 更新时间
    private Date utime;
*/


}
