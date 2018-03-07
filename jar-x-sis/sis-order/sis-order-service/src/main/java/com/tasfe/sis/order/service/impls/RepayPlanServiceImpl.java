package com.tasfe.sis.order.service.impls;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import com.tasfe.sis.order.entity.RepayInfo;
import com.tasfe.sis.order.entity.RepayPlan;
import com.tasfe.sis.order.service.RepayPlanService;

/**
 * Created by hefusang on 2017/8/10.
 */
@Service
public class RepayPlanServiceImpl implements RepayPlanService{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CrudTemplate crudTemplate;

	@Override
	public List<RepayPlan> queryRepayPlan(Long orderId) throws Exception {
		RepayPlan info = new RepayPlan();
		info.setOrderId(orderId);
		List<RepayPlan> plans = crudTemplate.find(info,Criteria.from(RepayPlan.class).where().and("order_id", Operator.EQ).endWhere().orderBy("id"));
		return plans;
	}

	@Override
	public void save(RepayPlan plan) throws Exception {
		crudTemplate.save(plan);
	}

	@Override
	public List<Long> getYestDayPlan(Date startDate, Date endDate) throws Exception {
		RepayPlan info = new RepayPlan();
		List<Long> ids = Lists.newArrayList();
		List<RepayPlan> plans = crudTemplate.find(info,Criteria.from(RepayPlan.class).fields("order_id").where().and("plan_date",Operator.GT,new java.sql.Date(startDate.getTime())).and("plan_date",Operator.LT,new java.sql.Date(endDate.getTime())).endWhere().orderBy("id"));
		for (RepayPlan plan :plans) {
			ids.add(plan.getOrderId());
		}
		return ids;
	}

}
