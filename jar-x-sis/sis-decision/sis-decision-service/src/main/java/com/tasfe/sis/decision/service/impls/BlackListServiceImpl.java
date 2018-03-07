package com.tasfe.sis.decision.service.impls;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import com.tasfe.sis.base.service.cache.redis.RedisTemplate;
import com.tasfe.sis.base.utils.PropertyUtil;
import com.tasfe.sis.base.utils.encrypt.Encrypt;
import com.tasfe.sis.base.utils.encrypt.MD5Util;
import com.tasfe.sis.decision.entity.BlackList;
import com.tasfe.sis.decision.entity.OrderCredit;
import com.tasfe.sis.decision.model.BlackListToExportVO;
import com.tasfe.sis.decision.model.BlackListVO;
import com.tasfe.sis.decision.service.BlackListService;
import com.tasfe.sis.decision.service.dto.ResponseDTO;

/**
 * Created by dongruixi on 2017/12/6.
 */
@Service
public class BlackListServiceImpl implements BlackListService {

    Logger logger= LoggerFactory.getLogger(BlackListServiceImpl.class);

    @Autowired
    CrudTemplate crudTemplate;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RestTemplate restTemplate;

    @Override
    public Boolean exists(String idCardNum) throws Exception {
        idCardNum = MD5Util.encode(idCardNum);
        BlackList blackList = new BlackList();
        blackList.setIdCardNum(idCardNum);
        Long count = crudTemplate.count(blackList,
                Criteria.from(BlackList.class)
                        .where()
                        .and("match_sign", Operator.EQ)
                        .endWhere());

        return count>0;
    }

    @Override
    public Boolean existsOrderCredit(String idCardNum) throws Exception{
        idCardNum = MD5Util.encode(idCardNum);

        OrderCredit orderCredit=new OrderCredit();
        orderCredit.setMatchSign(idCardNum);

        Long count = crudTemplate.count(orderCredit,
                Criteria.from(OrderCredit.class)
                        .where()
                        .and("match_sign", Operator.EQ)
                        .endWhere());

        return count>0;
    }

    @Override
    public List<BlackListToExportVO> getBackListToExport(Long aftTimeStamp) throws Exception {

        List<BlackListToExportVO> listVO = new ArrayList<>();
        getBackList(aftTimeStamp,(backListItems)->{
            backListItems.forEach((source)->{
                listVO.add(new BlackListToExportVO(){{
                    BeanUtils.copyProperties(source,this);
                }});
            });
        });

        return listVO;
    }

    @Override
    public List<BlackListVO> getBackList(Long aftTimeStamp) throws Exception {
        List<BlackListVO> listVO = new ArrayList<>();
        getBackList(aftTimeStamp,(backListItems)->{
            backListItems.forEach((source)->{
                listVO.add(new BlackListVO(){{
                    BeanUtils.copyProperties(source,this);
                }});
            });
        });

        return listVO;
    }


    private void getBackList(Long aftTimeStamp,Consumer<List<BlackList>> action) throws Exception {
        BlackList blackList = new BlackList();

        List<BlackList> backListItems  = crudTemplate.find(blackList,
                Criteria.from(BlackList.class)
                        .where()
                        .and("update_time", Operator.GT,new java.sql.Timestamp(aftTimeStamp))
                        .endWhere());

        action.accept(backListItems);
    }

	@Override
	public boolean save(BlackList bo) throws Exception {
		try {
			crudTemplate.save(bo);
		}catch(SQLException e) {
			return false;
		}
		return true;
	}
    /**
     * 推送黑名单
     * @return
     * @throws Exception
     */
    @Override
    public Boolean pushBlackList() throws Exception {
        Calendar now =Calendar.getInstance();
        now.add(Calendar.DATE,-1);

        final String lock="SIS-PUSH-BLACKLIST-LOCK";
        if(redisTemplate.setNX(lock,String.valueOf(new Date().getTime()))==1){
            try{
                List<BlackListToExportVO> ll = getBackListToExport(now.getTime().getTime());
                List<String> bl= ll.stream().map((vo)->{
                    return vo.getMatchSign();
                }).collect(Collectors.toList());

                ResponseEntity<String> dto = restTemplate.postForEntity(
                        PropertyUtil.getProperty("http.pushBlackList.api"),
                        new HttpEntity<>(Encrypt.encoderObject(bl)),
                        String.class
                );

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
}
