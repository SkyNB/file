package com.tasfe.sis.decision.model;

import lombok.Data;

/**
 * Created by dongruixi on 2017/12/6.
 */
@Data
public class BlackListVO {
    //数据ID
    private Long id;

    //身份证ID
    private String idCardNum;

    //姓名
    private String name;

    //配对签名，用于导出
    private String matchSign;

    private Long upTime;
}
