package com.tasfe.sis.bank.api;


import com.tasfe.sis.bank.api.dto.BatchTransferInfosDTO;

import java.util.List;
import java.util.Map;

/**
 * Created by hefusang on 2017/8/10.
 */
public interface BankContract {
    /**
     * 支付接口调用
     * @param batchTransferInfosDTO 输入参数
     * @return
     * @throws Exception
     */
    public Map<String, String> bankPayment(List<BatchTransferInfosDTO> batchTransferInfosDTOList) throws Exception;

    /**
     *
     */
    public Long getBankIdsWithBankCode(String code) throws Exception;


}
