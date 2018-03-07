package com.tasfe.sis.order.service;

import java.util.List;

import com.tasfe.sis.order.entity.CreditAccount;

/**
 * Created by hefusang on 2017/8/10.
 */
public interface CreditAccountService {
	
    public List<CreditAccount> queryAll() throws Exception;
    
    public List<CreditAccount> queryAllByStatus(int status) throws Exception;

    public void save(CreditAccount bo) throws Exception;
    
    public void closeAccount(Long id) throws Exception;
    
    public void startAccount(Long id) throws Exception;

    public void updateAccount(Long id,Integer aluse) throws Exception;
    
    public void cleanAlUse() throws Exception;
}
