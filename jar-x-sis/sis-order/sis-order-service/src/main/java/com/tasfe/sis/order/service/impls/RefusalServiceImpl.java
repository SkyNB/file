package com.tasfe.sis.order.service.impls;

import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import com.tasfe.sis.base.service.CrudService;
import com.tasfe.sis.base.service.impls.CrudServiceImpl;
import com.tasfe.sis.order.entity.OrderCheckLog;
import com.tasfe.sis.order.entity.OrderOperationCase;
import com.tasfe.sis.order.entity.OrderOperationCase;
import com.tasfe.sis.order.service.CheckLogService;
import com.tasfe.sis.order.service.RefusalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by hefusang on 2017/8/10.
 */
@Service("refusalService")
public class RefusalServiceImpl implements RefusalService{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CrudTemplate crudTemplate;

    @Override
    public OrderOperationCase selectByPrimaryKey(String key) throws Exception {
        OrderOperationCase record = new OrderOperationCase();
        record.setId(new Long(key));
        List<OrderOperationCase> orderRefuseCases = crudTemplate.find(record, Criteria.from(OrderOperationCase.class).where().and("id", Operator.EQ).endWhere());
        if(orderRefuseCases.size()>0)
            return orderRefuseCases.get(0);
        return null;
    }

    /*@Override
    public OrderRefuseCase selectByPrimaryKey(String key) throws Exception {
        return super.get(new Long(key));
    }*/
}
