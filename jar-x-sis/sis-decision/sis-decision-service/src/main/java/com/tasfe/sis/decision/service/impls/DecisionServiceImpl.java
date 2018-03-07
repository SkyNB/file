package com.tasfe.sis.decision.service.impls;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import com.tasfe.sis.application.model.dto.OrderDTO;
import com.tasfe.sis.base.service.cache.redis.RedisTemplate;
import com.tasfe.sis.base.utils.PropertyUtil;
import com.tasfe.sis.base.utils.encrypt.Encrypt;
import com.tasfe.sis.decision.entity.DecisionScript;
import com.tasfe.sis.decision.enums.DecisionResultEnum;
import com.tasfe.sis.decision.model.BlackListToExportVO;
import com.tasfe.sis.decision.model.DecisionVO;
import com.tasfe.sis.decision.service.BlackListService;
import com.tasfe.sis.decision.service.DecisionService;
import com.tasfe.sis.decision.service.dto.ResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by dongruixi on 2017/11/9.
 */
@Service
public class DecisionServiceImpl implements DecisionService {

    Logger logger= LoggerFactory.getLogger(DecisionServiceImpl.class);

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CrudTemplate crudTemplate;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    BlackListService blackListServiceImpl;

    @Autowired
    RabbitTemplate rabbitTemplate;



    private Map<String,String> buildResult(DecisionResultEnum result,String decision,String score){
        Map<String,String> drest=new HashMap<>();

        drest.put("result",result.getName());
        drest.put("description",decision);
        drest.put("ascore",score);

        return drest;
    }

    private Map<String,Object> convertToParameters(OrderDTO orderDTO){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

        Map<String,Object> parameters = new HashMap<>();

        parameters.put("姓名",orderDTO.getRenter());
        parameters.put("身份证号",orderDTO.getIdCardNum());
        parameters.put("手机号",orderDTO.getCellphone());
        parameters.put("性别",orderDTO.getGender()==1?"男":"女");
        parameters.put("生日",sdf.format(orderDTO.getBirthday()));
        parameters.put("省市",orderDTO.getRegion());
        parameters.put("区县",orderDTO.getCountry());
        parameters.put("婚姻状况",orderDTO.getMarried()==1?"已婚":"未婚");
        parameters.put("是否当地",orderDTO.getIsLocal()==1?"是":"否");
        parameters.put("配偶姓名",orderDTO.getSpouse());
        parameters.put("配偶身份证号",orderDTO.getSpouseIdCardNum());
        parameters.put("户籍",orderDTO.getCensus());
        parameters.put("邮编",orderDTO.getZipcode());
        parameters.put("联系电话",orderDTO.getTel());
        parameters.put("家庭住址",orderDTO.getHomeAddress());
        parameters.put("家庭邮政编码",orderDTO.getHomeZipcode());
        parameters.put("家庭电话",orderDTO.getHomeZipcode());
        parameters.put("所在公司",orderDTO.getCompany());
        parameters.put("学历",orderDTO.getEducational());
        parameters.put("所在行业",orderDTO.getIndustry());

        parameters.put("工作地址",orderDTO.getOfficeAddress());
        parameters.put("工作邮编",orderDTO.getOfficeZipcode());
        parameters.put("工作联系电话",orderDTO.getOffieTel());

        parameters.put("工作邮政编码",orderDTO.getIndustry());

        parameters.put("银行",orderDTO.getBankCode());
        parameters.put("来源",orderDTO.getChannel());

        parameters.put("借款金额",orderDTO.getAmount());
        parameters.put("借款期数",orderDTO.getPeriod());
        parameters.put("月利率",orderDTO.getRate());

        parameters.put("借款用途",orderDTO.getLoanUsage());
        return parameters;
    }

