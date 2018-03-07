package com.tasfe.sis.collection.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Created by hefusang on 2017/9/18.
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="col_order")
public class Order {

    @Id
    private Long id;

    private Long customerId;

    private String easiness;

    private String loanNumber;

    private String billAmount;

    private String borrowCycle;

    private String amountCredit;

    private Timestamp taskTime;

    private Timestamp loanTime;

    private Timestamp repayTime;

    private Timestamp commitmentTime;

    private String orderStatus;

    private String remarks;

    private String label;

    private String contactResult;

    private Timestamp lastTime;

}