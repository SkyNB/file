package com.tasfe.sis.order.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Table;

/**
 * Created by hefusang on 2017/8/8.
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="order_attrs")
public class BillAttrs {

    //主键id
    private Long id;

    // 订单概要id
    private Long osid;

    //
    private String key;

    //
    private String val;
    // 属性描述
    private String descr;

}
