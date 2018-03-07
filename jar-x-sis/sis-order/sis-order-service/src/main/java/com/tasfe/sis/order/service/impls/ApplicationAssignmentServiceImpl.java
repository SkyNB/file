package com.tasfe.sis.order.service.impls;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import com.tasfe.sis.base.service.cache.redis.RedisTemplate;
import com.tasfe.sis.order.api.ApplicationAssignmentContract;
import com.tasfe.sis.order.api.dto.OrderItemDTO;
import com.tasfe.sis.order.entity.Order;
import com.tasfe.sis.order.service.ApplicationAssignmentService;
import com.tasfe.sis.order.service.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongruixi on 2017/12/16.
 */
@Service
public class ApplicationAssignmentServiceImpl implements ApplicationAssignmentService,ApplicationAssignmentContract {
    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    CrudTemplate crudTemplate;

    @Autowired
    OrderService orderService;


    final String RedisOrderAssignmentQueueKey = "SIS-ORDER-ASSIGNMENT-QUEUE";
    final String RedisOrderAssignmentQueueInitLockKey = "SIS-ORDER-ASSIGNMENT-QUEUE-INIT-LOCK";

    final String RedisTaskQueueKeyTemplate = "SIS-ORDER-ASSIGNMENT-USER-QUEUE-%d";

    @Override
    public OrderItemDTO getOrder(Long accountId) throws Exception {
        String redisUserQueueKey = String.format(
                RedisTaskQueueKeyTemplate,accountId
        );


        OrderItemDTO order = null;
        while(order == null) {
            String sOrder="";
            if (redisTemplate.LLen(redisUserQueueKey) > 0) {
                //优先获取人员已有订单
                sOrder = redisTemplate.DeQueue(redisUserQueueKey);
            } else {
                sOrder = redisTemplate.DeQueue(RedisOrderAssignmentQueueKey);
            }

            if(Strings.isNullOrEmpty(sOrder)){
                return null;
            }
            OrderItemDTO dto = JSON.parseObject(sOrder,OrderItemDTO.class);

            order = orderService.getOrder(dto.getId());

            if(order == null){
                //order已经从数据库中删除，则从队列中获取下一笔订单
                continue;
            }
            else if( order.getCheckStatus()!=-1){
                //当前订单状态为非待审核，则获取下一笔订单
                order = null;
                continue;
            }
            else if( order.getAllocateUserId()==accountId){
                break;//已经是本人的订单，直接返回
            }
            else if( order.getAllocateUserId()!=null){
                order = null;
                continue; //如果已经分配则，获取下一笔订单
            }
            else{
                break;//订单未分配，则跳出循环
            }
        }

        if(order.getAllocateUserId()==null || order.getAllocateUserId()==-1)
        {
            //设置订单操作人员Id
            order.setAllocateUserId(accountId);

            Order orderUpdate = new Order();
            orderUpdate.setId(order.getId());
            orderUpdate.setAllocateUserId(accountId);
            crudTemplate.update(order,
                    Criteria.from(Order.class)
                    .where()
                    .and("id",Operator.EQ,order.getId())
                    .endWhere()
                    .fields("allocateUserId")
            );
        }

        return order;

    }

    @Override
    public Boolean pushOrder(OrderItemDTO orderItemDTO) {
        String sOrder = JSON.toJSONString(orderItemDTO);
        redisTemplate.EnQueue(RedisOrderAssignmentQueueKey,sOrder);
        return true;
    }

    @Override
    public Boolean pushUserOrder(Long accountId,OrderItemDTO orderItemDTO) {
        String sOrder = JSON.toJSONString(orderItemDTO);
        redisTemplate.EnQueue(
                String.format(
                        RedisTaskQueueKeyTemplate,accountId
                ),
                sOrder
        );
        return true;
    }

    @Override
    public Boolean initQueue() throws Exception {
        if(redisTemplate.setNX(RedisOrderAssignmentQueueInitLockKey,"lock")!=1) {
            return false;
        }
        try {
            if (redisTemplate.LLen(RedisOrderAssignmentQueueKey) > 0) {
                return true;
            }

            List<Integer> listAllocateUserQueueRegisted= new ArrayList<>();

            Order order = new Order();
            List<Order> orderList = crudTemplate.find(
                    order,
                    Criteria.from(Order.class)
                            .where()
                            .and("check_status", Operator.EQ, -1)

                            .endWhere()
                            .orderBy("ctime")
            );

            orderList.forEach((o) -> {
                OrderItemDTO dto = new OrderItemDTO();
                BeanUtils.copyProperties(o, dto);

                if(o.getAllocateUserId()==null || o.getAllocateUserId()==-1){
                    pushOrder(dto);
                }
                else{
                    Long accountId=dto.getAllocateUserId();
                    String redisTaskQueueKey = String.format(RedisTaskQueueKeyTemplate,accountId);

                    if(!listAllocateUserQueueRegisted.contains(accountId)){
                        redisTemplate.del(redisTaskQueueKey);
                    }

                    redisTemplate.EnQueue(
                            redisTaskQueueKey,
                            JSON.toJSONString(dto)
                            );
                }

            });
        }
        catch (Exception ex)
        {
            throw ex;
        }
        finally {
            redisTemplate.del(RedisOrderAssignmentQueueInitLockKey);
        }

        return true;
    }
}
