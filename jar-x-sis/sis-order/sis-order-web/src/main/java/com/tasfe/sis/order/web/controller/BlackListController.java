package com.tasfe.sis.order.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tasfe.sis.base.model.response.ResponseData;
import com.tasfe.sis.order.entity.BlackList;
import com.tasfe.sis.order.service.BlackListService;

/**
 * 黑名单
 */
@RestController
@RequestMapping("/blacklist")
public class BlackListController {
	@Autowired
	private BlackListService blackListService;

	/**
	 * 黑名单列表
	 * 
	 * @throws Exception
	 */
	@RequestMapping("/list")
	public ResponseData<List<BlackList>> list(@RequestParam String idCardNum) throws Exception {
		// 查询列表数据
		List<BlackList> jobList = blackListService.queryList(idCardNum);
		return new ResponseData<List<BlackList>>() {
			{
				setCode("0").setData(jobList);
			}
		};
	}
}
