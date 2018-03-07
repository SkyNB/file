package com.tasfe.sis.order.service.impls;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import com.tasfe.sis.order.entity.RepayInfo;
import com.tasfe.sis.order.service.RepayInfoService;

/**
 * Created by hefusang on 2017/8/10.
 */
@Service
public class RepayInfoServiceImpl implements RepayInfoService{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CrudTemplate crudTemplate;

	@Override
	public List<RepayInfo> queryRepayInfo(Long orderId) throws Exception {
		RepayInfo info = new RepayInfo();
		info.setOrderId(orderId);
		List<RepayInfo> repays = crudTemplate.find(info,Criteria.from(RepayInfo.class).where().and("order_id", Operator.EQ).endWhere().orderBy("id"));
		return repays;
	}

	@Override
	public void save(RepayInfo repay) throws Exception {
		crudTemplate.save(repay);
	}

}
