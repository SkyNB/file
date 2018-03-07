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
@Table(name="col_abando")
public class Abando {

    private Long id;

    private Long oId;

    private String abandonRemarks;

    private String loanNumber;

    private String abandonOperator;

    private Timestamp abandonTime;

    private String cancleAbandonPerson;

    private String cancleAbandonRemarks;

    private Timestamp cancleAbandonTime;

}
