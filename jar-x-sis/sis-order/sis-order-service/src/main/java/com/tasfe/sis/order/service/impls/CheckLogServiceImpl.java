package com.tasfe.sis.order.service.impls;

import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import com.tasfe.sis.base.configs.HljnsConstants;
import com.tasfe.sis.base.service.CrudService;
import com.tasfe.sis.base.service.impls.CrudServiceImpl;
import com.tasfe.sis.order.entity.Bill;
import com.tasfe.sis.order.entity.OrderCheckLog;
import com.tasfe.sis.order.model.ConfirmBillDto;
import com.tasfe.sis.order.model.QueryBillDto;
import com.tasfe.sis.order.model.vo.BillVO;
import com.tasfe.sis.order.service.BillService;
import com.tasfe.sis.order.service.CheckLogService;
import com.tasfe.sis.user.entity.User;
import com.tasfe.sis.user.service.SessionService;
import com.tasfe.sis.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by hefusang on 2017/8/10.
 */
@Service("billService")
public class CheckLogServiceImpl implements CheckLogService{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CrudTemplate crudTemplate;

    @Override
    public OrderCheckLog selectCheckLogByOrderId(String orderId) throws Exception {
        OrderCheckLog record = new OrderCheckLog();
//        record.setCid(orderId);
        List<OrderCheckLog> checkLogs = crudTemplate.find(record, Criteria.from(OrderCheckLog.class).where().and("cid", Operator.EQ).endWhere());
        if(checkLogs.size()>0)
            return checkLogs.get(0);
        return null;
    }


    /*@Override
    public OrderCheckLog selectCheckLogByOrderId(String orderId) {
        OrderCheckLog checkLog = new OrderCheckLog();
        checkLog.setCid(orderId);
        List<OrderCheckLog> orderCheckLogs = super.get(checkLog);
        if((orderCheckLogs!=null)&&(orderCheckLogs.size()>0)) {
            return orderCheckLogs.get(0);z
        }
        return null;
    }*/
}
