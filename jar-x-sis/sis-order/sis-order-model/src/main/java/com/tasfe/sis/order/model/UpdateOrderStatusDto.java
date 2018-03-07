package com.tasfe.sis.order.model;

import com.tasfe.sis.bank.model.BankSettings;
import lombok.Data;

/**
 * Created by hefusang on 2017/8/9.
 */
@Data
public class UpdateOrderStatusDto {

    private String id;

    private String status;

    private String refusalId;

    private String sessionCode;

    private Integer userId;

    private BankSettings bankSettings;

    private String account;
}
