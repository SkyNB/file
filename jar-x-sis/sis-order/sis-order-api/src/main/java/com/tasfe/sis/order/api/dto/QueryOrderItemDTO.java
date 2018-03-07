package com.tasfe.sis.order.api.dto;

import com.tasfe.sis.base.model.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public final class QueryOrderItemDTO extends BaseDto {
    /**
     * 对账日期
     */
    private String date;

    /**
     * 客户号
     */
    private String customerCode;

    /**
     * 起始行数（不包含）
     */
    private Integer startRow;
    /**
     * 当前第N页
     */
    private Integer pageSeq;

    /**
     * 每页数据量
     */
    private Integer pageSize;

    /**
     * 借款人姓名
     */
    private String renter;

    /**
     * 手机号码
     */
    private String cellPhone;

    /**
     * 身份证号
     */
    private String idCardNo;
}
