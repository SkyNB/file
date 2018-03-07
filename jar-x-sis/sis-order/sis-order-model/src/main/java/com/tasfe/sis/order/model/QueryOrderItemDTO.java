package com.tasfe.sis.order.model;

import com.tasfe.sis.bank.model.BankSettings;
import lombok.Data;

/**
 * Created by hefusang on 2017/8/9.
 */
@Data
public class QueryOrderItemDTO {

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

    /**
     * 账单号
     */
    private String account;

    /**
     * 统计是否可以放款（0或者为空代表所有数据、1代表能放款数据、2代表拒绝放款数据）
     */
    private String canLoan;

    private String sessionCode;

    /**
     *
     */
    private Long userId;


    private BankSettings bankSettings;

    //区域
    private String region;

    //年龄
    private String age;


}
