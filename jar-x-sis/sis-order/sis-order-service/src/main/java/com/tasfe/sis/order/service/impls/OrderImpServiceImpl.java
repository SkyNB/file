package com.tasfe.sis.order.service.impls;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mobanker.data.model.creditResponse.BlackUser;
import com.mobanker.data.model.creditResponse.BussinessInfo;
import com.mobanker.data.model.creditResponse.CreditClient;
import com.mobanker.data.model.creditResponse.CreditResponse;
import com.mobanker.data.model.creditResponse.InterestDetail;
import com.mobanker.data.model.creditResponse.InterestOffInfo;
import com.mobanker.data.model.creditResponse.PayDetail;
import com.mobanker.data.model.creditResponse.PayOffInfo;
import com.mobanker.data.service.CrccUntil;
import com.mobanker.data.service.CreditUntil;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import com.tasfe.sis.base.service.cache.redis.RedisTemplate;
import com.tasfe.sis.base.utils.date.DateUtil;
import com.tasfe.sis.base.utils.encrypt.MD5Util;
import com.tasfe.sis.base.utils.http.HttpClientUtil;
import com.tasfe.sis.order.config.OrderConfig;
import com.tasfe.sis.order.entity.BlackList;
import com.tasfe.sis.order.entity.CreditAccount;
import com.tasfe.sis.order.entity.Order;
import com.tasfe.sis.order.entity.PersonalCredit;
import com.tasfe.sis.order.entity.RepayInfo;
import com.tasfe.sis.order.entity.RepayPlan;
import com.tasfe.sis.order.entity.RequestLog;
import com.tasfe.sis.order.enums.RepayOrderEnum;
import com.tasfe.sis.order.handle.AdapterHandler;
import com.tasfe.sis.order.model.vo.BlackListVO;
import com.tasfe.sis.order.model.vo.OrderVO;
import com.tasfe.sis.order.service.BlackListService;
import com.tasfe.sis.order.service.CreditAccountService;
import com.tasfe.sis.order.service.OrderImpService;
import com.tasfe.sis.order.service.PersonalCreditService;
import com.tasfe.sis.order.service.RepayInfoService;
import com.tasfe.sis.order.service.RepayPlanService;
import com.tasfe.sis.order.service.RequestLogService;
import com.tasfe.sis.order.util.FileUtil;

/**
 * Created by Lait on 2017/7/27.
 */
@Service
public class OrderImpServiceImpl implements AdapterHandler<OrderVO>, OrderImpService {

	private static Logger logger = LoggerFactory.getLogger(OrderImpServiceImpl.class);

	@Autowired
	private CrudTemplate crudTemplate;

	@Autowired
	private RepayInfoService repayInfoService;

	@Autowired
	private RepayPlanService repayPlanService;

	@Autowired
	private BlackListService blackListService;

	@Autowired
	private PersonalCreditService personalCreditService;

	@Autowired
	private RequestLogService requestLogService;

	@Autowired
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	@Autowired
	private ThreadPoolTaskExecutor taskPoolExecutor;

	@Autowired
	private CreditAccountService creditAccountService;

	@Autowired
	RedisTemplate redisTemplate;

	private String REQUEST_EXPORT_TIME = "EXPORT_FILE";

	@Autowired
	private OrderConfig config;

