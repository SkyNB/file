package com.tasfe.sis.collection.service.impls;

import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.core.CrudTemplate;
import com.tasfe.sis.collection.entity.Customer;
import com.tasfe.sis.collection.entity.Order;
import com.tasfe.sis.collection.model.OrderVO;
import com.tasfe.sis.collection.model.QueryOrderInfos;
import com.tasfe.sis.collection.service.ColOrderService;
import org.codehaus.jackson.map.util.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hefusang on 2017/9/19.
 */
@Service
public class ColOrderServiceImpl implements ColOrderService {

    @Autowired
    private CrudTemplate crudTemplate;

    @Override
    public Map<String, Object> queryOrder(QueryOrderInfos queryOrderInfos) throws Exception {
        List<OrderVO> resList = new ArrayList<OrderVO>();
        Order record = new Order();
        List<Order> pre_orders = crudTemplate.find(record, Criteria.from(Order.class));
        Integer count = pre_orders.size();
        //筛选出分页结果
        List<Order> orders = null;
        if(pre_orders.size() > queryOrderInfos.getPageNo()*queryOrderInfos.getPageSize()) {
            orders = pre_orders.subList((queryOrderInfos.getPageNo()-1)*queryOrderInfos.getPageSize(),queryOrderInfos.getPageNo()*queryOrderInfos.getPageSize());
        } else {
            orders = pre_orders.subList((queryOrderInfos.getPageNo()-1)*queryOrderInfos.getPageSize(),pre_orders.size());
        }
        //关联客户
        for(Order order:orders) {
            OrderVO orderVO = new OrderVO();
            Customer record2 = new Customer();
            record2.setId(order.getCustomerId());
            List<Customer> customers = crudTemplate.find(record2, Criteria.from(Customer.class));
            BeanUtils.copyProperties(customers.get(0),orderVO);
            BeanUtils.copyProperties(order,orderVO);
            resList.add(orderVO);
        }
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("count",count);
        resMap.put("list",resList);
        return resMap;
    }

}