    private void pushResult(String borrowNid,String status){
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("checkStatus", status);
        messageMap.put("borrowNid",borrowNid);
        new Thread(()->{
            boolean flag = true;
        	while(flag) {
            	try{
			        rabbitTemplate.send("sis.order.topic.exchange", "sis.order.confirm",new Message(JSONObject.toJSON(messageMap).toString().getBytes(), new MessageProperties()));
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
            	
    }

    @Override
    public Map<String, String> evaluate(OrderDTO orderDTO) throws Exception {

        if(blackListServiceImpl.exists(orderDTO.getIdCardNum())){

            pushResult(orderDTO.getBorrowNid(), "101");
            return buildResult(
              DecisionResultEnum.NOK,
                    "黑名单",
                    ""
            );
        }

//        if(blackListServiceImpl.existsOrderCredit(orderDTO.getIdCardNum())){
//
//            pushResult(orderDTO.getBorrowNid(), "102");
//            return buildResult(
//                    DecisionResultEnum.NOK,
//                    "已有贷款",
//                    ""
//            );
//        }


        String decisionOn = PropertyUtil.getProperty("decision.ONOFF");
        if("ON".equals(decisionOn)) {
            String api = PropertyUtil.getProperty("decision.http.api");
            String decisionName = PropertyUtil.getProperty("decision.name");

            Map<String, Object> options = new HashMap<>();
            options.put("name", decisionName);
            Map<String, Object> parameters = convertToParameters(orderDTO);

            options.put("param", parameters);

            HttpEntity<String> entity = new HttpEntity<>(JSON.toJSONString(options));
            String result = restTemplate.postForObject(api, entity, String.class);

            JSONObject js = JSON.parseObject(result);

            Map<String, String> drest = new HashMap<>();
            DecisionResultEnum dre = Enum.valueOf(DecisionResultEnum.class, js.getString("决策结果").toUpperCase());

            if (dre == DecisionResultEnum.NOK) {
                pushResult(orderDTO.getBorrowNid(), "103");
            }

            return buildResult(dre, js.getString("文字描述"), js.getString("A-Score"));
        }
        else{
            return buildResult(DecisionResultEnum.OK, "自动通过", "9999");
        }
    }

    @Override
    public String updateDecision(String name, String version, String script) throws Exception {
        DecisionScript decisionScript=new DecisionScript();
        decisionScript.setName(name);
        decisionScript.setVersion(version);
        decisionScript.setScript(script);
        crudTemplate.save(decisionScript);
        String redisKey = "SIS-DECISION-SCRIPTS:"+name+":"+"version";
        redisTemplate.set(redisKey,script);
        return  "";
    }

    @Override
    public String currentDecision(String name, String version) throws Exception {
        String redisKey = "SIS-DECISION-SCRIPTS-CURRENT:"+name;
        DecisionScript preCurrent=getCurrentDecisionEntity(name);
        preCurrent.setIsCurrent(0);

        DecisionScript newCurrent=getDecisionEntity(name,version);
        newCurrent.setIsCurrent(1);


        crudTemplate.update(
                new ArrayList<DecisionScript>(){{
                    this.add(newCurrent);
                    this.add(preCurrent);
                }},
                Criteria.from(DecisionScript.class)
                        .fields("is_current")
                        .where()
                        .and("id",Operator.EQ)
                        .endWhere()
        );

        redisTemplate.set(name,version);
        return "";
    }

    private DecisionScript getDecisionEntity(String name, String version) throws Exception {
        DecisionScript decisionScript=new DecisionScript();
        decisionScript.setName(name);
        decisionScript.setVersion(version);


        List<DecisionScript> scripts = crudTemplate.find(
                decisionScript,
                Criteria.from(DecisionScript.class)
                        .where()
                        .and("name", Operator.EQ)
                        .and("version", Operator.EQ)
                        .endWhere()
        );
        if(scripts.size()<=0){
            return null;
        }
        else{
            return scripts.get(0);
        }
    }

    public DecisionScript getCurrentDecisionEntity(String name) throws Exception {
        DecisionScript decisionScript=new DecisionScript();
        decisionScript.setName(name);
        decisionScript.setIsCurrent(1);

        List<DecisionScript> scripts = crudTemplate.find(
                decisionScript,
                Criteria.from(DecisionScript.class)
                        .fields("id","version")
                        .where()
                        .and("name", Operator.EQ)
                        .and("is_current", Operator.EQ)
                        .endWhere()
        );
        if(scripts.size()<=0){
            return null;
        }
        else{
            return scripts.get(0);
        }
    }

    @Override
    public String getCurrentDecision(String name) throws Exception {
        String redisKey = "SIS-DECISION-SCRIPTS-CURRENT:"+name;

        String version = redisTemplate.get(redisKey);

        if(!Strings.isNullOrEmpty(version)){
            return version;
        }

        DecisionScript decisionScript=getCurrentDecisionEntity(name);

        if(decisionScript == null){
            return "";
        }
        else{
            version = decisionScript.getVersion();
            redisTemplate.set(redisKey,version);
            return version;
        }
    }

    @Override
    public String getDecision(String name, String version) throws Exception {
        String redisKey = "SIS-DECISION-SCRIPTS:"+name+":"+"version";
        String tscript = redisTemplate.get(redisKey);
        if(!Strings.isNullOrEmpty(tscript)){
            return tscript;
        }
        DecisionScript script = getDecisionEntity(name,version);
        if(script == null){
            return "";
        }
        else{
            tscript = script.getScript();
            redisTemplate.set(redisKey,tscript);
            return tscript;
        }
    }

    @Override
    public List<DecisionVO> listDecision(String name) throws Exception {
        DecisionScript decisionScript=new DecisionScript();
        decisionScript.setName(name);


        List<DecisionScript> scripts = crudTemplate.find(
                decisionScript,
                Criteria.from(DecisionScript.class)
                        .fields("id","name","version","is_current")
                        .where()
                        .and("name", Operator.EQ)
                        .endWhere()
        );

        List<DecisionVO> list = new ArrayList<>();

        scripts.forEach((script)->{
            list.add(new DecisionVO(){{
                BeanUtils.copyProperties(script,this);
            }});
        });

        return list;
    }


}