	/**
	 * 此方法是外前置系统导入处理 必须继承AdapterHandler接口， 必须标注泛型<对象> 传入参数为List<?>, 可强制转换成BillVO。
	 */
	@Override
	public boolean handle(OrderVO vo) throws Exception {
		Order order = new Order();
		order.setBorrowNid(vo.getBorrowNid());
		List<Order> orderlst = null;
		try {
			orderlst = crudTemplate.find(order,
					Criteria.from(Order.class).where().and("borrow_nid", Operator.EQ).endWhere());
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		if (orderlst != null && orderlst.size() > 0) {
			order = orderlst.get(0);
			List<RepayPlan> planTarget = vo.getRepayPlans();
			List<RepayPlan> planSource = null;
			try {
				planSource = repayPlanService.queryRepayPlan(order.getId());
			} catch (Exception e) {
				logger.debug(e.getMessage());
			}
			Map<String, Object> resultMap = comparatOutPlan(order.getId(), order.getBorrowNid(), planSource,
					planTarget);
			List<Long> delList = (List<Long>) resultMap.get("del");
			List<RepayPlan> addList = (List<RepayPlan>) resultMap.get("add");
			if (delList.size() > 0) {
				try {
					Long[] ids = delList.toArray(new Long[delList.size()]);
					crudTemplate.del(RepayPlan.class, ids);
				} catch (Exception e) {
					logger.debug(e.getMessage());
				}
			}
			if (addList.size() > 0) {
				try {
					crudTemplate.save(addList);
				} catch (Exception e) {
					logger.debug(e.getMessage());
				}
			}

			List<RepayInfo> payTarget = vo.getRepayInfos();
			List<RepayInfo> paySource = null;
			try {
				paySource = repayInfoService.queryRepayInfo(order.getId());
			} catch (Exception e) {
				logger.debug(e.getMessage());
			}
			Map<String, Object> repayMap = comparatOutPay(order.getId(), order.getBorrowNid(), paySource, payTarget);
			List<Long> delPayList = (List<Long>) repayMap.get("del");
			List<RepayInfo> addPayList = (List<RepayInfo>) repayMap.get("add");
			if (delPayList.size() > 0) {
				try {
					Long[] ids = delPayList.toArray(new Long[delList.size()]);
					crudTemplate.del(RepayInfo.class, ids);
				} catch (Exception e) {
					logger.debug(e.getMessage());
				}
			}
			if (addPayList.size() > 0) {
				try {
					crudTemplate.save(addPayList);
				} catch (Exception e) {
					logger.debug(e.getMessage());
				}
			}

			if (order != null) {
				try {
					order.setContractStatus(vo.getContractStatus());
					order.setBirthday(DateUtil.getDatetoSDate(vo.getBirthday()));
					order.setCheckTime(DateUtil.getDatetoSDate(vo.getCheckTime()));
					order.setLoanDay(DateUtil.getDatetoSDate(vo.getLoanDay()));
					order.setUtime(null);
					crudTemplate.update(order, Criteria.from(Order.class));
				} catch (Exception e) {
					logger.debug(e.getMessage());
				}
			}
		}
		return true;
	}

	/**
	 * 外前置导入文件
	 */
	@Override
	public boolean importOutFile(File file) throws Exception {
		FileUtil.outFileToHandle(file, OrderVO.class);
		FileUtil.deleteFile(file);
		String today = DateUtil.getToDay();
		HttpClientUtil.doGet(config.BLACKLIST_NOTIFY_URL);
		HttpClientUtil.doGet(config.PUSH_LOAN_URL + today);
		HttpClientUtil.doGet(config.PUSH_REPAYMENT_URL + today);
		return true;
	}

	@Override
	public File downInnerFile(String startDate, String endDate) throws Exception {
		File saveFile = FileUtil.generateFile(config.ORDERTRANSACTION_TEMPFILEPATH, endDate.concat("_IN"),
				config.ORDERTRANSACTION_FILEEXT);
		if (saveFile.exists()) {
			FileUtil.deleteFile(saveFile);
		}
		FileUtil.openFile(saveFile);
		Order order = new Order();
		OrderVO transVo = null;
		Date startsDate = DateUtil.getStrtoDate(startDate);
		Date endsDate = DateUtil.getStrtoDate(endDate);
		Date enddDate = DateUtil.getDateAfter(endsDate, 1);
		List<Order> orders = crudTemplate.find(order,
				Criteria.from(Order.class).where().and("utime", Operator.GT, new java.sql.Date(startsDate.getTime()))
						.and("utime", Operator.LT, new java.sql.Date(enddDate.getTime())).endWhere());
		for (Order orderItem : orders) {
			transVo = new OrderVO();
			try {
				if (orderItem.getLoanDay() == null)
					continue;
				List<RepayPlan> planLst = repayPlanService.queryRepayPlan(orderItem.getId());
				List<RepayInfo> payLst = repayInfoService.queryRepayInfo(orderItem.getId());
				transVo.setBorrowNid(orderItem.getBorrowNid());
				transVo.setRepayInfos(payLst);
				transVo.setRepayPlans(planLst);
				transVo.setContractStatus(orderItem.getContractStatus());
				transVo.setBirthday(DateUtil.getDatetoSDate(orderItem.getBirthday()));
				transVo.setCheckTime(DateUtil.getDatetoSDate(orderItem.getCheckTime()));
				transVo.setLoanDay(DateUtil.getDatetoSDate(orderItem.getLoanDay()));
				FileUtil.writeFile(saveFile, transVo);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		List<BlackList> blist = blackListService.queryListByCTime(startsDate, endsDate);
		BlackListVO bvo = null;
		for (BlackList black : blist) {
			bvo = new BlackListVO();
			BeanUtils.copyProperties(black, bvo);
			FileUtil.writeFile(saveFile, bvo);
		}
		return saveFile;
	}

	/**
	 * 内前置导出文件
	 */
	@Override
	public boolean innerFileReptile(String startDate) throws Exception {
		Date date = new Date();
		RequestLog requestLog = requestLogService.queryRequestLog(REQUEST_EXPORT_TIME);
		String toDay = null;
		String endDt = DateUtil.getToDay();
		if (startDate == null) {
			toDay = DateUtil.getTimestamptoStr(requestLog.getUtime());
		} else {
			toDay = startDate;
		}
		String password = MD5Util.EncryptTeller(config.CREDITSYSTEM_PASSWORD);
		logger.error("=---------------------跑批开始！-----------------------=");
		taskPoolExecutor.execute(new InnerOrderBuildFile(config.CRAWLER_HTTPADDRESS, toDay, endDt,
				config.CREDITSYSTEM_TELLER, password));
		taskPoolExecutor.execute(
				new BlackBuildFile(config.CRAWLER_HTTPADDRESS, toDay, endDt, config.CREDITSYSTEM_TELLER, password));
		// taskPoolExecutor.execute(new
		// PersonalCreditBuildFile(CRAWLER_HTTPADDRESS,toDay,endDt,CREDITSYSTEM_TELLER,password));

		requestLogService.updateTime(REQUEST_EXPORT_TIME, date);

		return true;
	}

	private static Map<String, InterestDetail> convertInterDetail(List<InterestDetail> planDtlTargets) {
		Map<String, InterestDetail> maps = Maps.newHashMap();
		if (!planDtlTargets.isEmpty()) {
			for (InterestDetail dtlInfo : planDtlTargets) {
				String dateStr = DateUtil.getStrtoDateStr(dtlInfo.getPayDate());
				maps.put(dateStr, dtlInfo);
			}
		}
		return maps;
	}

	private static Map<String, RepayInfo> convertRepayInfo(List<RepayInfo> targets) {
		Map<String, RepayInfo> maps = Maps.newHashMap();
		for (RepayInfo pay : targets) {
			String dateStr = DateUtil.getTimestamptoStr(pay.getRepayDate());
			maps.put(dateStr, pay);
		}
		return maps;
	}

	private static Map<String, Object> comparatorPay(Long orderId, String borrowNid, List<RepayInfo> sources,
			List<PayDetail> target, List<InterestDetail> payDtlTargets) {
		List<Long> delList = Lists.newArrayList();
		List<RepayInfo> addList = Lists.newArrayList();
		boolean isSearch = false;
		Map<Long, Integer> flagMap = Maps.newHashMap();

		Map<String, InterestDetail> interTargets = convertInterDetail(payDtlTargets);
		Map<String, RepayInfo> paySources = convertRepayInfo(sources);

		for (Iterator<PayDetail> iss = target.iterator(); iss.hasNext();) {
			PayDetail offDetail = (PayDetail) iss.next();
			isSearch = false;
			String tarDate = DateUtil.getStrtoDateStr(offDetail.getPayDate());
			RepayInfo repay = paySources.get(tarDate);
			InterestDetail interDtl = interTargets.get(tarDate);
			BigDecimal interAmt = new BigDecimal(0);
			if (interDtl != null)
				interAmt = new BigDecimal(interDtl.getPayAmt());
			if (repay != null) {
				BigDecimal totalAmt = new BigDecimal(offDetail.getPayAmt()).add(interAmt);
				if (repay.getRepayAmount().compareTo(totalAmt) == 0) {
					isSearch = true;
					flagMap.put(repay.getId(), 1);
					continue;
				}
			}
			if (!isSearch) {
				RepayInfo newInfo = convertToRepay(orderId, borrowNid, offDetail, interAmt);
				addList.add(newInfo);
			}
		}
		for (Iterator<RepayInfo> it = sources.iterator(); it.hasNext();) {
			RepayInfo xhInfo = (RepayInfo) it.next();
			if (flagMap.get(xhInfo.getId()) == null)
				delList.add(xhInfo.getId());
		}
		Map<String, Object> resultMap = Maps.newHashMap();
		resultMap.put("del", delList);
		resultMap.put("add", addList);
		return resultMap;
	}

	private static Map<String, Object> comparatOutPay(Long orderId, String borrowNid, List<RepayInfo> sources,
			List<RepayInfo> target) {
		List<Long> delList = Lists.newArrayList();
		List<RepayInfo> addList = Lists.newArrayList();
		boolean isSearch = false;
		Map<Long, Integer> flagMap = Maps.newHashMap();
		try {
			Map<String, RepayInfo> paySources = convertRepayInfo(sources);
			if(target!=null) {
				for (Iterator<RepayInfo> iss = target.iterator(); iss.hasNext();) {
					RepayInfo offDetail = (RepayInfo) iss.next();
					isSearch = false;
					String tarDate = DateUtil.getTimestamptoStr(offDetail.getRepayDate());
					RepayInfo repay = paySources.get(tarDate);
					if (repay != null) {
						BigDecimal totalAmt = offDetail.getRepayAmount();
						if (repay.getRepayAmount().compareTo(totalAmt) == 0) {
							isSearch = true;
							flagMap.put(repay.getId(), 1);
							continue;
						}
					}
					if (!isSearch) {
						RepayInfo newInfo = convertTRepay(orderId, borrowNid, offDetail);
						addList.add(newInfo);
					}
				}
			}
			if (sources!=null) {
				for (Iterator<RepayInfo> it = sources.iterator(); it.hasNext();) {
					RepayInfo xhInfo = (RepayInfo) it.next();
					if (flagMap.get(xhInfo.getId()) == null)
						delList.add(xhInfo.getId());
				}
			}
		}catch(Exception e) {
			logger.info(e.getMessage());
		}
		Map<String, Object> resultMap = Maps.newHashMap();
		resultMap.put("del", delList);
		resultMap.put("add", addList);
		return resultMap;
	}

	private static RepayInfo convertTRepay(Long orderId, String borrowNid, RepayInfo offDetail) {
		RepayInfo newInfo = new RepayInfo();
		BeanUtils.copyProperties(offDetail, newInfo);
		newInfo.setBorrowNid(borrowNid);
		newInfo.setOrderId(orderId);
		newInfo.setRepayDate(new java.sql.Date(offDetail.getRepayDate().getTime()));
		newInfo.setUtime(null);
		return newInfo;
	}

	private static RepayInfo convertToRepay(Long orderId, String borrowNid, PayDetail offDetail, BigDecimal interAmt) {
		RepayInfo newInfo = new RepayInfo();
		newInfo.setBorrowNid(borrowNid);
		newInfo.setOrderId(orderId);
		newInfo.setRepayAmount(new BigDecimal(offDetail.getPayAmt()).add(interAmt));
		newInfo.setRepayCount(Integer.parseInt(offDetail.getPayTimes()));
		newInfo.setRepayDate(new java.sql.Date(DateUtil.getStrtoDate(offDetail.getPayDate()).getTime()));
		String desc = offDetail.getPayStatus();
		newInfo.setRepayStatus(RepayOrderEnum.getState(desc));
		return newInfo;
	}

	private static Map<String, InterestOffInfo> convertInterOff(List<InterestOffInfo> planInterTargets) {
		Map<String, InterestOffInfo> maps = Maps.newHashMap();
		if (!planInterTargets.isEmpty()) {
			for (InterestOffInfo offinfo : planInterTargets) {
				String dateStr = DateUtil.getStrtoDateStr(offinfo.getInterEndDate());
				maps.put(dateStr, offinfo);
			}
		}
		return maps;
	}

	private static Map<String, RepayPlan> convertRepayPlan(List<RepayPlan> targets) {
		Map<String, RepayPlan> maps = Maps.newHashMap();
		for (RepayPlan pay : targets) {
			String dateStr = DateUtil.getTimestamptoStr(pay.getPlanDate());
			maps.put(dateStr, pay);
		}
		return maps;
	}

	private static Map<String, Object> comparatorPlan(Long orderId, String borrowNid, List<RepayPlan> sources,
			List<PayOffInfo> targets, List<InterestOffInfo> planInterTargets) {
		List<Long> delList = Lists.newArrayList();
		List<RepayPlan> addList = Lists.newArrayList();
		Map<String, InterestOffInfo> interTargets = convertInterOff(planInterTargets);
		Map<String, RepayPlan> paySources = convertRepayPlan(sources);

		boolean isSearch = false;
		Map<Long, Integer> flagMap = Maps.newHashMap();
		if (targets != null && targets.size() > 0) {
			for (Iterator<PayOffInfo> iss = targets.iterator(); iss.hasNext();) {
				PayOffInfo offplan = (PayOffInfo) iss.next();
				isSearch = false;
				String tarDate = DateUtil.getStrtoDateStr(offplan.getPlanPayDate());
				RepayPlan repay = paySources.get(tarDate);
				InterestOffInfo offInfo = interTargets.get(tarDate);
				BigDecimal interAmt = new BigDecimal(0);
				if (offInfo != null)
					interAmt = new BigDecimal(offInfo.getPayInterAmt());
				if (repay != null) {
					BigDecimal totalAmt = new BigDecimal(offplan.getPlanPayAmt()).add(interAmt);
					if (repay.getAmount().compareTo(totalAmt) == 0) {
						isSearch = true;
						flagMap.put(repay.getId(), 1);
						continue;
					}
				}
				if (!isSearch) {
					RepayPlan newPlan = convertToRepayPlan(orderId, borrowNid, offplan, interAmt);
					addList.add(newPlan);
				}
			}
			for (Iterator<RepayPlan> it = sources.iterator(); it.hasNext();) {
				RepayPlan xhPlan = (RepayPlan) it.next();
				if (flagMap.get(xhPlan.getId()) == null)
					delList.add(xhPlan.getId());
			}
		}
		Map<String, Object> resultMap = Maps.newHashMap();
		resultMap.put("del", delList);
		resultMap.put("add", addList);
		return resultMap;
	}

	private static Map<String, Object> comparatOutPlan(Long orderId, String borrowNid, List<RepayPlan> sources,
			List<RepayPlan> targets) {
		List<Long> delList = Lists.newArrayList();
		List<RepayPlan> addList = Lists.newArrayList();
		Map<String, RepayPlan> paySources = convertRepayPlan(sources);
		try {
			boolean isSearch = false;
			Map<Long, Integer> flagMap = Maps.newHashMap();
			if (targets != null && targets.size() > 0) {
				for (Iterator<RepayPlan> iss = targets.iterator(); iss.hasNext();) {
					RepayPlan offplan = (RepayPlan) iss.next();
					isSearch = false;
					String tarDate = DateUtil.getTimestamptoStr(offplan.getPlanDate());
					RepayPlan repay = paySources.get(tarDate);
					if (repay != null) {
						BigDecimal totalAmt = offplan.getPlanAmount();
						if (repay.getAmount().compareTo(totalAmt) == 0) {
							isSearch = true;
							flagMap.put(repay.getId(), 1);
							continue;
						}
					}
					if (!isSearch) {
						RepayPlan newPlan = convertOutRepayPlan(orderId, borrowNid, offplan);
						addList.add(newPlan);
					}
				}
				for (Iterator<RepayPlan> it = sources.iterator(); it.hasNext();) {
					RepayPlan xhPlan = (RepayPlan) it.next();
					if (flagMap.get(xhPlan.getId()) == null)
						delList.add(xhPlan.getId());
				}
			}
		}catch(Exception e) {
			logger.info(e.getMessage());
		}
		Map<String, Object> resultMap = Maps.newHashMap();
		resultMap.put("del", delList);
		resultMap.put("add", addList);
		return resultMap;
	}

	private static RepayPlan convertOutRepayPlan(Long orderId, String borrowNid, RepayPlan off) {
		RepayPlan newplan = new RepayPlan();
		BeanUtils.copyProperties(off, newplan);
		newplan.setBorrowNid(borrowNid);
		newplan.setOrderId(orderId);
		newplan.setPlanDate(new java.sql.Date(off.getPlanDate().getTime()));
		newplan.setUtime(null);
		return newplan;
	}

	private static RepayPlan convertToRepayPlan(Long orderId, String borrowNid, PayOffInfo off, BigDecimal interAmt) {
		RepayPlan newplan = new RepayPlan();
		newplan.setBorrowNid(borrowNid);
		newplan.setAmount(new BigDecimal(off.getPayAmt()).add(interAmt));
		newplan.setOrderId(orderId);
		newplan.setOverdueAmount(new BigDecimal(off.getOutDayAmt()));
		newplan.setPlanAmount(new BigDecimal(off.getPlanPayAmt()).add(interAmt));
		newplan.setPlanDate(new java.sql.Date(DateUtil.getStrtoDate(off.getPlanPayDate()).getTime()));
		return newplan;
	}

	/**
	 * 订单异动数据导出文件处理
	 * 
	 * @author fangli
	 *
	 */
	class InnerOrderBuildFile implements Runnable {
		private String crediturl;
		private String startDate;
		private String endDate;
		private String teller;
		private String password;

		public InnerOrderBuildFile(String crediturl, String startDate, String endDate, String teller, String password) {
			this.crediturl = crediturl;
			this.startDate = startDate;
			this.endDate = endDate;
			this.teller = teller;
			this.password = password;
		}

		@Override
		public void run() {
			Order order = null;
			Map<Long, String> skipOrderMaps = Maps.newHashMap();
			String busiStat = "00";
			//正常的订单
			CreditResponse response = CreditUntil.getBussinessInfo(crediturl,config.CREDITSYSTEM_DEPTCODE,busiStat, startDate, endDate, "", teller, password);
			if (response.isSuccess()) {
				List<BussinessInfo> list = (List<BussinessInfo>) response.getData();
				for (BussinessInfo info : list) {
					
					//替换开始
					order = new Order();
					order.setIdCardNum(info.getCardNo());
					order.setAmount(new BigDecimal(info.getProvideAmount()));
					order.setCheckStatus(1);
					order.setContractStatus("open");
					List<Order> orderlst = null;
					boolean nullFlag = false;
					try {
						orderlst = crudTemplate.find(order,Criteria.from(Order.class).where().and("id_card_num", Operator.EQ).and("amount", Operator.EQ).and("loan_day", Operator.EQ, info.getProviceDate()).and("contract_status", Operator.EQ).and("check_status", Operator.EQ).endWhere());
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (orderlst.isEmpty()) {
						try {
							nullFlag =true;
							order.setLoanDay(null);
							order.setContractStatus(null);
							orderlst = crudTemplate.find(order,Criteria.from(Order.class).where().and("id_card_num", Operator.EQ).and("amount", Operator.EQ).and("check_status", Operator.EQ).and("contract_status", Operator.EQ, "open").endWhere());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
										
					if (orderlst != null && orderlst.size() > 0) {
						for (Iterator it= orderlst.iterator();it.hasNext();) {
							order = (Order) it.next();
							if (nullFlag&&order.getLoanDay()!=null)
								continue;
							skipOrderMaps.put(order.getId(), "1");
							List<PayOffInfo> planTarget = info.getListPayPlan();
							List<InterestOffInfo> planInterTarget = info.getListInterOff();
							List<RepayPlan> planSource = null;
							try {
								planSource = repayPlanService.queryRepayPlan(order.getId());
							} catch (Exception e) {
								e.printStackTrace();
							}
							Map<String, Object> resultMap = comparatorPlan(order.getId(), order.getBorrowNid(), planSource,planTarget, planInterTarget);
							List<Long> delList = (List<Long>) resultMap.get("del");
							List<RepayPlan> addList = (List<RepayPlan>) resultMap.get("add");
							if (delList.size() > 0) {
								try {
									Long[] ids = delList.toArray(new Long[delList.size()]);
									crudTemplate.del(RepayPlan.class, ids);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							if (addList.size() > 0) {
								try {
									crudTemplate.save(addList);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}

							List<PayDetail> payTarget = info.getListPayDetail();
							List<InterestDetail> payDtlTarget = info.getListInterDetail();
							List<RepayInfo> paySource = null;
							try {
								paySource = repayInfoService.queryRepayInfo(order.getId());
							} catch (Exception e) {
								e.printStackTrace();
							}
							Map<String, Object> repayMap = comparatorPay(order.getId(), order.getBorrowNid(), paySource,payTarget, payDtlTarget);
							List<Long> delPayList = (List<Long>) repayMap.get("del");
							List<RepayInfo> addPayList = (List<RepayInfo>) repayMap.get("add");
							
							
							if (delPayList.size() > 0) {
								try {
									Long[] ids = delPayList.toArray(new Long[delList.size()]);
									crudTemplate.del(RepayInfo.class, ids);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							if (addPayList.size() > 0) {
								try {
									for(RepayInfo temptDto:addPayList) {
										crudTemplate.save(temptDto);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							if (order.getContractNo() == null || order.getLoanDay() == null||order.getContractStatus()==null) {
								order.setContractNo(info.getContractNo());
								Date payDate =DateUtil.getStrtoDate(info.getProviceDate()); 
								order.setLoanDay(new java.sql.Date(payDate.getTime()));
								order.setUtime(null);
								try {
									crudTemplate.update(order, Criteria.from(Order.class));
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}



					
					/*order = new Order();
					order.setIdCardNum(info.getCardNo());
					order.setAmount(new BigDecimal(info.getProvideAmount()));
					order.setCheckStatus(1);
					List<Order> orderlst = null;
					try {
						orderlst = crudTemplate.find(order,Criteria.from(Order.class).where().and("id_card_num", Operator.EQ)
										.and("amount", Operator.EQ).and("check_status", Operator.EQ).endWhere());
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (orderlst != null && orderlst.size() > 0) {
						order = orderlst.get(0);
						skipOrderMaps.put(order.getId(), "1");
						List<PayOffInfo> planTarget = info.getListPayPlan();
						List<InterestOffInfo> planInterTarget = info.getListInterOff();
						List<RepayPlan> planSource = null;
						try {
							planSource = repayPlanService.queryRepayPlan(order.getId());
						} catch (Exception e) {
							e.printStackTrace();
						}
						Map<String, Object> resultMap = comparatorPlan(order.getId(), order.getBorrowNid(), planSource,planTarget, planInterTarget);
						List<Long> delList = (List<Long>) resultMap.get("del");
						List<RepayPlan> addList = (List<RepayPlan>) resultMap.get("add");
						if (delList.size() > 0) {
							try {
								Long[] ids = delList.toArray(new Long[delList.size()]);
								crudTemplate.del(RepayPlan.class, ids);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						if (addList.size() > 0) {
							try {
								crudTemplate.save(addList);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						List<PayDetail> payTarget = info.getListPayDetail();
						List<InterestDetail> payDtlTarget = info.getListInterDetail();
						List<RepayInfo> paySource = null;
						try {
							paySource = repayInfoService.queryRepayInfo(order.getId());
						} catch (Exception e) {
							e.printStackTrace();
						}
						Map<String, Object> repayMap = comparatorPay(order.getId(), order.getBorrowNid(), paySource,payTarget, payDtlTarget);
						List<Long> delPayList = (List<Long>) repayMap.get("del");
						List<RepayInfo> addPayList = (List<RepayInfo>) repayMap.get("add");
						
						
						if (delPayList.size() > 0) {
							try {
								Long[] ids = delPayList.toArray(new Long[delList.size()]);
								crudTemplate.del(RepayInfo.class, ids);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						if (addPayList.size() > 0) {
							try {
								for(RepayInfo temptDto:addPayList) {
									crudTemplate.save(temptDto);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						if (order.getContractNo() == null || order.getLoanDay() == null||order.getContractStatus()==null) {
							order.setContractNo(info.getContractNo());
							order.setLoanDay(new java.sql.Date(DateUtil.getStrtoDate(info.getProviceDate()).getTime()));
							try {
								crudTemplate.update(order, Criteria.from(Order.class).where().endWhere());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}*/
				}
				order =null;
			}
			//临时解决方法
			List<Order> orderlist=null;
			try {
				order = new Order();
				order.setContractStatus("open");
				orderlist = crudTemplate.find(order,Criteria.from(Order.class).where().and("contract_status", Operator.EQ).endWhere());
			} catch (Exception e) {
				e.printStackTrace() ;
			}
			if (orderlist != null && orderlist.size() > 0) {
				busiStat ="01";
				for(Order orderItem :orderlist) {
					if (orderItem.getContractNo()!=null) {
						CreditResponse responseInfo = CreditUntil.getBussinessInfo(crediturl,config.CREDITSYSTEM_DEPTCODE,busiStat,startDate, endDate,orderItem.getContractNo(), teller, password);
						if (responseInfo.isSuccess()) {
							List<BussinessInfo> lists = (List<BussinessInfo>) responseInfo.getData();
							if (lists != null && lists.size() > 0) {
								BussinessInfo info = lists.get(0);
								skipOrderMaps.put(orderItem.getId(), "1");
								List<PayOffInfo> planTarget = info.getListPayPlan();
								List<InterestOffInfo> planInterTarget = info.getListInterOff();
								List<RepayPlan> planSource = null;
								try {
									planSource = repayPlanService.queryRepayPlan(orderItem.getId());
								} catch (Exception e) {
									e.printStackTrace();
								}
								Map<String, Object> resultMap = comparatorPlan(orderItem.getId(), orderItem.getBorrowNid(),planSource, planTarget, planInterTarget);
								List<Long> delList = (List<Long>) resultMap.get("del");
								List<RepayPlan> addList = (List<RepayPlan>) resultMap.get("add");
								if (delList.size() > 0) {
									try {
										Long[] ids = delList.toArray(new Long[delList.size()]);
										crudTemplate.del(RepayPlan.class, ids);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
								if (addList.size() > 0) {
									try {
										crudTemplate.save(addList);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}

								List<PayDetail> payTarget = info.getListPayDetail();
								List<InterestDetail> payDtlTarget = info.getListInterDetail();
								List<RepayInfo> paySource = null;
								try {
									paySource = repayInfoService.queryRepayInfo(orderItem.getId());
								} catch (Exception e) {
									e.printStackTrace();
								}
								Map<String, Object> repayMap = comparatorPay(orderItem.getId(), orderItem.getBorrowNid(),paySource, payTarget, payDtlTarget);
								List<Long> delPayList = (List<Long>) repayMap.get("del");
								List<RepayInfo> addPayList = (List<RepayInfo>) repayMap.get("add");
								if (delPayList.size() > 0) {
									try {
										Long[] ids = delPayList.toArray(new Long[delPayList.size()]);
										crudTemplate.del(RepayInfo.class, ids);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
								if (addPayList.size() > 0) {
									try {
										crudTemplate.save(addPayList);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}

								if (!"open".equals(info.getContractIsOver())) {
									orderItem.setContractStatus(info.getContractIsOver());
									orderItem.setUtime(null);
									try {
										crudTemplate.update(orderItem, Criteria.from(Order.class));
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}
						}
					}
				}
				order =null;
			}
			
			//提起换款的订单
//			try {
//				busiStat ="02";
//				CreditResponse payResponse = CreditUntil.getPayRecord(crediturl, startDate, endDate, config.CREDITSYSTEM_DEPTCODE, teller, password);
//				if (payResponse.isSuccess()) {
//					List<PayRecord> paylist = (List<PayRecord>) payResponse.getData();
//					if (!paylist.isEmpty()) {
//						for(PayRecord repay:paylist) {
//							CreditResponse responseInfo = CreditUntil.getBussinessInfo(crediturl,config.CREDITSYSTEM_DEPTCODE,busiStat,startDate, endDate,repay.getContractNo(), teller, password);
//							if (responseInfo.isSuccess()) {
//								List<BussinessInfo> lists = (List<BussinessInfo>) responseInfo.getData();
//								if (lists != null && lists.size() > 0) {
//									BussinessInfo info = lists.get(0);
//									order = new Order();
//									order.setIdCardNum(info.getCardNo());
//									order.setAmount(new BigDecimal(info.getProvideAmount()));
//									order.setCheckStatus(1);
//									List<Order> orderlst = null;
//									try {
//										orderlst = crudTemplate.find(order,Criteria.from(Order.class).where().and("id_card_num", Operator.EQ).and("amount", Operator.EQ).and("check_status", Operator.EQ).endWhere());
//									} catch (Exception e) {
//										e.printStackTrace();
//									}
//									if (orderlst != null && orderlst.size() > 0) {
//										order = orderlst.get(0);
//										skipOrderMaps.put(order.getId(), "1");
//										List<PayOffInfo> planTarget = info.getListPayPlan();
//										List<InterestOffInfo> planInterTarget = info.getListInterOff();
//										List<RepayPlan> planSource = null;
//										try {
//											planSource = repayPlanService.queryRepayPlan(order.getId());
//										} catch (Exception e) {
//											e.printStackTrace();
//										}
//										Map<String, Object> resultMap = comparatorPlan(order.getId(), order.getBorrowNid(),planSource, planTarget, planInterTarget);
//										List<Long> delList = (List<Long>) resultMap.get("del");
//										List<RepayPlan> addList = (List<RepayPlan>) resultMap.get("add");
//										if (delList.size() > 0) {
//											try {
//												Long[] ids = delList.toArray(new Long[delList.size()]);
//												crudTemplate.del(RepayPlan.class, ids);
//											} catch (Exception e) {
//												e.printStackTrace();
//											}
//										}
//										if (addList.size() > 0) {
//											try {
//												crudTemplate.save(addList);
//											} catch (Exception e) {
//												e.printStackTrace();
//											}
//										}
//		
//										List<PayDetail> payTarget = info.getListPayDetail();
//										List<InterestDetail> payDtlTarget = info.getListInterDetail();
//										List<RepayInfo> paySource = null;
//										try {
//											paySource = repayInfoService.queryRepayInfo(order.getId());
//										} catch (Exception e) {
//											e.printStackTrace();
//										}
//										Map<String, Object> repayMap = comparatorPay(order.getId(), order.getBorrowNid(),paySource, payTarget, payDtlTarget);
//										List<Long> delPayList = (List<Long>) repayMap.get("del");
//										List<RepayInfo> addPayList = (List<RepayInfo>) repayMap.get("add");
//										if (delPayList.size() > 0) {
//											try {
//												Long[] ids = delPayList.toArray(new Long[delPayList.size()]);
//												crudTemplate.del(RepayInfo.class, ids);
//											} catch (Exception e) {
//												e.printStackTrace();
//											}
//										}
//										if (addPayList.size() > 0) {
//											try {
//												crudTemplate.save(addPayList);
//											} catch (Exception e) {
//												e.printStackTrace();
//											}
//										}
//		
//										if (!"open".equals(info.getContractIsOver())) {
//											order.setContractStatus(info.getContractIsOver());
//											try {
//												crudTemplate.update(order, Criteria.from(Order.class).fields("contractStatus"));
//											} catch (Exception e) {
//												e.printStackTrace();
//											}
//										}
//									}
//								}
//							}
//							order =null;
//						}
//					}
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
			//按正常分期还款
//			try {
//				busiStat ="00";
//				List<Long> list = repayPlanService.getYestDayPlan(DateUtil.getStrtoDate(startDate),DateUtil.getStrtoDate(endDate));
//				for (Long id : list) {
//					if (skipOrderMaps.get(id) != null) {
//						continue;
//					} else {
//						order = crudTemplate.get(Order.class, id);
//						if (order.getContractNo() != null) {
//							CreditResponse responseInfo = CreditUntil.getBussinessInfo(crediturl,config.CREDITSYSTEM_DEPTCODE,busiStat,startDate, endDate,order.getContractNo(), teller, password);
//							List<BussinessInfo> lists = (List<BussinessInfo>) responseInfo.getData();
//							if (lists != null && lists.size() > 0) {
//								BussinessInfo info = lists.get(0);
//								List<PayOffInfo> planTarget = info.getListPayPlan();
//								List<InterestOffInfo> planInterTarget = info.getListInterOff();
//								List<RepayPlan> planSource = null;
//								try {
//									planSource = repayPlanService.queryRepayPlan(order.getId());
//								} catch (Exception e) {
//									e.printStackTrace();
//								}
//								Map<String, Object> resultMap = comparatorPlan(order.getId(), order.getBorrowNid(),
//										planSource, planTarget, planInterTarget);
//								List<Long> delList = (List<Long>) resultMap.get("del");
//								List<RepayPlan> addList = (List<RepayPlan>) resultMap.get("add");
//								if (delList.size() > 0) {
//									try {
//										Long[] ids = delList.toArray(new Long[delList.size()]);
//										crudTemplate.del(RepayPlan.class, ids);
//									} catch (Exception e) {
//										e.printStackTrace();
//									}
//								}
//								if (addList.size() > 0) {
//									try {
//										crudTemplate.save(addList);
//									} catch (Exception e) {
//										e.printStackTrace();
//									}
//								}
//
//								List<PayDetail> payTarget = info.getListPayDetail();
//								List<InterestDetail> payDtlTarget = info.getListInterDetail();
//								List<RepayInfo> paySource = null;
//								try {
//									paySource = repayInfoService.queryRepayInfo(order.getId());
//								} catch (Exception e) {
//									e.printStackTrace();
//								}
//								Map<String, Object> repayMap = comparatorPay(order.getId(), order.getBorrowNid(),paySource, payTarget, payDtlTarget);
//								List<Long> delPayList = (List<Long>) repayMap.get("del");
//								List<RepayInfo> addPayList = (List<RepayInfo>) repayMap.get("add");
//								if (delPayList.size() > 0) {
//									try {
//										Long[] ids = delPayList.toArray(new Long[delPayList.size()]);
//										crudTemplate.del(RepayInfo.class, ids);
//									} catch (Exception e) {
//										e.printStackTrace();
//									}
//								}
//								if (addPayList.size() > 0) {
//									try {
//										crudTemplate.save(addPayList);
//									} catch (Exception e) {
//										e.printStackTrace();
//									}
//								}
//
//								if (order.getContractNo() == null || order.getLoanDay() == null) {
//									order.setLoanDay(DateUtil.getStrtoDate(info.getProviceDate()));
//									order.setContractNo(info.getContractNo());
//									try {
//										crudTemplate.update(order, Criteria.from(Order.class).fields("loanDay","contractNo"));
//									} catch (Exception e) {
//										e.printStackTrace();
//									}
//								}
//							}
//						}
//					}
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
		}
	}

	/**
	 * 授信数据生成文件处理
	 * 
	 * @author fangli
	 *
	 */
	class PersonalCreditBuildFile implements Runnable {
		private String crediturl;
		private String startDate;
		private String endDate;
		private String teller;
		private String password;

		public PersonalCreditBuildFile(String crediturl, String startDate, String endDate, String teller,
				String password) {
			this.crediturl = crediturl;
			this.startDate = startDate;
			this.endDate = endDate;
			this.teller = teller;
			this.password = password;
		}

		@Override
		public void run() {
			CreditResponse response = CreditUntil.getCreditIdCard(crediturl,config.CREDITSYSTEM_DEPTCODE, startDate, endDate, teller, password);
			if (response.isSuccess()) {
				List<CreditClient> list = (List<CreditClient>) response.getData();
				PersonalCredit bo = null;
				for (CreditClient clent : list) {
					bo = new PersonalCredit();
					bo.setIdCardNum(clent.getIdCardNum());
					bo.setMatchSign(clent.getMatchSign());
					bo.setName(clent.getCustName());
					try {
						personalCreditService.save(bo);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
		}
	}

	/**
	 * 黑名单生成文件处理
	 * 
	 * @author fangli
	 *
	 */
	class BlackBuildFile implements Runnable {
		private String crediturl;
		private String startDate;
		private String endDate;
		private String teller;
		private String password;

		public BlackBuildFile(String crediturl, String startDate, String endDate, String teller, String password) {
			this.crediturl = crediturl;
			this.startDate = startDate;
			this.endDate = endDate;
			this.teller = teller;
			this.password = password;
		}

		@Override
		public void run() {
			CreditResponse response = CreditUntil.getCreditBlackList(crediturl,config.CREDITSYSTEM_DEPTCODE, startDate, endDate, teller, password);
			if (response.isSuccess()) {
				List<BlackUser> list = (List<BlackUser>) response.getData();
				BlackList bo = null;
				for (BlackUser black : list) {
					bo = new BlackList();
					bo.setIdCardNum(black.getCardNo());
					bo.setMatchSign(black.getMatchSign());
					bo.setName(black.getCustName());
					try {
						blackListService.save(bo);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
			logger.error("=---------------------跑批结束！-----------------------=");
		}
	}

	/**
	 * 下载文件
	 */
	@Override
	public File downOutFile(String startDate, String endDate) throws Exception {
		File saveFile = FileUtil.generateFile(config.ORDERTRANSACTION_TEMPFILEPATH, endDate.concat("_OUT"),
				config.ORDERTRANSACTION_FILEEXT);
		if (saveFile.exists()) {
			FileUtil.deleteFile(saveFile);
		}
		FileUtil.openFile(saveFile);
		Order order = new Order();
		Date startsDate = DateUtil.getStrtoDate(startDate);
		Date endsDate = DateUtil.getStrtoDate(endDate);
		Date enddDate = DateUtil.getDateAfter(endsDate, 1);
		order.setCheckStatus(1);
		List<Order> orders = crudTemplate.find(order,
				Criteria.from(Order.class).where().and("ctime", Operator.GT, new java.sql.Date(startsDate.getTime()))
						.and("ctime", Operator.LT, new java.sql.Date(enddDate.getTime()))
						.and("check_status", Operator.EQ).endWhere());
		OrderVO vo = null;
		for (Order orderItem : orders) {
			vo = new OrderVO();
			BeanUtils.copyProperties(orderItem, vo);
			vo.setBirthday(DateUtil.getDatetoSDate(orderItem.getBirthday()));
			vo.setCheckTime(DateUtil.getDatetoSDate(orderItem.getCheckTime()));
			vo.setLoanDay(DateUtil.getDatetoSDate(orderItem.getLoanDay()));
			FileUtil.writeFile(saveFile, vo);
		}
		return saveFile;
	}

	/**
	 * 内前置导入方法
	 */
	@Override
	public boolean importInnerFile(File file) throws Exception {
		FileUtil.innerFileToHandle(file, OrderVO.class);
		FileUtil.deleteFile(file);
		return true;
	}

	@Override
	public boolean insertVo(OrderVO vo) throws Exception {
		threadPoolTaskExecutor.execute(new OrderThreadTask(vo));
		return true;
	}

	class OrderThreadTask implements Runnable {

		private OrderVO vo;

		public OrderThreadTask(OrderVO vo) {
			this.vo = vo;
		}

		@Override
		public void run() {
			Order order = null;
			String borrowNid = vo.getBorrowNid();
			order = new Order();
			order.setBorrowNid(borrowNid);
			List<Order> orderlst = null;
			try {
				orderlst = crudTemplate.find(order, Criteria.from(Order.class).fields("borrow_nid").where()
						.and("borrow_nid", Operator.EQ).endWhere());
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (orderlst.isEmpty()) {
				try {
					order = new Order();
					BeanUtils.copyProperties(vo, order);
					order.setBirthday(DateUtil.getDatetoSqlDate(vo.getBirthday()));
					order.setLoanDay(DateUtil.getDatetoSqlDate(vo.getLoanDay()));
					order.setCheckTime(DateUtil.getDatetoSqlDate(vo.getCheckTime()));
					order.setUtime(null);
					crudTemplate.save(order);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 人行征信处理
	 * 
	 * @author fangli
	 *
	 */
	class CreditThreadTask implements Runnable {

		private Logger logger = LoggerFactory.getLogger(this.getClass());
		private String crediturl;
		private String username;
		private String certype;
		private String cercode;
		private String queryreason;
		private String filePath;
		private String loginNum;
		private String password;

		public CreditThreadTask(String crediturl, String username, String certype, String cercode, String queryreason,
				String filePath, String loginNum, String password) {
			this.username = username;
			this.certype = certype;
			this.cercode = cercode;
			this.queryreason = queryreason;
			this.filePath = filePath;
			this.crediturl = crediturl;
			this.loginNum = loginNum;
			this.password = password;
		}

		@Override
		public void run() {
			File file = new File(filePath + cercode + ".html");
			if (!file.exists()) {
				CreditResponse response = CrccUntil.getCrccInfo(crediturl, username, certype, cercode, queryreason,
						filePath, loginNum, password);
				if (!response.isSuccess()) {
					logger.warn("getCrccInfo ERROR Message:{}", response.getErrorMessage());
				}
			}
		}
	}

	@Override
	public String queryCrccReport(String userName, String idCard) throws Exception {
		Date date = new Date();
		String tellerAccount = null;
		String password = null;
		StringBuffer httpUrl = new StringBuffer();
		File file = new File(config.PCREDITTRANSACTION_FILEPATH + idCard + ".html");
		if (!file.exists()) {
			List<CreditAccount> list = creditAccountService.queryAllByStatus(1);
			String redisMapId = "SIS-CREDIT-SYSTEM-REQUEST";
			try {
				int i = 0;
				boolean flag = true;
				while (flag) {
					if (i >= 3) {
						flag = false;
						redisTemplate.del(redisMapId);
						break;
					}
					// 判断缓存中是否有该调用
					if (redisTemplate.setNX(redisMapId, "1") == 1) {
						redisTemplate.expire(redisMapId, 3);
						for (CreditAccount account : list) {
							if (account.getLimitNum() > account.getAlUse()) {
								tellerAccount = account.getAccount();
								password = account.getPassword();
								creditAccountService.updateAccount(account.getId(), account.getAlUse() + 1);
								flag = false;
								redisTemplate.del(redisMapId);
								break;
							}
						}
					} else {
						try {
							logger.error("sis.order.confirm error,after 3 sec retry");
							i++;
							Thread.sleep(2000);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
				}
			} catch (Exception ex) {
				throw ex;
			}
			logger.warn("getCrccInfo ERROR Message:{}", tellerAccount + "--" + password);
			if (tellerAccount != null) {
				CreditResponse response = CrccUntil.getCrccInfo(config.PCREDIT_HTTPADDRESS, userName,
						config.PCREDITSEARCH_IDCARD, idCard, config.PCREDITSEARCH_REASON,
						config.PCREDITTRANSACTION_FILEPATH, tellerAccount, password);
				if (!response.isSuccess()) {
					logger.warn("getCrccInfo ERROR Message:{}", response.getErrorMessage());
				} else
					httpUrl.append(config.PCREDITTRANSACTION_URL).append(idCard).append(".html");
			} else
				return null;
		} else
			httpUrl.append(config.PCREDITTRANSACTION_URL).append(idCard).append(".html");

		return httpUrl.toString();
	}

	@Override
	public void timeTaskCredit() throws Exception {
		Date startDate = DateUtil.getMonthDate(3);
		Date endDate = DateUtil.getMonthDate(1);
		Order order = new Order();
		List<Order> orders = crudTemplate.find(order,
				Criteria.from(Order.class).where().and("utime", Operator.GT, new java.sql.Date(startDate.getTime()))
						.and("utime", Operator.LT, new java.sql.Date(endDate.getTime())).endWhere());
		// for(Iterator<Order> it=orders.iterator();it.hasNext();) {
		// Order orderItem = (Order)it.next();
		// if (orderItem.getLoanDay()!=null)
		// continue;
		// File file = new
		// File(config.PCREDITTRANSACTION_FILEPATH+orderItem.getIdCardNum()+".html");
		// if (!file.exists()) {
		// threadPoolTaskExecutor.execute(new
		// CreditThreadTask(config.PCREDIT_HTTPADDRESS,orderItem.getRenter(),config.PCREDITSEARCH_IDCARD,orderItem.getIdCardNum(),config.PCREDITSEARCH_REASON,config.PCREDITTRANSACTION_FILEPATH,config.PCREDIT_LOGIN,config.PCREDIT_PASSWORD));
		// }
		// }
	}

	@Override
	public void cleanTaskCredit() throws Exception {
		creditAccountService.cleanAlUse();
	}
}
