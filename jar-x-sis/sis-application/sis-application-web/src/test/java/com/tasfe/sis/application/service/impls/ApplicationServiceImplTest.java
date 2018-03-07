package com.tasfe.sis.application.service.impls;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.tasfe.sis.application.model.dto.OrderDTO;
import com.tasfe.sis.application.service.dto.ResponseDTO;
import com.tasfe.sis.base.utils.encrypt.Base64;
import com.tasfe.sis.base.utils.encrypt.Encrypt;

/**
 * Created by dongruixi on 2017/12/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-application-test.xml")
public class ApplicationServiceImplTest {
    @Autowired
    RestTemplate restTemplate;

    final String NID="B-S8-";
    int sn=1;
    final int SIZE=1;
//    @Test
//    public void newOrderBanch(){
//
//        class Lock{
//            public int count=SIZE;
//        }
//        final Lock lock =new Lock();
//
//
//        for(int i=0;i<SIZE;i++) {
//            CompletableFuture.runAsync(
//                    ()->{
//                        try {
//                            newOrder();
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        finally {
//                            lock.count--;
//                        }
//                    }
//            );
//
//        }
//        try {
//            while (lock.count > 0) {
//                Thread.sleep(1000);
//            }
//        }
//        catch (InterruptedException ex){
//            ex.printStackTrace();
//        }
//        Assert.assertTrue(true);
//    }
//
//    @Test
//    public void newOrder() throws Exception {
//        OrderDTO dto = new OrderDTO();
//        String s=new SimpleDateFormat("yyMMdd").format(new Date());
//        String nid=NID+s+"-2"+sn++;
//        System.out.println(String.format("order:%s",nid));
//        dto.setBorrowNid(nid);
//        dto.setRenter("董瑞熙");
//        dto.setIdCardNum("310xx19");
//        dto.setCellphone("13524157392");
//        dto.setGender(1);
//
//        dto.setBirthday(java.sql.Date.valueOf("1980-07-23"));
//        dto.setRegion("上海市");
//        dto.setCountry("静安区");
//        dto.setMarried(1);
//        dto.setIsLocal(0);
//        dto.setSpouse("陈xx");
//        dto.setSpouseIdCardNum("310xxx");
//        dto.setCensus("上海市静安区安远路xxx号");
//        dto.setZipcode("200041");
//        dto.setTel("021-621xxxxx");
//        dto.setHomeAddress("上海市静安区康定路xxx号");
//        dto.setHomeZipcode("200042");
//        dto.setHomeTel("021-622xxxxx");
//        dto.setCompany("前隆科技");
//        dto.setEducational("本科");
//        dto.setIndustry("科技");
//        dto.setOfficeAddress("上海虹口区");
//        dto.setOfficeZipcode("200000");
//        dto.setOffieTel("62580000");
//        dto.setPhotoUrl0("ftp://xxxx.com/310xx19-1.jpg");
//        dto.setPhotoUrl1("ftp://xxxx.com/310xx19-2.jpg");
//        dto.setAgreementUrl("1|2|3|4");
//        dto.setPeriod(3);
//        dto.setAmount(new BigDecimal(5000));
//        dto.setRate(new BigDecimal("0.02"));
//        dto.setRepayBankCard("648923222xxxx0");
//        dto.setLoanBankCard("648923222xxxx1");
//        dto.setChannel("sjd");
//        dto.setLoanUsage("消费");
//
//        HttpEntity<OrderDTO> entity = new HttpEntity<>(dto);
//        ResponseEntity<String> res = restTemplate.postForEntity("http://192.168.1.203:8280/order/new",entity,String.class);
//        Assert.assertTrue(true);
//    }

    @Test
    public void testRabbitMQ(){
    	ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.1.204");
        factory.setUsername("mobp2p");
        factory.setPassword("mobp2p");
        factory.setVirtualHost("/");
        factory.setPort(5672);
        factory.setConnectionTimeout(4000);
		try {
			for(int i=0;i<1000;i++) {
				Connection connection = factory.newConnection();
		        System.out.println("connection 建立");
				Channel channel = connection.createChannel();
		
		        channel.queueDeclare("sis.order.confirm", false, false, false, null);
		        String message = "Hello World!";
		        channel.basicPublish("", "sis.order.confirm", null, message.getBytes("UTF-8"));
		        System.out.println(" [x] Sent '" + message + "'");
		
		        channel.close();
		        connection.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
//    @Test
//    public void testPullApplication(){
//        restTemplate.getForObject("http://211.159.243.249/khl-risk/order/pullApplication",String.class);
//    }


    String getRandomString(int length) {
        StringBuffer buffer = new StringBuffer("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        int range = buffer.length();
        for (int i = 0; i < length; i ++) {
            sb.append(buffer.charAt(random.nextInt(range)));
        }
        return sb.toString();
    }
//    @Test
//    public void testBase64(){
//        //String ss=getRandomString(9000);
//        String ss="中华ss人民共和,*0901232国";
//        String sBase64=Base64.encode(ss.getBytes());
//        String sSource=new String(Base64.decode(sBase64));
//        Assert.assertEquals(ss,sSource);
//    }

//    @Test
//    public void getOrderContract() throws Exception {
//
//        Map<String,String> mParam=new HashMap<String,String>(){{
//            this.put("orderNo","96e7b9b0215a4da98916182cf38c27ed");
//        }};
//        Map<String,String> param = Encrypt.encoder(mParam);
//
//        ResponseEntity<ResponseDTO> res = restTemplate.postForEntity(
//                "http://211.159.243.249/khl-risk/order/getPreivewUrlByOrderNo"
//                ,new HttpEntity<>(param)
//                , ResponseDTO.class);
//
//        String response = res.getBody().getBizResponse();
//
//        String orderList = Encrypt.decoder(response,String.class);
//
//        Assert.assertTrue(true);
//    }
}