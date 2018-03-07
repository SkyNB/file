package com.tasfe.sis.order.service.impls;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import com.tasfe.sis.order.entity.BlackList;
import com.tasfe.sis.order.entity.CreditAccount;
import com.tasfe.sis.order.entity.PersonalCredit;
import com.tasfe.sis.order.handle.AdapterHandler;
import com.tasfe.sis.order.model.CreditAccountAddDTO;
import com.tasfe.sis.order.model.CreditAccountDelDTO;
import com.tasfe.sis.order.model.vo.PersonalCreditVO;
import com.tasfe.sis.order.service.PersonalCreditService;

/**
 * Created by dongruixi on 2017/12/6.
 */
@Service
public class PersonalCreditServiceImpl implements AdapterHandler<PersonalCreditVO>, PersonalCreditService {
    @Autowired
    CrudTemplate crudTemplate;

    @Override
    public Boolean exists(String idCardNum) throws Exception {
        BlackList blackList = new BlackList();
        blackList.setIdCardNum(idCardNum);
        Long count = crudTemplate.count(blackList,
                Criteria.from(BlackList.class)
                        .where()
                        .and("id_card_num", Operator.EQ)
                        .endWhere());

        return count>0;
    }

	@Override
	public boolean save(PersonalCredit bo) throws Exception {
		try {
			crudTemplate.save(bo);
		}catch(SQLException e) {
			return false;
		}
		return true;
	}

	@Override
	public List<PersonalCredit> queryList(String idCardNum) throws Exception {
		PersonalCredit entry = new PersonalCredit();
		entry.setIdCardNum(idCardNum);
		List<PersonalCredit> lst = crudTemplate.find(
				entry,
                Criteria.from(PersonalCredit.class)
                        .where()
                        .and("idCardNum", Operator.EQ)
                        .endWhere()
        );
		return lst;
	}

	/**
	 * 外前置导入数据处理
	 */
	@Override
	public boolean handle(PersonalCreditVO vo) throws Exception {
		PersonalCredit bo = new PersonalCredit();
		BeanUtils.copyProperties(vo, bo);
		crudTemplate.save(bo);
		return true;
	}

	@Override
	public boolean insertVo(PersonalCreditVO vo) throws Exception {
		return true;
	}

	@Override
	public List<PersonalCredit> queryListByCTime(Date startDate, Date endDate) throws Exception {
		PersonalCredit bo = new PersonalCredit();
		List<PersonalCredit> bos = crudTemplate.find(bo,Criteria.from(PersonalCredit.class).where().and("ctime",Operator.GT,new java.sql.Date(startDate.getTime())).and("ctime",Operator.LT,new java.sql.Date(endDate.getTime())).endWhere());
		return bos;
	}

	@Override
	public void addCreditAccount(CreditAccountAddDTO dto) throws Exception {
		CreditAccount record = new CreditAccount();
		record.setAccount(dto.getAccount());
		record.setPassword(dto.getPassword());
		record.setAlUse(0);
		record.setLimitNum(1000);
		record.setStatus(1);
		record.setUtime(new Date());
		crudTemplate.save(record);
	}

	@Override
	public List<CreditAccount> queryAllCreditAccount() throws Exception {
		CreditAccount record = new CreditAccount();
		List<CreditAccount> list = crudTemplate.find(record, Criteria.from(CreditAccount.class).where().endWhere());
		return list;
	}
	
	public void deleteCreditAccount(CreditAccountDelDTO dto) throws Exception {
		CreditAccount record = new CreditAccount();
		record.setId(dto.getId());
		crudTemplate.del(record, Criteria.from(CreditAccount.class).where().endWhere());
	}
}
