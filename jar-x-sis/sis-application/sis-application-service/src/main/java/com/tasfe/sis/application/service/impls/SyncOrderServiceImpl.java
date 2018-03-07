package com.tasfe.sis.application.service.impls;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.tasfe.sis.application.service.dto.IdentityPhotoDTO;
import com.tasfe.sis.base.utils.encrypt.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.file.remote.session.Session;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.rabbitmq.client.Channel;
import com.tasfe.sis.application.service.ApplicationService;
import com.tasfe.sis.application.service.SyncOrderService;
import com.tasfe.sis.application.service.dto.ApplicationDTO;
import com.tasfe.sis.application.service.dto.ConfirmDTO;
import com.tasfe.sis.application.service.dto.ResponseDTO;
import com.tasfe.sis.base.service.cache.redis.RedisTemplate;
import com.tasfe.sis.base.utils.PropertyUtil;
import com.tasfe.sis.base.utils.encrypt.Encrypt;
import com.tasfe.sis.order.api.OrderContract;
import com.tasfe.sis.order.api.dto.OrderItemDTO;
import com.tasfe.sis.order.api.dto.RepayPlanDTO;
import com.tasfe.sis.order.api.dto.RepaymentDTO;

/**
 * Created by dongruixi on 2017/12/8.
 */
@Service
public class SyncOrderServiceImpl implements SyncOrderService,ChannelAwareMessageListener {

    Logger logger= LoggerFactory.getLogger(SyncOrderServiceImpl.class);

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ApplicationService applicationServiceImpl;

    @Resource
    OrderContract orderContract;


    @Autowired
    DefaultSftpSessionFactory sftpSessionFactory;


