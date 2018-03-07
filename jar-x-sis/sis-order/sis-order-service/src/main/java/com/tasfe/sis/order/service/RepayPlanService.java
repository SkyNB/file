package com.tasfe.sis.order.service;

import java.util.Date;
import java.util.List;

import com.tasfe.sis.order.entity.RepayPlan;

/**
 * Created by hefusang on 2017/8/10.
 */
public interface RepayPlanService {

    public List<RepayPlan> queryRepayPlan(Long orderId) throws Exception;

    public void save(RepayPlan plan) throws Exception;
    
    public List<Long> getYestDayPlan(Date startDate,Date endDate)throws Exception;

}
