package com.tasfe.sis.decision.service.impls;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.tasfe.sis.application.model.dto.OrderDTO;
import com.tasfe.sis.decision.service.DecisionService;
import com.tasfe.sis.decision.service.QueueListener;
import com.tasfe.sis.order.api.ApplicationAssignmentContract;
import com.tasfe.sis.order.api.OrderContract;
import com.tasfe.sis.order.api.dto.OrderItemDTO;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageListener;

import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Reference;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dongruixi on 2017/11/9.
 */
@Service
public class QueueListenerImpl implements QueueListener,InitializingBean {

    @Autowired
    DecisionService decisionService;

    @Resource
    OrderContract orderContract;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Resource
    ApplicationAssignmentContract applicationAssignmentContract;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        try{
            String body = new String(message.getBody(),"UTF-8");
            OrderDTO orderDTO = JSON.parseObject(body,OrderDTO.class);
            Map<String,String> result = decisionService.evaluate(orderDTO);
            OrderItemDTO dto = new OrderItemDTO();
            dto.setId(orderDTO.getId());
            dto.setDecisionResult(result.get("ascore")+":"+result.get("description"));

            if("ok".equalsIgnoreCase(result.get("result"))){
                dto.setCheckStatus(-1); //决策成功:-1
            }
            else{
                dto.setCheckStatus(-2);//决策失败:-2
            }

            dto.setAllocateUserId(-1L);//未分配时为-1
            orderContract.updateOrderDecision(dto);

            applicationAssignmentContract.pushOrder(dto);

            System.out.print(message.toString());
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),true);
        }catch(Exception e){
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            e.printStackTrace();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
