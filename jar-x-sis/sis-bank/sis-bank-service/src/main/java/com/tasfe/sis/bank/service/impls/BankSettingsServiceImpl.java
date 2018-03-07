package com.tasfe.sis.bank.service.impls;

import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import com.tasfe.sis.auth.entity.Account;
import com.tasfe.sis.bank.entity.Bank;
import com.tasfe.sis.bank.model.BankSettings;
import com.tasfe.sis.bank.service.BankSettingsService;
import com.tasfe.sis.base.service.CrudService;
import com.tasfe.sis.base.service.impls.CrudServiceImpl;
import com.tasfe.sis.user.entity.User;
import com.tasfe.sis.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Created by hefusang on 2017/8/8.
 */
@Service
public class BankSettingsServiceImpl  implements BankSettingsService {

    @Autowired
    private CrudTemplate crudTemplate;

    @Autowired
    private UserService userService;

    @Override
    public BankSettings getBankSettingsWithUserId(String userId) throws Exception {
        Account record = new Account();
        record.setId(new Long(userId));
        List<Account> accounts = crudTemplate.find(record, Criteria.from(Account.class));
        if(accounts.size() > 0) {
            BankSettings record1 = new BankSettings();
            record1.setCustomerId(accounts.get(0).getBankId());
            List<BankSettings> bankSettings = crudTemplate.find(record1, Criteria.from(BankSettings.class));
            if(bankSettings.size() > 0) {
                return bankSettings.get(0);
            }
        }
        return null;
    }

    public Bank getBankWithCode(String code) {

        return null;
    }
}
