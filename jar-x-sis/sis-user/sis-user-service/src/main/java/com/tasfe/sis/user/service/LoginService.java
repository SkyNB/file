package com.tasfe.sis.user.service;

import com.tasfe.sis.base.model.response.ResponseData;
import com.tasfe.sis.user.model.vo.UserLoginVO;

/**
 * Created by Lait on 2017/7/31.
 */
public interface LoginService {
    /**
     * 登陆
     * @param userLoginVO
     * @return
     */
    ResponseData<?> login(UserLoginVO userLoginVO);

    /**
     * 登出
     * @param userLoginVO
     */
    void logout(UserLoginVO userLoginVO);
}
