package com.tasfe.sis.decision.service;

import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;

/**
 * Created by dongruixi on 2017/11/9.
 */
public interface QueueListener extends ChannelAwareMessageListener {

}
