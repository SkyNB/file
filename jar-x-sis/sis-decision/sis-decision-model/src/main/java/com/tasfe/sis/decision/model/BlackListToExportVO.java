package com.tasfe.sis.decision.model;

import lombok.Data;

import java.util.Date;

/**
 * Created by dongruixi on 2017/12/6.
 */
@Data
public class BlackListToExportVO {
    //数据ID
    private Long id;

    //姓名
    private String name;

    //配对签名，用于导出
    private String matchSign;

    private Date updateTime;
}
