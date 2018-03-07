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
import com.tasfe.sis.order.handle.AdapterHandler;
import com.tasfe.sis.order.model.vo.BlackListVO;
import com.tasfe.sis.order.service.BlackListService;

/**
 * Created by dongruixi on 2017/12/6.
 */
@Service
public class BlackListServiceImpl implements AdapterHandler<BlackListVO>, BlackListService {
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
	public boolean save(BlackList bo) throws Exception {
		try {
			crudTemplate.save(bo);
		}catch(SQLException e) {
			return false;
		}
		return true;
	}

	@Override
	public List<BlackList> queryList(String idCardNum) throws Exception {
		BlackList entry = new BlackList();
		entry.setIdCardNum(idCardNum);
		List<BlackList> lst = crudTemplate.find(
				entry,
                Criteria.from(BlackList.class)
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
	public boolean handle(BlackListVO vo) throws Exception {
		BlackList bo = new BlackList();
		BeanUtils.copyProperties(vo, bo);
		crudTemplate.save(bo);
		return true;
	}

	@Override
	public boolean insertVo(BlackListVO vo) throws Exception {
		BlackList bo = new BlackList();
		BeanUtils.copyProperties(vo, bo);
		crudTemplate.save(bo);
		return true;
	}
	
	@Override
	public List<BlackList> queryListByCTime(Date startDate, Date endDate) throws Exception {
		BlackList bo = new BlackList();
		List<BlackList> bos = crudTemplate.find(bo,Criteria.from(BlackList.class).where().and("update_time",Operator.GT,new java.sql.Date(startDate.getTime())).and("update_time",Operator.LT,new java.sql.Date(endDate.getTime())).endWhere());
		return bos;
	}
}
