package com.tasfe.sis.order.model;

import lombok.Data;

/**
 * Created by hefusang on 2017/8/9.
 */
@Data
public class ConfirmBillDto {

    private String account;

    private String customerCode;

    private String sessionCode;

}
