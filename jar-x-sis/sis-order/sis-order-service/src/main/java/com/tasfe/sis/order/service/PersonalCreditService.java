package com.tasfe.sis.order.service;

import java.util.Date;
import java.util.List;

import com.tasfe.sis.order.entity.CreditAccount;
import com.tasfe.sis.order.entity.PersonalCredit;
import com.tasfe.sis.order.model.CreditAccountAddDTO;
import com.tasfe.sis.order.model.CreditAccountDelDTO;

/**
 * Created by dongruixi on 2017/12/6.
 */
public interface PersonalCreditService {
    /**
     * 根据身份证判断是否存在授权用户
     * @param idCardNum
     * 身份证号
     * @return
     * 存在授权列用户，返回True，否则为False
     */
	public Boolean exists(String idCardNum) throws Exception;
    /**
     * 存储授权用户bo
     * @param bo
     * @return
     * @throws Exception
     */
    public boolean save(PersonalCredit bo) throws Exception;
    
    /**
	 * 查询授权用户表
	 */
	public List<PersonalCredit> queryList(String idCardNum)throws Exception;
	/**
	 * 查询时间段范围的授信用户
	 */
	public List<PersonalCredit> queryListByCTime(Date startDate,Date endDate)throws Exception;
	
	/**
	 * 增加征信账号
	 * @param dto
	 * @throws Exception
	 */
	public void addCreditAccount(CreditAccountAddDTO dto) throws Exception;
	
	
	
	public List<CreditAccount> queryAllCreditAccount() throws Exception;
	
	
	public void deleteCreditAccount(CreditAccountDelDTO dto) throws Exception;
}
