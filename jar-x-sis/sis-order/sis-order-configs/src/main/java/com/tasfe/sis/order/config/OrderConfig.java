package com.tasfe.sis.order.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 直接走配置中心，目前先写死，便于测试
 */
@Component
public class OrderConfig {

	@Value("${credit.system.url}")
    public String CRAWLER_HTTPADDRESS = "http://11.1.2.5:8080/cmis";
    
	@Value("${credit.rhzx.url}")
    public String PCREDIT_HTTPADDRESS = "http://9.116.46.114";
    
	@Value("${credit.system.temppath}")
	public String ORDERTRANSACTION_TEMPFILEPATH = "/var/logs/";
    
	@Value("${credit.system.fileext}")
    public String ORDERTRANSACTION_FILEEXT = ".jst";
    
	@Value("${credit.rhzx.path}")
    public String PCREDITTRANSACTION_FILEPATH ="/opt/credit/report/";
	
	@Value("${credit.rhzx.httpurl}")
    public String PCREDITTRANSACTION_URL ="http://11.52.1.6/";

	@Value("${credit.query.idcard}")
    public String PCREDITSEARCH_IDCARD="0";
    
	@Value("${credit.query.reson}")
    public String PCREDITSEARCH_REASON="02";
    
	@Value("${credit.system.teller}")
	public String CREDITSYSTEM_TELLER="024173";
	
	@Value("${credit.system.deptCode}")
	public String CREDITSYSTEM_DEPTCODE="096700";
    /**
     * password 是一个md5再次大写后，又一次的md5再次大写
     */
	@Value("${credit.system.password}")
	public String CREDITSYSTEM_PASSWORD="4C5FF58924D1E5778FECB25A2DF2DD85";
	
	@Value("${blacklist.notify.url}")
	public String BLACKLIST_NOTIFY_URL="http://localhost:8282/blacklist/push";
	
	@Value("${push.loan.url}")
	public String PUSH_LOAN_URL="http://localhost:8282/sync/pushLoans";
	
	@Value("${push.repayment.url}")
	public String PUSH_REPAYMENT_URL="http://localhost:8282/sync/pushRepayment";
}
