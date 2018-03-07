package com.tasfe.sis.auth.service.impls;

import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.api.params.Page;
import com.tasfe.framework.crud.core.CrudTemplate;
import com.tasfe.sis.auth.entity.Account;
import com.tasfe.sis.auth.entity.AccountRole;
import com.tasfe.sis.auth.entity.Role;
import com.tasfe.sis.auth.model.*;
import com.tasfe.sis.auth.service.AccountRoleService;
import com.tasfe.sis.auth.service.AccountService;
import com.tasfe.sis.bank.api.BankContract;
import com.tasfe.sis.base.configs.HljnsConstants;
import com.tasfe.sis.base.configs.HljnsServerConfig;
import com.tasfe.sis.base.service.CacheService;
import com.tasfe.sis.base.service.CrudService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hefusang on 2017/8/22.
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private CrudTemplate crudTemplate;

    /*@Autowired
    private CrudService crudService;*/

    @Autowired
    private AccountRoleService accountRoleService;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private BankContract bankContract;

    @Override
    public Account getAccount(LoginInfos loginInfos) throws Exception {
        Long bankId = bankContract.getBankIdsWithBankCode(loginInfos.getBankCode());
        Account record = new Account();
        record.setAccount(loginInfos.getAccount());
        record.setPwd(loginInfos.getPwd());
        List<Account> accounts = crudTemplate.find(record, Criteria.from(Account.class));
        if((accounts.size()>0)&&(accounts.get(0).getBankId() == bankId))
            return accounts.get(0);
        return null;
    }

    @Override
    public Long add(AccountInfos accountInfos) throws Exception {
        Account account = new Account();
        account.setAccount(accountInfos.getAccount());
        account.setStatus(new Integer(accountInfos.getStatus()));
        account.setPwd(accountInfos.getPwd());
        account.setTyp(new Integer(accountInfos.getTyp()));
        account.setNick(accountInfos.getNick());
        crudTemplate.save(account);
        List<Account> accounts = crudTemplate.find(account, Criteria.from(Account.class));
        if(accounts.size()>0){
            return accounts.get(0).getId();
        }
        return null;
    }

    @Override
    public Account selectByAccount(String account) throws Exception {
        Account record = new Account();
        record.setAccount(account);
        List<Account> accounts = crudTemplate.find(record, Criteria.from(Account.class));
        if(accounts.size()>0) {
            return accounts.get(0);
        }
        return null;
    }

    @Override
    public List<AccountModel> selectAccount(QueryAccountInfos queryAccountInfos) throws Exception {
        Account record = new Account();
        record.setStatus(new Integer(queryAccountInfos.getStatus()));
        if((StringUtils.isEmpty(queryAccountInfos.getCellPhone()))&&(StringUtils.isEmpty(queryAccountInfos.getEmail()))) {
            if(StringUtils.isNotEmpty(queryAccountInfos.getAccount())) {
                record.setAccount(queryAccountInfos.getAccount());
            }
            List<Account> accounts = crudTemplate.find(record, Criteria.from(Account.class));
            List<AccountModel> resList = new ArrayList<>();
            for (Account account:accounts) {
                AccountModel accountModel = new AccountModel();
                BeanUtils.copyProperties(account,accountModel);
                //���ҵ�ǰ�˺�ӵ�м�����ɫ
                List<Role> roles = accountRoleService.getRolesByAccountId(account.getId());
                accountModel.setRoles(roles);
                resList.add(accountModel);
            }
            return resList;
        } else {
            return null;
        }
    }

    @Override
    public void updateAccount(UpdateAccountInfos updateAccountInfos) throws Exception {
        //修改account基本信息
        Account record = new Account();
        record.setId(new Long(updateAccountInfos.getAccountId()));
        if(StringUtils.isNotEmpty(updateAccountInfos.getPwd())) {
            record.setPwd(updateAccountInfos.getPwd());
        }
        if(StringUtils.isNotEmpty(updateAccountInfos.getStatus())) {
            record.setStatus(new Integer(updateAccountInfos.getStatus()));
        }
        if(StringUtils.isNotEmpty(updateAccountInfos.getNick())) {
            record.setNick(updateAccountInfos.getNick());
        }
        if(StringUtils.isNotEmpty(updateAccountInfos.getTyp())) {
            record.setTyp(new Integer(updateAccountInfos.getTyp()));
        }
        crudTemplate.update(record,Criteria.from(Account.class));
        //关联角色
        AccountRole record1 = new AccountRole();
        record1.setAid(new Long(updateAccountInfos.getAccountId()));
        crudTemplate.del(record1,Criteria.from(AccountRole.class).where().and("aid", Operator.EQ).endWhere());
        if(StringUtils.isNotEmpty(updateAccountInfos.getRoleNames())) {
            AccountInfos accountInfos = new AccountInfos();
            accountInfos.setAccountId(updateAccountInfos.getAccountId());
            accountInfos.setRoleNames(updateAccountInfos.getRoleNames());
            accountRoleService.correlateRoleAndAccount(accountInfos);
        }
    }

    @Override
    public void deleteAccount(DeleteAccountInfos deleteAccountInfos) throws Exception {
        crudTemplate.del(Account.class,new Long(deleteAccountInfos.getId()));
    }

    @Override
    public String authLogin(Account account) {
        //生成code
        StringBuffer sb = new StringBuffer();
        sb.append(System.currentTimeMillis());
        sb.append(account.getAccount());
        sb.append(account.getPwd());

        sb.append(System.currentTimeMillis());
        byte[] base64 = Base64.encodeBase64(sb.toString().getBytes());
        String code = DigestUtils.md5(base64).toString();

        String key = HljnsConstants.PREFIX_REDIS_CODE + code;
        cacheService.set(key,account.getId()+"");
        cacheService.expire(key, 60*30);

        return code;
    }


    @Override
    public Page<Account> pagingAccount(AccountInfos accountInfos, Criteria criteria) throws Exception {
        Page page = crudTemplate.paging(accountInfos,criteria);
        return page;
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
