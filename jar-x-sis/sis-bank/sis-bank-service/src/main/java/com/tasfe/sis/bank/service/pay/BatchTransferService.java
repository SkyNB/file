package com.tasfe.sis.bank.service.pay;

import com.tasfe.sis.bank.api.PayContract;
import com.tasfe.sis.bank.model.BatchTransferInfos;
import com.tasfe.sis.bank.service.pay.util.Core;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by Lait on 2017/8/8.
 */
@Service
public class BatchTransferService implements PayContract {


    @Override
    public void batchPay() {

    }

    /**
     * 转账付款信息加密
     * @param batchTransferInfos
     * @return
     * @throws Exception
     */
    public Map<String, String> transferPayInfosEncrypt(BatchTransferInfos batchTransferInfos) throws Exception {
        String charset = batchTransferInfos.get_input_charset();
        // 该值由浏览器证书追加
        String signKey = batchTransferInfos.getSign_key();
        String signType = batchTransferInfos.getSign_type();
        //转账列表加密
        try{
            String transferList = Core.encryptData(batchTransferInfos.getTransfer_list(),charset);
            batchTransferInfos.setTransfer_list(transferList);
            // 转map
            Map<String,String> transferParams = BeanUtils.describe(batchTransferInfos);
            //参数加密
            Map<String, String> map = Core.buildRequestPara(transferParams,signType,signKey,charset);
            return map;
        }
        catch (Throwable e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    }

}
