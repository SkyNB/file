package com.tasfe.sis.user.service.impls;

import com.tasfe.sis.auth.model.AuthInfos;
import com.tasfe.sis.auth.model.ResourcesCache;
import com.tasfe.sis.auth.service.AuthService;
import com.tasfe.sis.base.model.code.Checkcode;
import com.tasfe.sis.base.model.response.ResponseData;
import com.tasfe.sis.base.service.CacheService;
import com.tasfe.sis.base.service.CheckcodeService;
import com.tasfe.sis.user.model.vo.UserLoginVO;
import com.tasfe.sis.user.service.LoginService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 用户的登陆登出服务
 * Created by Lait on 2017/7/31.
 */
public class LoginServiceImpl implements LoginService {

    @Autowired
    private CheckcodeService checkcodeService;

    @Autowired
    private AuthService authService;

    @Autowired
    private CacheService cacheService;

    /**
     * 登陆
     *
     * @param userLoginVO
     */
    @Override
    public ResponseData<?> login(UserLoginVO userLoginVO) {
        ResponseData<?> responseData = new ResponseData<>();
        // 1.校验验证码或短信验证码
        Checkcode checkcode = new Checkcode();
        boolean success = checkcodeService.verify(checkcode);
        // 2.校验成功，登陆鉴权
        if (success) {
            AuthInfos authInfos = new AuthInfos();
            BeanUtils.copyProperties(userLoginVO, authInfos);
            authService.authentication(authInfos);
        }
        // 3.从权限session或缓存中获取资源树并返回
        cacheService.get(ResourcesCache.arck.toString());
        return responseData;
    }

    /**
     * 登出
     *
     * @param userLoginVO
     */
    @Override
    public void logout(UserLoginVO userLoginVO) {

    }
}
