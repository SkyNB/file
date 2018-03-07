package com.tasfe.sis.decision.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 请来模块开发人员来认领
 * Created by Rich on 2017/7/27.
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="de_black_list")
public class BlackList {
    @Id
    private Long id;

    /**
     * 身份证号
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
