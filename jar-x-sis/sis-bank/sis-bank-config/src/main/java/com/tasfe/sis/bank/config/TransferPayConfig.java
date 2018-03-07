package com.tasfe.sis.bank.config;

import org.springframework.stereotype.Component;

/**
 * 直接走配置中心，目前先写死，便于测试
 * Created by Lait on 2017/8/8.
 */
@Component
public class TransferPayConfig {

    //@Value("${kjt.gatewayUrl}")
    public static String gatewayUrl = "http://mag.kjtpay.com/mag/gateway/receiveOrder.do";
    //@Value("${kjt.cvm}")
    public static String cvm = "/opt/pay/config/basis/mag/cvm.xml";
    //@Value("${kjt.keypwd}")
    public static String keypwd = "123456";
    //@Value("${kjt.pfx}")
    public static String pfx = "/opt/cafiles/200000030006.pfx";
}
