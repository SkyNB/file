package com.tasfe.sis.order.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 授权用户
 * Created by Rich on 2017/7/27.
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="de_personal_credit")
public class PersonalCredit {
    @Id
    private Long id;

    /**
     *身份证ID
     * Field:id_card_num
     */

    private String idCardNum;

    /**
     * 姓名
     * Field:name
     */
    private String name;
    
    /**
     * 配对签名
     * Field:match_sign
     */
    private String matchSign;
    /**
     * 更新时间
     * Field:up_time
     */
    private Date updateTime;


}
