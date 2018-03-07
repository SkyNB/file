package com.tasfe.sis.bank.service;

import com.tasfe.sis.bank.entity.Bank;
import com.tasfe.sis.bank.model.BankSettings;

/**
 * Created by hefusang on 2017/8/8.
 */
public interface BankSettingsService {

    public BankSettings getBankSettingsWithUserId(String userId) throws Exception;

}
