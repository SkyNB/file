package com.tasfe.sis.bank.service;

import com.tasfe.sis.bank.entity.Bank;
import com.tasfe.sis.bank.model.BankSettings;
import com.tasfe.sis.bank.model.BankVO;

import java.util.List;

/**
 * Created by hefusang on 2017/8/8.
 */
public interface BankService {

    public Bank getBankWithCode(String code);

}
