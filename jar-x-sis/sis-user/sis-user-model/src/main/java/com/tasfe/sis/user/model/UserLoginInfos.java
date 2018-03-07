package com.tasfe.sis.user.model;

import lombok.Data;

/**
 * Created by Lait on 2017/7/28.
 */
@Data
public class UserLoginInfos {
    /**
     * 客户ID/客户号 对应Table-customer code
     */
    private String customerCode;
    /**
     * 登录名
     */
    private String loginName;
    /**
     * 密码
     */
    private String loginPwd;

    /**
     *
     */
    private String sessionCode;
}
