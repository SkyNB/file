import com.tasfe.sis.bank.api.BankContract;
import com.tasfe.sis.bank.api.dto.BatchTransferInfosDTO;
import com.tasfe.sis.bank.service.BankService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;

/**
 * Created by lixiaofeng on 2017-8-15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring-bank-test-application.xml"})
public class BrankPayment {

    @Autowired
    private BankContract bankContract;

    @Test
    public void bankPayment() throws Exception {
        BatchTransferInfosDTO batchTransferInfosDTO = new BatchTransferInfosDTO();
        //接口名称
        batchTransferInfosDTO.setService("bankPayment");
        //接口版本
        batchTransferInfosDTO.setVersion("v1.0");
        //合作者身份ID
        batchTransferInfosDTO.setService("200000030006");
        //接口名称
        batchTransferInfosDTO.set_input_charset("UTF-8");
        //签名
        batchTransferInfosDTO.setSign("e8qdwl9caset5zugii2r7q0k8ikopxor");
        batchTransferInfosDTO.setSign_type("MD5");//签名方式
        batchTransferInfosDTO.setSign_key("bankPayment");//该key由浏览器证书持有,不需要手动设置该值
        batchTransferInfosDTO.setReturn_url("XXX.jsp");//返回页面路径
        batchTransferInfosDTO.setMemo("支付接口调用");//备注
        batchTransferInfosDTO.setBatch_no("20131105154925");//商户网站请求号
        batchTransferInfosDTO.setTransfer_num("123");//转账笔数
        batchTransferInfosDTO.setTransfer_amount("100000000");//转账总金额
        batchTransferInfosDTO.setOperator_id("10001");//操作员Id
        batchTransferInfosDTO.setIdentity_no("123");//会员标识号
        batchTransferInfosDTO.setIdentity_type("1");//标识类型
        batchTransferInfosDTO.setNotify_url("yyy.jsp");//服务器异步通知页面路径
        batchTransferInfosDTO.setName("lixiaofeng");//姓名
        batchTransferInfosDTO.setCard_no("6222xxxx");//银行卡号
        batchTransferInfosDTO.setBank("ICBC");//银行表示 如工商银行：ICBC
        batchTransferInfosDTO.setBank_name("工商银行");//银行名称
        batchTransferInfosDTO.setBranch_name("浦东支行");//支行名称
        batchTransferInfosDTO.setBank_prov("上海");//省
        batchTransferInfosDTO.setBank_city("上海");//市
        batchTransferInfosDTO.setCompany_or_personal("C");//对公对私：对公/B, 对私/C
        batchTransferInfosDTO.setFound_no("bankPayment");//

        bankContract.bankPayment(new ArrayList<BatchTransferInfosDTO>() {{
            this.add(batchTransferInfosDTO);
        }});

    }


}
