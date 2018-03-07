package com.tasfe.sis.order.model;

import lombok.Data;
import org.omg.CORBA.INTERNAL;

import java.util.Date;

/**
 * Created by hefusang on 2017/12/7.
 */
@Data
public class QueryOrderDTO {

    private String sessionCode;

    private Long userId;

    //1为调查员 2为审核人员
    private Integer roleId;

    /**
     * 当前第N页
     */
    private Integer pageSeq;

    /**
     * 每页数据量
     */
    private Integer pageSize;

    private String orderId;

    private String fromDate;

    private String endDate;

    //按照姓名查找
    private String name;

    //按照身份证号查找
    private String idCardNum;

    //按照手机号查找
    private String cellPhone;

    //按照状态查询
    private Integer status;

}
