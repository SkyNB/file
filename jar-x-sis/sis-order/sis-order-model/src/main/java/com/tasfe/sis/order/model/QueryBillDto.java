package com.tasfe.sis.order.model;

import lombok.Data;

/**
 * Created by hefusang on 2017/8/9.
 */
@Data
public class QueryBillDto {

    private String customerCode;

    private String dateFrom;

    private String confirmDate;

    private Integer confirmStatus;//0 未确认； 1 已确认

    private String dateTo;

    private String statusId;

    private String account;

    private String sessionCode;

    private String billNo;

}
