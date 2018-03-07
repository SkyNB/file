package com.tasfe.sis.decision.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 请来模块开发人员来认领
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



}
