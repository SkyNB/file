package com.tasfe.sis.application.web.controller;

import com.alibaba.fastjson.JSON;
import com.tasfe.sis.application.service.dto.ResponseDTO;
import com.tasfe.sis.application.web.controller.dto.ContractInfo;
import com.tasfe.sis.base.model.response.ResponseData;
import com.tasfe.sis.base.service.cache.redis.RedisTemplate;
import com.tasfe.sis.base.utils.PropertyUtil;
import com.tasfe.sis.base.utils.encrypt.Encrypt;
import org.apache.commons.lang.enums.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by dongruixi on 2017/12/23.
 */
@RestController
@RequestMapping(value = "/contract", method = RequestMethod.GET)
public class ContractController {

    enum SignCode{
        电子签名授权书(1),征信查询授权书(2),自动还款服务协议(3),贷款用途承诺书(4),贷款协议(5),贷款凭证(6),Unknown(-1);

        public static SignCode valueOf(int value){
            switch (value) {
                case 1:
                    return 电子签名授权书;
                case 2:
                    return 征信查询授权书;
                case 3:
                    return 自动还款服务协议;
                case 4:
                    return 贷款用途承诺书;
                case 5:
                    return 贷款协议;
                case 6:
                    return 贷款凭证;
                default:
                    return Unknown;
            }
        }

        private int nCode;
        private SignCode(int code){
            this.nCode = code;
        }

    };

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping("get")
    public ResponseData<List<ContractInfo>> getContract(@RequestParam String orderNo) throws Exception {

        List<ContractInfo> list=new ArrayList<>();

        Map<String,String> mParam=new HashMap<String,String>(){{
            this.put("orderNo",orderNo);
        }};

        Map<String,String> param = Encrypt.encoder(mParam);
        ResponseEntity<ResponseDTO> res = restTemplate.postForEntity(
                PropertyUtil.getProperty("http.api.get_contract")
                ,new HttpEntity<>(param)
                , ResponseDTO.class);

        String response = res.getBody().getBizResponse();
        if (response!=null) {
	        String cList = Encrypt.decoder(response,String.class);
	
	        list.addAll(JSON.parseArray(cList,ContractInfo.class));
	
	        list.stream().forEach((i)->{
	            SignCode code = SignCode.valueOf(Integer.parseInt(i.getSignCode()));
	            i.setDescription(code.name());
	        });
        } 
        return new ResponseData<List<ContractInfo>>().setCode("").setData(list);
    }
}
