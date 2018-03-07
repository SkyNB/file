package com.tasfe.sis.auth.ws.controller;

import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.params.Page;
import com.tasfe.sis.auth.entity.Account;
import com.tasfe.sis.auth.entity.Resources;
import com.tasfe.sis.auth.entity.Role;
import com.tasfe.sis.auth.model.*;
import com.tasfe.sis.auth.service.AccountRoleService;
import com.tasfe.sis.auth.service.AccountService;
import com.tasfe.sis.auth.service.RoleResourcesService;
import com.tasfe.sis.auth.service.RoleService;
import com.tasfe.sis.base.model.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hefusang on 2017/8/22.
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRoleService accountRoleService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleResourcesService roleResourcesService;

    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json")
    public ResponseData addAccount(@RequestBody AccountInfos accountInfos) {
        ResponseData responseData = new ResponseData();
        Account account = null;
        try {
            account = accountService.selectByAccount(accountInfos.getAccount());
        } catch (Exception e) {
            e.printStackTrace();
            responseData.setStatus("0");
            responseData.setMsg("添加账号失败");
            responseData.setError("380000000002");
            return responseData;
        }
        if(account != null) {
            responseData.setStatus("0");
            responseData.setMsg("此账号已存在");
            return responseData;
        }
        try{
            Long accountId = accountService.add(accountInfos);
            accountInfos.setAccountId(String.valueOf(accountId));
            accountRoleService.correlateRoleAndAccount(accountInfos);
        } catch (Exception ex) {
            ex.printStackTrace();
            responseData.setStatus("0");
            responseData.setError("380000000005");
            return responseData;
        }
        responseData.setStatus("1");
        responseData.setMsg("添加账号成功");
        responseData.setError("000000000000");
        return responseData;
    }


    @RequestMapping(value = "/paging", method = RequestMethod.POST, produces = "application/json")
    public ResponseData paging(@RequestBody AccountInfos accountInfos) throws Exception {
        ResponseData<Page> responseData = new ResponseData();
        Page<Account> page = accountService.pagingAccount(accountInfos, Criteria.from(Account.class).limit(Page.resolve(accountInfos.getPageNo(), accountInfos.getPageSize())));
        responseData.setData(page);
        responseData.setCode("100000");
        responseData.setMsg("success");
        responseData.setStatus("1");
        return responseData;


    }

    @RequestMapping(value = "/list", method = RequestMethod.POST, produces = "application/json")
    public ResponseData queryAccount(@RequestBody QueryAccountInfos queryAccountInfos) {
        ResponseData responseData = new ResponseData();
        try {
            List<AccountModel> accounts = accountService.selectAccount(queryAccountInfos);
            responseData.setStatus("1");
            responseData.setData(accounts);
        } catch (Exception e) {
            e.printStackTrace();
            responseData.setStatus("0");
            responseData.setMsg("查询账户失败");
        }
        return responseData;
    }

    @RequestMapping(value = "/upd", method = RequestMethod.POST, produces = "application/json")
    public ResponseData updateAccount(@RequestBody UpdateAccountInfos updateAccountInfos) {
        ResponseData responseData = new ResponseData();
        try {
            accountService.updateAccount(updateAccountInfos);
            responseData.setStatus("1");
            responseData.setMsg("修改账号信息成功");
            return responseData;
        } catch (Exception e) {
            e.printStackTrace();
            responseData.setStatus("0");
            responseData.setMsg("修改账号信息失败");
            return responseData;
        }
    }

    @RequestMapping(value = "/del", method = RequestMethod.POST, produces = "application/json")
    public ResponseData deleteAccount(@RequestBody DeleteAccountInfos deleteAccountInfos) {
        ResponseData responseData = new ResponseData();
        try {
            accountService.deleteAccount(deleteAccountInfos);
            responseData.setStatus("1");
        } catch (Exception e) {
            e.printStackTrace();
            responseData.setStatus("0");
            responseData.setMsg("删除账号失败");
        }
        return responseData;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
    public ResponseData login(@RequestBody LoginInfos loginInfos) {
        ResponseData responseData = new ResponseData();
        Account account = null;
        String code = "";
        try {
            account = accountService.getAccount(loginInfos);
            code = accountService.authLogin(account);
        } catch (Exception e) {
            e.printStackTrace();
            responseData.setStatus("0");
            responseData.setMsg("客户号或账号或者密码不正确");
            return responseData;
        }
        //获取角色权限
        List<Role> roles = null;
        try {
            roles = accountRoleService.getRolesByAccountId(account.getId());
        } catch (Exception e) {
            e.printStackTrace();
            responseData.setStatus("0");
            responseData.setMsg("获取角色失败");
            return responseData;
        }
        Map<String,Object> resMap = new HashMap<>();
        resMap.put("code",code);
        resMap.put("roles",roles);
        responseData.setError("00000000");
        responseData.setStatus("1");
        responseData.setMsg("登陆成功");
        responseData.setData(resMap);
        return responseData;
    }

}
