package com.tasfe.sis.order.service.impls;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import com.tasfe.sis.order.entity.CreditAccount;
import com.tasfe.sis.order.service.CreditAccountService;

/**
 * Created by hefusang on 2017/8/10.
 */
@Service
public class CreditAccountServiceImpl implements CreditAccountService{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CrudTemplate crudTemplate;

	@Override
	public List<CreditAccount> queryAll() throws Exception {
		CreditAccount bo = new CreditAccount();
        List<CreditAccount> lists = crudTemplate.find(bo,Criteria.from(CreditAccount.class));
		return lists;
	}

	@Override
	public List<CreditAccount> queryAllByStatus(int status) throws Exception {
		CreditAccount bo = new CreditAccount();
		bo.setStatus(status);
        List<CreditAccount> lists = crudTemplate.find(bo,Criteria.from(CreditAccount.class).where()
        		.and("status", Operator.EQ).endWhere());
		return lists;
	}

	@Override
	public void save(CreditAccount bo) throws Exception {
		crudTemplate.save(bo);
	}

	@Override
	public void closeAccount(Long id) throws Exception {
		CreditAccount bo = new CreditAccount();
		bo.setId(id);
		bo.setStatus(0);
		crudTemplate.update(bo,Criteria.from(CreditAccount.class).fields("status"));
	}

	@Override
	public void startAccount(Long id) throws Exception {
		CreditAccount bo = new CreditAccount();
		bo.setId(id);
		bo.setStatus(1);
		crudTemplate.update(bo,Criteria.from(CreditAccount.class).fields("status"));
	}

	@Override
	public void updateAccount(Long id,Integer aluse) throws Exception {
		CreditAccount bo = new CreditAccount();
		bo.setId(id);
		bo.setAlUse(aluse);
		crudTemplate.update(bo,Criteria.from(CreditAccount.class).fields("alUse"));
	}

	@Override
	public void cleanAlUse() throws Exception {
		CreditAccount bo = new CreditAccount();
		bo.setAlUse(0);
		crudTemplate.update(bo,Criteria.from(CreditAccount.class).fields("alUse"));
		
	}

}
