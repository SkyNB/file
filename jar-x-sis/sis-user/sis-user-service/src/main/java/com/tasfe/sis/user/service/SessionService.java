package com.tasfe.sis.user.service;

import com.tasfe.sis.user.entity.User;
import com.tasfe.sis.user.entity.UserSession;
import com.tasfe.sis.user.model.UserLoginInfos;

/**
 * Created by hefusang on 2017/8/10.
 */
public interface SessionService {

    public String userLogin(User user) throws Exception;

    public Long validateSessionCode(String sessionCode) throws Exception;

}
