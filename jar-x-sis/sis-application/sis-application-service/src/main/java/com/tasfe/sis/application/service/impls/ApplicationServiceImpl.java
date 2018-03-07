package com.tasfe.sis.application.service.impls;


import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.tasfe.sis.application.model.dto.OrderDTO;
import com.tasfe.sis.application.service.ApplicationService;
import com.tasfe.sis.base.service.cache.redis.RedisTemplate;
import com.tasfe.sis.order.api.ApplicationAssignmentContract;
import com.tasfe.sis.order.api.BillContract;
import com.tasfe.sis.order.api.OrderContract;
import com.tasfe.sis.order.api.dto.OrderItemDTO;

/**
 * Created by dongruixi on 2017/11/7.
 */
@Service
public class ApplicationServiceImpl implements ApplicationService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    RedisTemplate redisTemplate;

    @Resource
    OrderContract orderContract;

    @Resource
    BillContract billContract;

    @Resource
    ApplicationAssignmentContract applicationAssignmentContract;

    @Override
    public String newOrder(OrderDTO order) throws Exception {
        String redisMapId=String.format("SIS-IDs-MAP:%s",order.getBorrowNid());

        //判断缓存中是否有该笔订单
        if(redisTemplate.exists(redisMapId)){
           return "";
        }

        //判断数据库中是否有该笔订单
        OrderItemDTO dto = orderContract.getOrder(order.getBorrowNid());
        if(dto!=null){
            redisTemplate.set(redisMapId,String.valueOf(dto.getId()));
            return "";
        }

        OrderItemDTO itemDTO=new OrderItemDTO();

        BeanUtils.copyProperties(order,itemDTO);
        itemDTO.setExtScore(order.getScore());
        itemDTO.setCheckStatus(-99); //进件状态：99

        Long orderId = orderContract.newOrder(itemDTO);
        order.setId(orderId);
        redisTemplate.set(redisMapId,String.valueOf(orderId));

        Long id = redisTemplate.incr("sis.order.id");

        SimpleDateFormat dtf=new SimpleDateFormat("yyyyMMdd");

        String sn=String.format("%s-%d", dtf.format(new Date()), id);

        order.setSn(sn);


        String orderStr = JSON.toJSONString(order);
        new Thread(()->{
            boolean flag = true;
        	while(flag) {
            	try{
            		rabbitTemplate.send("sis.order.topic.exchange", "sis.application.new-order", new Message(orderStr.getBytes(), new MessageProperties()));
            		flag = false;
	            } catch (Exception e) {
	            	try {
	            		logger.error("sis.order.confirm error,after 5 sec retry",e);
						Thread.sleep(5000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
						break;
					}
	            }
            }
        }).start();
        return sn;
    }




    @PostConstruct
    public void postConstruct() throws Exception {
        applicationAssignmentContract.initQueue();
    }
}
