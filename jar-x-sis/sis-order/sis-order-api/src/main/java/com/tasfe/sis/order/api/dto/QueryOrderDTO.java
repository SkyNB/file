package com.tasfe.sis.order.api.dto;

import com.tasfe.sis.base.model.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public final class QueryOrderDTO extends BaseDto {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 客户号
     */
    private String customerCode;
    /**
     * 开始时间
     */
    private String dateFrom;

    private String confirmDate;

    /**
     * 0 未确认； 1 已确认
     */
    private Integer confirmStatus;
    /**
     * 结束时间
     */
    private String dateTo;
    /**
     * 状态
     */
    private String statusId;

}
