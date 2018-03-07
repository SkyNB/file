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
@Table(name="col_collection")
public class Collection {

    private Long id;

    private Long cId;

    private String collectionType;

    private String currentCollector;

    private String previewCollector;

    private Timestamp cTime;

    private String callLog;

}