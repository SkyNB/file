package com.tasfe.sis.user.ws.controller;

import com.tasfe.sis.auth.model.vo.UserVO;
import com.tasfe.sis.auth.service.AuthService;
import com.tasfe.sis.bank.entity.Bank;
import com.tasfe.sis.bank.model.BankVO;
import com.tasfe.sis.bank.service.BankService;
import com.tasfe.sis.base.model.response.ResponseData;
import com.tasfe.sis.base.service.CacheService;
import com.tasfe.sis.user.entity.User;
import com.tasfe.sis.user.model.UserLoginInfos;
import com.tasfe.sis.user.service.SessionService;
import com.tasfe.sis.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lait on 2017/7/28.
 */
@RestController
@RequestMapping("/auth")
public class UserController {
    private static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private BankService bankService;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private SessionService sessionService;


    /**
     * 登陆
     * @param userLoginInfos
     * @return
     */

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
    public ResponseData<UserVO> login(@RequestBody UserLoginInfos userLoginInfos) throws Exception {
        ResponseData responseData = new ResponseData();
        //非空判断
        if(userLoginInfos.getCustomerCode() == null || "".equals(userLoginInfos.getCustomerCode())){
            return responseData.setError("用户号为空");
        }
        if(userLoginInfos.getLoginName() == null || "".equals(userLoginInfos.getLoginName())){
            return responseData.setError("用户名为空");
        }
        if(userLoginInfos.getLoginPwd() == null || "".equals(userLoginInfos.getLoginPwd())){
            return responseData.setError("密码为空");
        }
        Bank bank = bankService.getBankWithCode(userLoginInfos.getCustomerCode());
        if(bank != null) {
            List<User> users = userService.getUsersByCustomerId(bank.getId() + "");
            for(User user:users) {
                if((userLoginInfos.getLoginName().equals(user.getName()))&&(userLoginInfos.getLoginPwd().equals(user.getPwd()))) {
                    Map<String, Object> resMap = new HashMap<>();
                    String sessionCode = sessionService.userLogin(user);
                    resMap.put("code",sessionCode);
                    resMap.put("legalName",user.getName());
                    resMap.put("loginType",String.valueOf(user.getLoginType()));
                    resMap.put("permissionSet",String.valueOf(user.getPermissionSet()));
                    responseData.setData(resMap);
                    responseData.setStatus("1");
                    responseData.setError("00000000");
                    return responseData;
                }
            }
            responseData.setError("登陆用户名或密码不正确");
            return responseData;
        }
        responseData.setError("客户号不存在");
        return responseData;
    }


    /**
     * 登出
     * @param userLoginInfos
     * @return
     */
    public ResponseData logout(@RequestBody UserLoginInfos userLoginInfos){

        return null;
    }



    @RequestMapping(value = "/session", method = RequestMethod.POST, produces = "application/json")
    public ResponseData<Boolean> validateSession(@RequestBody UserLoginInfos userLoginInfos){
        ResponseData responseData = new ResponseData();
        Long userId = null;
        try {
            userId = sessionService.validateSessionCode(userLoginInfos.getSessionCode());
        } catch (Exception e) {
            e.printStackTrace();
            return responseData.setData(false);
        }
        if(userId == null) {
            return responseData.setData(false);
        }
        responseData.setStatus("1");
        responseData.setError("00000000");
        return responseData.setData(true);
    }

}
