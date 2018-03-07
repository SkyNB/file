package com.tasfe.sis.user.service.impls;

import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import com.tasfe.sis.base.configs.HljnsConstants;
import com.tasfe.sis.base.service.CacheService;
import com.tasfe.sis.base.service.CrudService;
import com.tasfe.sis.base.service.impls.CrudServiceImpl;
import com.tasfe.sis.user.entity.User;
import com.tasfe.sis.user.entity.UserSession;
import com.tasfe.sis.user.service.SessionService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhou on 17/6/26.
 */

@Service
public class SessionServiceImpl extends CrudServiceImpl<UserSession,Long> implements CrudService<UserSession,Long>,SessionService {

    private static Logger LOGGER = LoggerFactory.getLogger(SessionServiceImpl.class);

    @Autowired
    private CacheService cacheService;

    @Autowired
    private CrudTemplate crudTemplate;

    @Override
    public String userLogin(User user) throws Exception {
        //生成code
        StringBuffer sb = new StringBuffer();
        sb.append(System.currentTimeMillis());
        sb.append(user.getName());
        sb.append(user.getPwd());
        sb.append(System.currentTimeMillis());
        byte[] base64 = Base64.encodeBase64(sb.toString().getBytes());
        String code = DigestUtils.md5(base64).toString();
        //老会话信息失效
        UserSession root = new UserSession();
        List<UserSession> temptList = crudTemplate.find(root, Criteria.from(UserSession.class).where().and("status", Operator.EQ, 1).and("user_id", Operator.EQ, user.getId()).endWhere());
        for (UserSession temptSession:temptList) {
            temptSession.setStatus(0);
        }
        if(temptList.size()>0) {
            crudTemplate.update(temptList, Criteria.from(UserSession.class).fields("status"));
        }
        //保存新的code
        UserSession userSession = new UserSession();
        userSession.setCode(code);
        userSession.setStatus(HljnsConstants.STATUS_ONE);
        userSession.setUserId(user.getId().longValue());
        try {
            crudTemplate.save(userSession);
            String key = HljnsConstants.PREFIX_REDIS_CODE + code;
            cacheService.set(key, user.getId().toString());
            int seconds = new Integer(HljnsConstants.SESSION_TIMEOUT);//Integer.valueOf(CacheManager.getActiveMapValue("session_expiry_sec"));
            cacheService.expire(key, seconds);
            //更新redis的userId:code
            cacheService.set(String.valueOf(user.getId()), code);
            return code;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Long validateSessionCode(String sessionCode) throws Exception {
        int sessionExpirySec = new Integer(HljnsConstants.SESSION_TIMEOUT);
        String key = HljnsConstants.PREFIX_REDIS_CODE + sessionCode;
        if (cacheService.exists(key) && cacheService.get(key) != null) {
            String userId = cacheService.get(key);
            cacheService.expire(key,sessionExpirySec);
            return Long.valueOf(userId);
        }
        return null;
    }

}
