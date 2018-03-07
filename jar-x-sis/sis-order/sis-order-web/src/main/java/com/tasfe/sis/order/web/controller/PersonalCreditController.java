package com.tasfe.sis.order.web.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tasfe.sis.base.model.response.ResponseData;
import com.tasfe.sis.order.entity.PersonalCredit;
import com.tasfe.sis.order.model.CreditAccountAddDTO;
import com.tasfe.sis.order.model.CreditAccountDelDTO;
import com.tasfe.sis.order.model.QueryOrderItemDTO;
import com.tasfe.sis.order.service.PersonalCreditService;

/**
 * 授权用户
 */
@RestController
@RequestMapping("/personalcredit")
public class PersonalCreditController {
	@Autowired
	private PersonalCreditService personalCreditService;

	/**
	 * 授权用户列表
	 * 
	 * @throws Exception
	 */
	@RequestMapping("/list")
	public ResponseData<List<PersonalCredit>> list(@RequestParam String idCardNum) throws Exception {
		// 查询列表数据
		List<PersonalCredit> jobList = personalCreditService.queryList(idCardNum);
		return new ResponseData<List<PersonalCredit>>() {
			{
				setCode("0").setData(jobList);
			}
		};
	}
	
	
	@RequestMapping(value = "/addCreditAccount", method = RequestMethod.POST, produces = "application/json")
    public ResponseData<Void> addCreditAccount(@RequestBody CreditAccountAddDTO dto) throws Exception {
		ResponseData responseData = new ResponseData();
		personalCreditService.addCreditAccount(dto);
		responseData.setStatus("1");
		return responseData;
	}
	
	@RequestMapping(value = "/queryCreditAccount", method = RequestMethod.POST, produces = "application/json")
    public ResponseData<Void> queryCreditAccount() throws Exception {
		ResponseData responseData = new ResponseData();
		personalCreditService.queryAllCreditAccount();
		return responseData;
	}
	
	@RequestMapping(value = "/deleteCreditAccount", method = RequestMethod.POST, produces = "application/json")
    public ResponseData<Void> deleteCreditAccount(@RequestBody CreditAccountDelDTO dto) throws Exception {
		ResponseData responseData = new ResponseData();
		personalCreditService.deleteCreditAccount(dto);
		return responseData;
	}
	
	
}
