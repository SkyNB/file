package com.tasfe.sis.order.entity;

import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 请来模块开发人员来认领
 * Created by Rich on 2017/7/27.
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="credit_account")
public class CreditAccount {
    @Id
    private Long id;

    /**
     * 人行账号
     * Field:id_card_num
     */
    private String account;

    /**
     * 密码
     * Field:name
     */
    private String password;
    
    /**
     * 限制次数
     * Field:match_sign
     */
    private Integer limitNum;
    /**
     * 已使用
     * Field:match_sign
     */
    private Integer alUse;
    /**
     * 状态
     * Field:match_sign
     */
    private Integer status;
    /**
     * 更新时间
     * Field:up_time
     */
    private Date utime;
}
