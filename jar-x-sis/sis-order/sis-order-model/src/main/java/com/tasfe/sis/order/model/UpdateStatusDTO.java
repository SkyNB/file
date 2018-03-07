package com.tasfe.sis.order.model;

import lombok.Data;

/**
 * Created by hefusang on 2017/12/7.
 */
@Data
public class UpdateStatusDTO {

    private String sessionCode;

    private Long orderId;

    private String status;

    private Long userId;

}
