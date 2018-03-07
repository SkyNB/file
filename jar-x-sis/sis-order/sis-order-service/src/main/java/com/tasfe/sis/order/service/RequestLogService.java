package com.tasfe.sis.order.service;

import java.util.Date;

import com.tasfe.sis.order.entity.RequestLog;

/**
 * Created by hefusang on 2017/8/10.
 */
public interface RequestLogService {

    public RequestLog queryRequestLog(String type) throws Exception;

    public void save(RequestLog repay) throws Exception;
    
    public void updateTime(String type, Date date) throws Exception;

}
