package com.tasfe.sis.collection.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Created by hefusang on 2017/9/18.
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="col_control_code")
public class ControlCode {

    private Long id;

    private Long oId;

    private String controlCode;

    private Long controlType;

    private String remarks;

    private String firstControlCode;

    private String secondControlCode;

    private String operator;

    private Timestamp codeTime;

    private String dialingOperator;

    private Timestamp dialingTime;

    private String dialRemarks;

    private String controlResult;

}