package com.tasfe.sis.order.model;

import lombok.Data;

/**
 * Created by hefusang on 2017/11/29.
 */
@Data
public class OrderDetailDTO {

    /**
     * 当前第N页
     */
    private Integer pageSeq;

    /**
     * 每页数据量
     */
    private Integer pageSize;

}
