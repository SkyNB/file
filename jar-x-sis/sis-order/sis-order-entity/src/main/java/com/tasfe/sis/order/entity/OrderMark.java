package com.tasfe.sis.order.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by hefusang on 2017/12/25.
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="order_mark")
public class OrderMark {

    @Id
    private Long id;

    private Long orderId;

    private String description;

}
