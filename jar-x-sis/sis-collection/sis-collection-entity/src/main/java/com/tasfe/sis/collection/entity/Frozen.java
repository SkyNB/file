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
@Table(name="col_frozen")
public class Frozen {

    private Long id;

    private Long oId;

    private String frozenReason;

    private String frozenRemarke;

    private String loanNumber;

    private String frozenMan;

    private Timestamp frozenTime;

    private String thawMan;

    private String thawRemarks;

    private Timestamp thawTime;

}
