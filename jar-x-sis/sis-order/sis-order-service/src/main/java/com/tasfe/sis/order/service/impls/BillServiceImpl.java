package com.tasfe.sis.order.service.impls;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import com.tasfe.sis.bank.api.BankContract;
import com.tasfe.sis.bank.api.dto.BatchTransferInfosDTO;
import com.tasfe.sis.bank.model.BatchTransferInfos;
import com.tasfe.sis.base.configs.HljnsConstants;
import com.tasfe.sis.base.service.CrudService;
import com.tasfe.sis.base.service.cache.redis.RedisTemplate;
import com.tasfe.sis.base.service.impls.CrudServiceImpl;
import com.tasfe.sis.order.api.BillContract;
import com.tasfe.sis.order.api.OrderContract;
import com.tasfe.sis.order.entity.Bill;
import com.tasfe.sis.order.entity.Order;
import com.tasfe.sis.order.model.ConfirmBillDto;
import com.tasfe.sis.order.model.QueryBillDto;
import com.tasfe.sis.order.model.vo.BillVO;
import com.tasfe.sis.order.model.vo.OrderVO;
import com.tasfe.sis.order.service.BillService;
import com.tasfe.sis.order.service.OrderService;
import com.tasfe.sis.user.entity.User;
import com.tasfe.sis.user.service.SessionService;
import com.tasfe.sis.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by hefusang on 2017/8/10.
 */
@Service
public class BillServiceImpl implements BillService,BillContract{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CrudTemplate crudTemplate;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private UserService userService;

