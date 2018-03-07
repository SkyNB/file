package com.tasfe.sis.decision.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 请来模块开发人员来认领
 * Created by Rich on 2017/12/6.
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="de_decision_script")
public class DecisionScript {
    @Id
    Long id;

    /**
     * 决策名称
     * Field:version nvarchar(20)
     */
    private String name;

    /**
     * 版本
     * Field:version varchar(10)
     */
    private String version;

    /**
     * 脚本
     * Field:script ntext
     */
    private String script;

    /**
     * 脚本
     * Field:is_current(1:True,0:False)
     */
    private Integer isCurrent;
}
