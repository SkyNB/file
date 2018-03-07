package com.tasfe.sis.order.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by hefusang on 2017/12/7.
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="contract")
public class Contract {

    @Id
    private Long id;

    private String personId;

    private String name;

    private String phone;

    private String relation;

    private String isChief;

}
