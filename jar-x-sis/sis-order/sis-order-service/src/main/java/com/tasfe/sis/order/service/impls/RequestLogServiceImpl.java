package com.tasfe.sis.order.service.impls;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import com.tasfe.sis.order.entity.RequestLog;
import com.tasfe.sis.order.service.RequestLogService;

/**
 * Created by hefusang on 2017/8/10.
 */
@Service
public class RequestLogServiceImpl implements RequestLogService{

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private CrudTemplate crudTemplate;

	@Override
	public RequestLog queryRequestLog(String type) throws Exception {
		RequestLog info = new RequestLog();
		Date date = new Date();
		info.setType(type);
		List<RequestLog> list = crudTemplate.find(info,Criteria.from(RequestLog.class).where().and("type", Operator.EQ).endWhere());
		if (!list.isEmpty()) {
			return list.get(0);
		}else {
			info = new RequestLog();
			info.setType(type);
			info.setUtime(new java.sql.Date(date.getTime()));
			save(info);
		}
		return info;
	}

	@Override
	public void save(RequestLog log) throws Exception {
		crudTemplate.save(log);
	}
	
	@Override
	public void updateTime(String type, Date date) throws Exception {
		RequestLog log = queryRequestLog(type);
		log.setUtime(new java.sql.Date(date.getTime()));
		crudTemplate.update(log, Criteria.from(RequestLog.class).fields("utime"));
	}
}
