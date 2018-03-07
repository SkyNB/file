package com.tasfe.sis.bank.web.controller.pay;

import com.tasfe.sis.bank.config.TransferPayConfig;
import com.tasfe.sis.bank.model.BatchTransferInfos;
import com.tasfe.sis.bank.model.BatchTransferNotifyInfos;
import com.tasfe.sis.bank.model.NotifyMessage;
import com.tasfe.sis.bank.service.pay.BatchTransferNotifyService;
import com.tasfe.sis.bank.service.pay.BatchTransferService;
import com.tasfe.sis.bank.service.pay.util.HttpClientUtilsExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by Lait on 2017/8/8.
 */
@RestController
@RequestMapping("btp")
public class BatchTransferController {

    @Autowired
    private BatchTransferService batchTransferService;

    @Autowired
    private BatchTransferNotifyService batchTransferNotifyService;

    /**
     * 批量转账
     * 统一抛出所有异常，统一异常处理
     * @param httpServletRequest
     * @param batchTransferInfos
     * @throws Exception
     */
    @RequestMapping("/payUrl")
    public void batchTransferPay(HttpServletRequest httpServletRequest,BatchTransferInfos batchTransferInfos) throws Exception {

        Map<String, String> encryptParams = batchTransferService.transferPayInfosEncrypt(batchTransferInfos);
        // https/http调用支付请求
        HttpClientUtilsExt.post(TransferPayConfig.gatewayUrl,encryptParams);

    }

    /**
     * 获取转款后的信息
     * @param httpServletRequest
     * @param batchTransferNotifyInfos
     * @throws Exception
     */
    @RequestMapping("/notifyUrl")
    public NotifyMessage getNotifyMessage(HttpServletRequest httpServletRequest,BatchTransferNotifyInfos batchTransferNotifyInfos) throws Exception {
        NotifyMessage notifyMessage = batchTransferNotifyService.getNotifyMessage(batchTransferNotifyInfos);
        return notifyMessage;
    }
}
