package com.tasfe.sis.bank.service.pay;

import com.tasfe.sis.bank.model.BatchTransferNotifyInfos;
import com.tasfe.sis.bank.model.NotifyMessage;
import com.tasfe.sis.bank.model.TradeStatus;
import com.tasfe.sis.bank.model.VerifyResult;
import com.tasfe.sis.bank.service.pay.util.VerifyClient;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by Lait on 2017/8/8.
 */
@Service
public class BatchTransferNotifyService {


    /**
     * 转账后通知信息
     * @param batchTransferNotifyInfos
     * @return
     * @throws Exception
     */
    public NotifyMessage getNotifyMessage(BatchTransferNotifyInfos batchTransferNotifyInfos) throws Exception {
        Map<String,String> btn = BeanUtils.describe(batchTransferNotifyInfos);
        //签名验证
        VerifyResult result = VerifyClient.verifyBasic(batchTransferNotifyInfos.get_input_charset(),btn);
        if (result.isSuccess()) {
            //out.println("签名验证成功！");
            String tradeStatus = batchTransferNotifyInfos.getWithdrawal_status();

            if (TradeStatus.WITHDRAWAL_SUBMITTED.equals(tradeStatus)) {
                //out.println("已提交银行！");
                return new NotifyMessage(TradeStatus.WITHDRAWAL_SUBMITTED.toString(), TradeStatus.WITHDRAWAL_SUBMITTED.getDescr());
            } else if (TradeStatus.WITHDRAWAL_SUCCESS.equals(tradeStatus)) {
                //out.println("转账成功！");
                return new NotifyMessage(TradeStatus.WITHDRAWAL_SUCCESS.toString(), TradeStatus.WITHDRAWAL_SUCCESS.getDescr());
            } else if (TradeStatus.WITHDRAWAL_FAIL.equals(tradeStatus)) {
                //out.println("转账失败！");
                return new NotifyMessage(TradeStatus.WITHDRAWAL_FAIL.toString(), TradeStatus.WITHDRAWAL_FAIL.getDescr());
            } else if (TradeStatus.RETURN_TICKET.equals(tradeStatus)) {
                //out.println("转账退票！");
                return new NotifyMessage(TradeStatus.RETURN_TICKET.toString(), TradeStatus.RETURN_TICKET.getDescr());
            }
        } else {
            // 签名验证失败
            return new NotifyMessage(TradeStatus.VERIFY_FAILURE.toString(), TradeStatus.VERIFY_FAILURE.getDescr());
        }
        return null;
    }

}
