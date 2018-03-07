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
@Table(name="col_customer")
public class Customer {

    private Long id;

    private String cutomerName;

    private String cardId;

    private String registerPhone;

    private Timestamp registerTime;

    private String phoneModel;

    private String adress;

}
