package com.tasfe.sis.order.service;

import java.util.List;

import com.tasfe.sis.order.entity.RepayInfo;

/**
 * Created by hefusang on 2017/8/10.
 */
public interface RepayInfoService {

    public List<RepayInfo> queryRepayInfo(Long orderId) throws Exception;

    public void save(RepayInfo repay) throws Exception;

}