    @Autowired
    private BankContract bankContract;

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public List<BillVO> queryBill(QueryBillDto queryBillDto) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(HljnsConstants.DATE_FOMAT_1);
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = simpleDateFormat.parse(queryBillDto.getDateFrom());
            endDate = simpleDateFormat.parse(queryBillDto.getDateTo());
            long intervalMilli = endDate.getTime() - startDate.getTime();
            int days = (int) (intervalMilli / (24 * 60 * 60 * 1000));
            if(days>300){
                throw new Exception();
            }
        } catch (ParseException e) {
            logger.error("ZznshOrderService#queryOrder",e);
            throw new Exception();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        List<BillVO> resList = Lists.newArrayList();
        do {
            List<BillVO> billList = queryOrderSummary(calendar.getTime(),queryBillDto.getCustomerCode(),queryBillDto.getConfirmStatus(),queryBillDto.getSessionCode());
            if(billList != null) {
                resList.addAll(billList);
            }
            calendar.add(Calendar.DATE,1);
        } while((calendar.getTime().compareTo(endDate)<=0));
        return resList;
    }

    @Override
    public boolean confirmBill(ConfirmBillDto confirmBillDto) throws Exception {
        Order example1 = new Order();
        example1.setCheckStatus(0);
        Long count1 = crudTemplate.count(example1,Criteria.from(Order.class).where().and("osid",Operator.EQ).and("check_status",Operator.EQ).endWhere());
        if(count1> 0) {
            return false;
        }
        Bill record = new Bill();
        record.setId(new Long(confirmBillDto.getAccount()));
        record.setConfirmStatus(HljnsConstants.STATUS_ONE);
        crudTemplate.update(record,Criteria.from(Bill.class).fields("confirmStatus"));
        return true;
    }

    private List<BillVO> queryOrderSummary(Date batchDate, String customerCode, Integer confirmStatus, String sessionCode) throws Exception {
        List<BillVO> orderList = new ArrayList<BillVO>();
        Bill bill = new Bill();
        bill.setCustomerCode(customerCode);
        bill.setBatchDate(new java.sql.Date(batchDate.getTime()));
        List<Bill> res = crudTemplate.find(bill, Criteria.from(Bill.class).where().and("batch_date", Operator.EQ).and("customer_code",Operator.EQ).endWhere());

        //日期已确认
        Boolean dateConfirmed = false;
        List<Bill> summaryList = new ArrayList<Bill>();
        Future<Integer> future = null;
        if((res != null)&&(res.size() > 0)){
            for(Bill temptSummary: res) {
                summaryList.add(temptSummary);
            }
            //查询的是未确认,将已确认变量置空
            if(HljnsConstants.STATUS_ZERO.equals(confirmStatus)){
                dateConfirmed = true;
                List<Bill> delList = new ArrayList<Bill>();
                for(Bill temptSummary:summaryList) {
                    if(HljnsConstants.STATUS_ONE.equals(temptSummary.getConfirmStatus())) {
                        delList.add(temptSummary);
                    }
                }
                for(Bill temptSummary:delList) {
                    summaryList.remove(temptSummary);
                }
            }
            //查询的是已确认
            if(HljnsConstants.STATUS_ONE.equals(confirmStatus)){
                dateConfirmed = true;
                List<Bill> delList = new ArrayList<Bill>();
                for(Bill temptSummary:summaryList) {
                    if(HljnsConstants.STATUS_ZERO.equals(temptSummary.getConfirmStatus())) {
                        delList.add(temptSummary);
                    }
                }
                for(Bill temptSummary:delList) {
                    summaryList.remove(temptSummary);
                }
            }
        }
        if(summaryList != null){
            for(Bill temptOrderSummary:summaryList) {
                BillVO temptOrderVO = new BillVO();
                SimpleDateFormat sdf= new SimpleDateFormat(HljnsConstants.DATE_FOMAT_1);
                temptOrderVO.setDate(sdf.format(batchDate));
                temptOrderVO.setStatus(new Integer(1).equals(temptOrderSummary.getConfirmStatus())?"已确认":"未确认");
                temptOrderVO.setAmount(temptOrderSummary.getTotalAmount());
                temptOrderVO.setQuantity(String.valueOf(temptOrderSummary.getOrderNum()));
                temptOrderVO.setSummaryOrderId(temptOrderSummary.getId());
                temptOrderVO.setOrderSource(temptOrderSummary.getOrderSource());
                temptOrderVO.setLoanStatus(temptOrderSummary.getLoanStatus());
                temptOrderVO.setBillNo(temptOrderSummary.getBillNo());
                if(future != null){
                    try {
                        Integer insertResult = future.get(1000L, TimeUnit.MILLISECONDS);
                        logger.info("添加新BiOrderSummary结果>>>>>>: "+insertResult);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }
                }
                orderList.add(temptOrderVO);
            }
        }
        return orderList;
    }

    @Override
    public void payment(String account) throws Exception {
        List<BatchTransferInfosDTO> infos = new ArrayList<BatchTransferInfosDTO>();
        bankContract.bankPayment(infos);
    }

    @Override
    public Long getBillId(String sn) throws Exception {
        String billId = redisTemplate.get("SIS-BANK-BILLS."+sn);
        if(billId!=null) {
            return Long.parseLong(billId);
        }

        return queryBill(new QueryBillDto(){{
            this.setBillNo(sn);
        }}).stream().findFirst().get().getSummaryOrderId();


    }

    @Override
    public String getBillOpening(String bankCode, String orderSource) throws Exception {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
        Date today=new Date();
        String REDIS_OPENNING="SIS-BANK-BILL-OPENING."+bankCode+"."+orderSource+"."+sdf.format(today);


        String sn = redisTemplate.get(REDIS_OPENNING);

        if(!Strings.isNullOrEmpty(sn)) {
            return sn;
        }

        Long lsn = redisTemplate.incr("SIS-BANK-BILL-LSN."+bankCode);
        sn = String.format("S%s-%s-%06d",bankCode,sdf.format(today),lsn);
        Bill bill=new Bill();
        bill.setBillNo(sn);
        bill.setCustomerCode(bankCode);
        bill.setOrderSource(orderSource);
        bill.setBatchDate(new java.sql.Date(today.getTime()));
        bill.setConfirmStatus(0);
        bill.setOrderNum(0L);
        bill.setTotalAmount(new BigDecimal(0));
        crudTemplate.save(bill);

        Long billId = bill.getId();
        redisTemplate.set("SIS-BANK-BILLS."+sn,billId.toString());
        redisTemplate.set(REDIS_OPENNING,sn);
        redisTemplate.expire(REDIS_OPENNING,86400);
        return sn;


    }
}
