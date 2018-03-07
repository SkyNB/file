package com.tasfe.sis.order.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by hefusang on 2017/11/7.
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="region")
public class Region {

    //主键id
    @Id
    private Long id;

    private String name;

}