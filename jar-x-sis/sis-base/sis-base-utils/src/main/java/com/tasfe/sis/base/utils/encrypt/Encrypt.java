package com.tasfe.sis.base.utils.encrypt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tasfe.sis.base.utils.PropertyUtil;
import com.tasfe.sis.base.utils.encrypt.utils.CoderUtil;
import com.tasfe.sis.base.utils.encrypt.utils.RSACoderUtil;
import com.tasfe.sis.base.utils.encrypt.utils.SecretUtil;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dongruixi on 2017/12/8.
 */
public class Encrypt extends RestTemplate {

    private static String SECRET = PropertyUtil.getProperty("http.api.secret");
    private static String RISK_PUB_KEY = PropertyUtil.getProperty("http.api.risk_pub_key");
    private static String FRONT_PRI_KEY = PropertyUtil.getProperty("http.api.front_pri_key");

    public static Map<String,String> encoderObject(Object in) throws Exception {
        //1.构建请求参数
        String json = JSON.toJSONString(in);

        //System.out.println("加密前明文:" + json.toJSONString());

        //2.生成对称秘钥（可以随机生成，也可以用常量）
        String secret = SECRET;

        //3.对请求参数对称加密
        String bizRequest = SecretUtil.encryptMode(json, secret);

        System.out.println("加密后密文:" + bizRequest);

        //4.对刚刚生成的对称秘钥进行非对称加密（秘钥为星辰的公钥）
        byte[] b = RSACoderUtil.encryptByPublicKey(secret.getBytes(),RISK_PUB_KEY);
        secret = CoderUtil.encryptBASE64(b);

        //5.对bizRequest加签
        String sign =  RSACoderUtil.sign(bizRequest.getBytes(),FRONT_PRI_KEY);

        //6.构建http请求参数
        Map<String,String> param = new HashMap<String,String>();
        param.put("bizRequest", bizRequest);
        param.put("secret", secret);
        param.put("sign", sign);

        return param;
    }


    public static Map<String,String> encoder(Map<String,String> in) throws Exception {
        return encoderObject(in);
    }

    public static Map<String,String> encoder(Serializable in) throws Exception {
        return encoderObject(in);
    }

    //响应过程的解密、验签过程
    public static  <T> T decoder(String bizResponse, Class<T> clz) throws Exception {

            //模拟返回数据


            //2.非对称解密bizResponse(加密数据为商户的公钥，所以解密秘钥为商户的私钥,由星辰生成商户时分配)
            String merchantPriKey = FRONT_PRI_KEY;

            bizResponse = new String(RSACoderUtil.decryptByPrivateKey(CoderUtil.decryptBASE64(bizResponse),merchantPriKey));

            return JSON.parseObject(bizResponse,clz);


    }
}
