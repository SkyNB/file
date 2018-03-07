package com.tasfe.sis.order.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by hefusang on 2017/8/8.
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="hlj_check_log")
public class OrderCheckLog {
    //主键id
    @Id
    private Long id;

    //操作id
    private String operationId;

    //
    private Long userId;

    //订单id
    private Long orderId;

    //创建时间
    private Date ctime;

    //更改时间
    private Date utime;
}
