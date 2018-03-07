package com.tasfe.sis.bank.service.impls;

import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import com.tasfe.sis.bank.api.BankContract;
import com.tasfe.sis.bank.api.dto.BatchTransferInfosDTO;
import com.tasfe.sis.bank.entity.Bank;
import com.tasfe.sis.bank.model.BatchTransferInfos;
import com.tasfe.sis.bank.model.transfer.TransferCollection;
import com.tasfe.sis.bank.model.transfer.TransferInfo;
import com.tasfe.sis.bank.service.BankService;
import com.tasfe.sis.bank.service.pay.BatchTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by hefusang on 2017/8/8.
 */
@Service
public class BankServiceImpl implements BankService ,BankContract {

    @Autowired
    private CrudTemplate crudTemplate;

    @Autowired
    private BatchTransferService batchTransferService;
    /************************  对内 *****************************/
    /**
     *  支付接口调用实现
     * @param batchTransferInfosDTOList 输入参数
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, String> bankPayment(List<BatchTransferInfosDTO> batchTransferInfosDTOList) throws Exception{
        //组装branch_list
        TransferCollection transferCollection = new TransferCollection();
        batchTransferInfosDTOList.forEach((batchTransferInfosDTO)->{
            TransferInfo transferInfo = new TransferInfo();
            transferInfo.setBatch_no(batchTransferInfosDTO.getBatch_no());
            transferInfo.setName(batchTransferInfosDTO.getName());
            transferInfo.setCard_no(batchTransferInfosDTO.getCard_no());
            transferInfo.setBank(batchTransferInfosDTO.getBank());
            transferInfo.setBank_name(batchTransferInfosDTO.getBank_name());
            transferInfo.setBranch_name(batchTransferInfosDTO.getBranch_name());
            transferInfo.setBank_prov(batchTransferInfosDTO.getBank_prov());
            transferInfo.setBank_city(batchTransferInfosDTO.getBank_city());
            transferInfo.setCompany_or_personal(batchTransferInfosDTO.getCompany_or_personal());
            transferInfo.setFound_no(batchTransferInfosDTO.getFound_no());
            transferCollection.add(transferInfo);
        });
        String branch_list = transferCollection.toString();
        //复制对象
        BatchTransferInfos batchTransferInfos = new BatchTransferInfos();
        batchTransferInfos.setTransfer_list(branch_list);
        Map<String, String> map = batchTransferService.transferPayInfosEncrypt(batchTransferInfos);
        return map;
    }

    @Override
    public Long getBankIdsWithBankCode(String code) throws Exception {
        Bank record = new Bank();
        record.setCode(code);
        List<Bank> banks = crudTemplate.find(record, Criteria.from(Bank.class));
        if(banks.size() > 0)
            return banks.get(0).getId();
        return null;
    }


    /************************  对内 *****************************/
    @Override
    public Bank getBankWithCode(String code) {
        Bank bank = new Bank();
        bank.setCode(code);
        try {
            List<Bank> banks = crudTemplate.find(bank,Criteria.from(Bank.class).where().and("code", Operator.EQ).endWhere());
            if(banks.size() > 0)
                return banks.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