    @Async
    @Override
    public Boolean downloadFiles(List<String> remoteFiles) throws Exception {
        final String localPath = PropertyUtil.getProperty("photo.download.localStoragePath");
        Session session = sftpSessionFactory.getSession();
        remoteFiles.forEach((path)->{
            String[] paths=path.split("/");
            String fileName = paths[paths.length-1];
            OutputStream os = null;
            try {
                os = new FileOutputStream(localPath + "/" + fileName);
                session.read(path, os);
                os.flush();
            }
            catch (FileNotFoundException ex)
            {
                logger.error(String.format("Local Path %s not found",localPath));
            }
            catch (IOException ex)
            {
            	logger.error(ex.getMessage());
            }finally {
                if(os!=null){
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        });
        session.close();


        return true;

    }

    @Override
    public Boolean downloadIdentityPhoto(Long orderId, String idCardNo) throws Exception {
        final String localPath = PropertyUtil.getProperty("photo.download.localStoragePath");
        Map<String,String> identityInfo=new HashMap<String,String>(){{
            this.put("identityNumber",idCardNo);
        }};
        Map<String,String> param = Encrypt.encoder(identityInfo);


        ResponseEntity<ResponseDTO> res = restTemplate.postForEntity(
                        PropertyUtil.getProperty("http.downloadIdentityPhoto.api"),
                        new HttpEntity<>(param),
                ResponseDTO.class);



        List<String> urls = new ArrayList<String>(){{
            this.add("");
            this.add("");
        }};


        if(res.getStatusCode()==HttpStatus.OK){
            List<IdentityPhotoDTO> list = JSON.parseArray(res.getBody().getBizResponse(),IdentityPhotoDTO.class);
            for(IdentityPhotoDTO photo : list){
                byte[] bytes = Base64.decode(photo.getPhoto());
                String fileName = String.format("%s_%s.%s",
                        idCardNo,
                        photo.getType(),
                        photo.getExtensions()
                );
                urls.set(("0".equals(photo.getType()))?0:1,fileName);
                FileOutputStream fs = new FileOutputStream(
                        String.format("%s/%s",
                                localPath,fileName
                        )
                );
                fs.write(bytes);
                fs.flush();
                fs.close();
            }

            orderContract.updateOrderPhotos(orderId,urls.get(0),urls.get(1));

            return true;
        }
        else{
            logger.error("Cannot download identity photos by access api error.");
            return false;
        }
    }


    @Override
    public Integer pullApplication() throws Exception {
        final String lock="SIS-PULL-APPLICATION-LOCK";
        if(redisTemplate.setNX(lock,String.valueOf(new Date().getTime()))==1){
            try{

                ResponseEntity<ResponseDTO> res = restTemplate.getForEntity(
                        PropertyUtil.getProperty("http.pullApplication.api"), ResponseDTO.class);

                List<String> orderIdlist=new ArrayList<>();

                if(res.getStatusCode()== HttpStatus.OK){
                    String response = res.getBody().getBizResponse();

                    ApplicationDTO orderList = Encrypt.decoder(response,ApplicationDTO.class);

                    orderList.forEach((order)->{

                        String orderProced = String.format("SIS-SYNC-ORDER-APPLICATION-ORDERS-PROCCED:%s", order.getBorrowNid());
                        if(redisTemplate.setNX(orderProced,"")==1){
                            try {
                                order.setBankCode(PropertyUtil.getProperty("BANK.CODE"));
                                applicationServiceImpl.newOrder(order);
                                orderIdlist.add(order.getBorrowNid());
                                //通过sftp下载照片
                                new Thread(()->{
                                    try {
                                        downloadIdentityPhoto(order.getId(), order.getIdCardNum());
                                    } catch (Exception e) {
                                        logger.error("Pull order photos error",e);
                                    }



                                }).start();
                                redisTemplate.expire(orderProced,60*60*24*2);//缓存2天
                            } catch (Exception e) {
                                redisTemplate.del(orderProced);
                                e.printStackTrace();
                            }
                        }
                        else{
                            //已经处理订单，不重复处理
                        }

                    });
                    if(orderIdlist.size()>0) {
                        Map<String, String> params = Encrypt.encoderObject(orderIdlist);

                        ResponseEntity<ResponseDTO> dto = restTemplate.postForEntity(
                                PropertyUtil.getProperty("http.commitApplication.api"),
                                new HttpEntity<Map<String, String>>(params),
                                ResponseDTO.class
                        );

                        return orderIdlist.size();
                    }
                    else{
                        return 0;
                    }
                }


            }
            catch (Exception ex)
            {
                throw ex;
            }
            finally {
                redisTemplate.del(lock);
            }


        }

        return -1;
    }

    /**
     * 推送审批结果
     * @return
     * @throws Exception
     */
    @Override
    public Boolean pushApprove(String orderId, String status) throws Exception {



        Map<String,String> approval=new HashMap<String,String>(){{
            this.put("orderId",orderId);
            this.put("status",status);
        }};
        Map<String,String> param = Encrypt.encoder(approval);
        ResponseEntity<String> res = restTemplate.postForEntity(PropertyUtil.getProperty("http.pushApprove.api"), new HttpEntity<>(param),String.class);





        return true;
    }


    private List<Map<String,Object>> getPlans(Long orderId) throws Exception {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        List<RepayPlanDTO> plans=orderContract.getRepayPlan(orderId);
        List<Map<String,Object>> lPlan= plans.stream().map((plan)->{
            Map<String,Object> planInfo=new HashMap<String,Object>(){{
                this.put("planDate",sdf.format(plan.getPlanDate()));
                this.put("planAmount",plan.getPlanAmount());
                this.put("amount",plan.getAmount());
                this.put("overdueAmount",plan.getOverdueAmount());
            }};
            return planInfo;
        }).collect(Collectors.toList());
        return lPlan;
    }

    /**
     * 推送放款
     * @return
     * @throws Exception
     */
    @Override
    public Boolean pushLoans(Date date) throws Exception {

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

        final String lock="SIS-PUSH-LOANS-LOCK";
        if(redisTemplate.setNX(lock,String.valueOf(date.getTime()))==1){
        		redisTemplate.expire(lock, 5);
            try{
                List<OrderItemDTO> list = orderContract.getLoanedOrderItems(date);
                List<Map<String,Object>> lparam = list.stream().map((dto)->{

                    Map<String,Object> orderInfo=new HashMap<String,Object>(){{
                        this.put("borrowNid",dto.getBorrowNid());
                        this.put("amount",String.valueOf(dto.getAmount()));
                        this.put("date",sdf.format(dto.getLoanDay()));
                    }};

                    return orderInfo;
                }).collect(Collectors.toList());

                Map<String,String> param = Encrypt.encoderObject(lparam);
                ResponseEntity<String> res = restTemplate.postForEntity(PropertyUtil.getProperty("http.pushLoans.api"), new HttpEntity<>(param),String.class);
            }
            catch (Exception ex)
            {
                throw ex;
            }
            finally {
                redisTemplate.del(lock);
            }


        }

        return true;
    }

    /**
     * 推送还款明细
     * @return
     * @throws Exception
     */
    @Override
    public Boolean pushRepayment(Date date) throws Exception {
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");

        final String lock="SIS-PUSH-REPAYMENT-LOCK";
        if(redisTemplate.setNX(lock,String.valueOf(date.getTime()))==1){
            try{

                List<RepaymentDTO> list = orderContract.getRepayment(date);

                List<Map<String,Object>> lParam=list.stream().map((dto)->{
                    List<Map<String,Object>> plans = new ArrayList<>();
                    try {
                        plans.addAll(getPlans(dto.getOrderId()));
                    } catch (Exception e) {
                        logger.error(
                                String.format("Load plan error for orderId%d",dto.getOrderId()),
                                e
                        );
                        return null;
                    }
                    try {
						OrderItemDTO order = orderContract.getOrder(dto.getOrderId());
						Map<String,Object> orderInfo=new HashMap<String,Object>(){{
	                        this.put("borrowNid",dto.getBorrowNid());
	                        this.put("amount",dto.getRepayAmount());
	                        this.put("date",sdf.format(dto.getRepayDate()));
	                        this.put("plan",plans);
	                        this.put("status","close".equalsIgnoreCase(order.getContractStatus())?"1":"0");
	                    }};
	                    return orderInfo;
					} catch (Exception e) {
						logger.error(
                                String.format("Load plan error for orderId%d",dto.getOrderId()),
                                e
                        );
                        return null;
					}
                    
                    
                }).filter((i)->{
                	return i!=null;
                }).collect(Collectors.toList());

                Map<String,String> param = Encrypt.encoderObject(lParam);
                ResponseEntity<String> res = restTemplate.postForEntity(PropertyUtil.getProperty("http.pushRepay.api"), new HttpEntity<>(param),String.class);
            }
            catch (Exception ex)
            {
                throw ex;
            }
            finally {
                redisTemplate.del(lock);
            }


        }

        return true;
    }

    @Override
    public Boolean downloadOrderPhotos(Long orderId, String url0, String url1) throws Exception {
        downloadFiles(
                new ArrayList<String>(){{
                    if(!Strings.isNullOrEmpty(url0)){
                        this.add(url0);
                    }
                    if(!Strings.isNullOrEmpty(url1)){
                        this.add(url1);
                    }
                }}
        );
        String[] photoUrl0PathInfo=Strings.isNullOrEmpty(url0)?null:url0.split("/");
        String[] photoUrl1PathInfo=Strings.isNullOrEmpty(url1)?null:url1.split("/");

        orderContract.updateOrderPhotos(
                orderId,
                photoUrl0PathInfo==null?null:photoUrl0PathInfo[photoUrl0PathInfo.length-1],
                photoUrl1PathInfo==null?null:photoUrl1PathInfo[photoUrl1PathInfo.length-1]
        );
        return true;
    }


    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        String body = new String(message.getBody(),"UTF-8");
        ConfirmDTO dto = JSON.parseObject(body,ConfirmDTO.class);

        try{
            pushApprove(dto.getBorrowNid(),dto.getCheckStatus());
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),true);
        }catch(Exception e){
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            logger.error(String.format("Push approve error OrderId(%s)",dto.getBorrowNid()),e);
        }

    }


}
