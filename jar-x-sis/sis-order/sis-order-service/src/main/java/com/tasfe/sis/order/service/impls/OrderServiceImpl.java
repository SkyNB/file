package com.tasfe.sis.order.service.impls;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.tasfe.sis.order.entity.*;
import com.tasfe.sis.order.model.*;
import com.tasfe.sis.order.model.vo.OrderDetailVO;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.criteria.Limit;
import com.tasfe.framework.crud.api.criteria.Where;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.api.params.Page;
import com.tasfe.framework.crud.core.CrudTemplate;
import com.tasfe.sis.bank.api.BankContract;
import com.tasfe.sis.bank.api.dto.BatchTransferInfosDTO;
import com.tasfe.sis.base.configs.HljnsConstants;
import com.tasfe.sis.base.exception.BizException;
import com.tasfe.sis.base.model.code.ErrorCode;
import com.tasfe.sis.base.service.CacheService;
import com.tasfe.sis.base.service.cache.redis.RedisTemplate;
import com.tasfe.sis.base.utils.poi.CellUtil;
import com.tasfe.sis.order.api.OrderContract;
import com.tasfe.sis.order.api.dto.OrderItemDTO;
import com.tasfe.sis.order.api.dto.RepayPlanDTO;
import com.tasfe.sis.order.api.dto.RepaymentDTO;
import com.tasfe.sis.order.model.vo.ContractVO;
import com.tasfe.sis.order.model.vo.OrderItemVO;
import com.tasfe.sis.order.model.vo.PersonInfoVO;
import com.tasfe.sis.order.service.ApplicationAssignmentService;
import com.tasfe.sis.order.service.CheckLogService;
import com.tasfe.sis.order.service.OrderService;
import com.tasfe.sis.order.service.RefusalService;
import com.tasfe.sis.user.entity.User;
import com.tasfe.sis.user.service.UserService;

/**
 * Created by Lait on 2017/7/27.
 */
@Service
public class OrderServiceImpl implements OrderService,OrderContract{
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private CrudTemplate crudTemplate;

    @Autowired
    private CheckLogService checkLogService;

    @Autowired
    private RefusalService refusalService;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private BankContract bankContract;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    final String RedisManageQueueKeyTemplate = "SIS-ORDER-MANAGMENT-ORDER-LOCK-%d";

    @Autowired
    private ApplicationAssignmentService applicationAssignmentService;

    @Override
    public Long newOrder(OrderItemDTO orderItemDTO) throws Exception {
        Order order = new Order();
        BeanUtils.copyProperties(orderItemDTO,order);
        order.setBirthday(new java.sql.Date(orderItemDTO.getBirthday().getTime()));
        crudTemplate.save(order);
        return order.getId();
    }

    @Override
    public Long updateOrderDecision(OrderItemDTO orderItemDTO) throws Exception {
        Order order = new Order();
        order.setCheckStatus(orderItemDTO.getCheckStatus());
        order.setId(orderItemDTO.getId());
        order.setDecisionResult(orderItemDTO.getDecisionResult());
        crudTemplate.update(order,Criteria.from(Order.class).fields("checkStatus","decisionResult"));
        //crudTemplate.saveOrUpdate(order);

        return null;
    }

    @Override
    public Long updateOrderPhotos(Long orderId, String photoUrl0, String photoUrl1) throws Exception {
        Order order = new Order();
        order.setPhotoUrl1(photoUrl1);
        order.setPhotoUrl0(photoUrl0);
        order.setId(orderId);
        crudTemplate.update(order,Criteria.from(Order.class));
        return null;
    }

    @Override
    public List<OrderItemDTO> getApprovedOrderItems(Date date) throws Exception {
        Order order=new Order();
        List<Order> orders = crudTemplate.find(
                order,
                Criteria.from(Order.class)
                .where()
                .and("utime",Operator.GT,new java.sql.Date(date.getTime()))
                        .and("check_status",Operator.EQ,"1")
                .endWhere()
        );
        List<OrderItemDTO> list=new ArrayList<>();
        orders.forEach((entity)->{
            OrderItemDTO dto = new OrderItemDTO();
            BeanUtils.copyProperties(entity,dto);
        });
        return list;
    }

    @Override
    public List<OrderItemDTO> getLoanedOrderItems(Date date) throws Exception {
        Order order=new Order();
        List<Order> orders = crudTemplate.find(
                order,
                Criteria.from(Order.class)
                        .where()
                        .and("utime",Operator.GTE,new java.sql.Date(date.getTime()))
                        .endWhere()
        );
        List<OrderItemDTO> list=orders.stream().map((entity)->{
        	if(entity.getLoanDay() == null) {
            	return null;
            }
            OrderItemDTO dto = new OrderItemDTO();
            BeanUtils.copyProperties(entity,dto);
            return dto;
        }).filter((i)->{
        	return i!=null;
        }).collect(Collectors.toList());
        return list;
    }

    @Override
    public List<RepaymentDTO> getRepayment(Date date) throws Exception {
        RepayInfo returnInfo=new RepayInfo();

        List<RepayInfo> list = crudTemplate.find(
                returnInfo,
                Criteria.from(RepayInfo.class)
                        .where()
                        .and("utime",Operator.GTE, new java.sql.Date(date.getTime()))
                        .endWhere()
        );


        List<RepaymentDTO> ll=list.stream().map((info)->{
            RepaymentDTO dto = new RepaymentDTO();
            BeanUtils.copyProperties(info,dto);
            return dto;
        }).collect(Collectors.toList());

        return ll;
    }

    @Override
    public List<RepayPlanDTO> getRepayPlan(Long orderId) throws Exception {
        RepayPlan repayPlan=new RepayPlan();
        repayPlan.setOrderId(orderId);

        List<RepayPlan> list=crudTemplate.find(
                repayPlan,
                Criteria.from(RepayPlan.class)
                .where()
                .and("order_id",Operator.EQ)
                .endWhere()
                );
        List<RepayPlanDTO> ll = list.stream().map((plan)->{
            RepayPlanDTO dto = new RepayPlanDTO();
            BeanUtils.copyProperties(plan,dto);
            return dto;
        }).collect(Collectors.toList());
        return ll;
    }

    @Override
    public OrderItemDTO getOrder(String borrowNid) throws Exception {
        Order order=new Order();
        order.setBorrowNid(borrowNid);
        List<Order> l = crudTemplate.find(
                order,
                Criteria.from(Order.class)
                .where()
                .and("borrow_nid",Operator.EQ)
                .endWhere()
                );
        for(Order o : l){
            OrderItemDTO dto = new OrderItemDTO();
            BeanUtils.copyProperties(o,dto);
            return dto;
        }
        return null;
    }

    @Override
    public OrderItemDTO getOrder(Long orderId) throws Exception {
        Order order=new Order();
        order.setId(orderId);
        List<Order> l = crudTemplate.find(
                order,
                Criteria.from(Order.class)
                        .where()
                        .and("id",Operator.EQ)
                        .endWhere()
        );
        for(Order o : l){
            OrderItemDTO dto = new OrderItemDTO();
            BeanUtils.copyProperties(o,dto);
            return dto;
        }
        return null;
    }


    @Override
    public Map<String, Object> getOrderItems(QueryOrderItemDTO dto) throws Exception {
        User user = userService.getUsersByUserId(dto.getUserId()+"");
        Integer totalNum = null;
        Order record = new Order();
        Criteria criteria = Criteria.from(Order.class);
        Criteria criteria1 = Criteria.from(Order.class);
        Criteria criteria2 = Criteria.from(Order.class);
        Where where = criteria.where();
        Where where1 = criteria1.where();
        Where where2 = criteria2.where();
        where = where.and("osid",Operator.EQ);
        where1 = where1.and("osid",Operator.EQ);
        where2 = where2.and("osid",Operator.EQ);
        if(StringUtils.isNotEmpty(dto.getRenter())){
            record.setRenter(dto.getRenter());
            where = where.and("renter", Operator.EQ);
            where1 = where1.and("renter", Operator.EQ);
            where2 = where2.and("renter", Operator.EQ);
        }
        if(StringUtils.isNotEmpty(dto.getCellPhone())){
            record.setCellphone(dto.getCellPhone());
            where = where.and("cellPhone",Operator.EQ);
            where1 = where1.and("cellPhone", Operator.EQ);
            where2 = where2.and("cellPhone", Operator.EQ);
        }
        if(StringUtils.isNotEmpty(dto.getIdCardNo())){
            record.setIdCardNum(dto.getIdCardNo());
            where = where.and("idCardNum",Operator.EQ);
            where1 = where1.and("idCardNum", Operator.EQ);
            where2 = where2.and("idCardNum", Operator.EQ);
        }
        if((StringUtils.isNotEmpty(dto.getRegion()))&&(!"0".equals(dto.getRegion()))) {
            Region temptRecord = new Region();
            temptRecord.setId(new Long(dto.getRegion()));
            List<Region> regions = crudTemplate.find(temptRecord, Criteria.from(Region.class));
            if(!CollectionUtils.isEmpty(regions)) {
                Region temptRegion = regions.get(0);
                record.setRegion(temptRegion.getName());
                where = where.and("region",Operator.EQ);
                where1 = where1.and("region",Operator.EQ);
                where2 = where2.and("region",Operator.EQ);
            }
        }
        List<Order> res = new ArrayList<>();
        if(StringUtils.isNotEmpty(dto.getCanLoan())) {
            if ("1".equals(dto.getCanLoan())) {//初审可以放款
                record.setCheckStatus(0);
                where = where.and("check_status",Operator.EQ);
                List<Order> temptList = crudTemplate.find(record,where.endWhere());
                res.addAll(temptList);
            } else if ("2".equals(dto.getCanLoan())) {//初审拒绝放款
                record.setCheckStatus(1);
                where = where.and("check_status",Operator.EQ);
                List<Order> temptList = crudTemplate.find(record,where.endWhere());
                res.addAll(temptList);
            } else if ("3".equals(dto.getCanLoan())) {//次审可以放款
                record.setCheckStatus(2);
                where = where.and("check_status",Operator.EQ);
                List<Order> temptList = crudTemplate.find(record,where.endWhere());
                res.addAll(temptList);
            } else if ("4".equals(dto.getCanLoan())){//次审拒绝放款
                record.setCheckStatus(3);
                where = where.and("check_status",Operator.EQ);
                List<Order> temptList = crudTemplate.find(record,where.endWhere());
                res.addAll(temptList);
            } else {
                Order order1 = new Order();
                Order order2 = new Order();
                BeanUtils.copyProperties(record,order1);
                record.setCheckStatus(2);//复审能放款
                order2.setCheckStatus(0);//初审能放款
                where2 = where2.and("check_status",Operator.EQ);
                where1 = where1.and("check_status",Operator.IS);
                where = where.and("check_status",Operator.EQ);
                List<Order> temptList1 = crudTemplate.find(order1,where1.endWhere());
                List<Order> temptList2 = crudTemplate.find(record,where.endWhere());
                List<Order> temptList3 = crudTemplate.find(order2,where2.endWhere());
                res.addAll(temptList1);
                res.addAll(temptList2);
                res.addAll(temptList3);
            }
        }
        //对年龄进行筛选
        List<Order> ress = new ArrayList<>();
        if(!CollectionUtils.isEmpty(res)) {
            if((StringUtils.isNotEmpty(dto.getAge()))&&(!"0".equals(dto.getAge()))) {
                if ("1".equals(dto.getAge())) {//18～35包含35
                    for(Order rpt:res) {
                        ress.add(rpt);
                    }
                } else if ("2".equals(dto.getAge())) {//35～50包含50
                    for(Order rpt:res) {
                        ress.add(rpt);
                    }
                } else if ("3".equals(dto.getAge())) {//50以上
                    for(Order rpt:res) {
                        ress.add(rpt);
                    }
                }
            } else {
                ress.addAll(res);
            }
        } else {
            ress.addAll(res);
        }
        totalNum = ress.size();
        HashMap<String,Object> resMap = new HashMap<>();
        List<OrderItemVO> resultList = new ArrayList<>();
        if((totalNum != 0) && (totalNum > (dto.getPageSeq()-1)*dto.getPageSize())) {
            if(totalNum>=dto.getPageSeq()*dto.getPageSize()) {
                ress = ress.subList((dto.getPageSeq()-1)*dto.getPageSize(),dto.getPageSeq()*dto.getPageSize());
            } else {
                ress = ress.subList((dto.getPageSeq()-1)*dto.getPageSize(),totalNum);
            }
            if(ress!=null){
                for (Order order:ress) {
                    OrderItemVO vo = new OrderItemVO();
                    try {
                        //test
                        vo.setId(order.getId());
                        vo.setCheckStatus(order.getCheckStatus()==null?null:String.valueOf(order.getCheckStatus()));
                        //test
                        vo.setDebitaccount(dto.getBankSettings().getDebitAccount());
                        vo.setLoadaccount(dto.getBankSettings().getLoadAccount());
                        vo.setLoanRate(dto.getBankSettings().getLoanRate());
                        vo.setOverdueRange(dto.getBankSettings().getOverdueRange());
                        //假如此单被拒绝，找出拒绝原因
                        if((order.getCheckStatus()!=null)&&(("1".equals(order.getCheckStatus()+""))||("3".equals(order.getCheckStatus()+"")))) {
                            OrderCheckLog checkLog = checkLogService.selectCheckLogByOrderId(order.getId() + "");
                            if(checkLog!=null) {
                                OrderOperationCase orderRefuseCase = refusalService.selectByPrimaryKey(checkLog.getOperationId());
                                vo.setResusalEx(orderRefuseCase.getOperationExpression());
                            }
                        }
                    } catch (IllegalAccessException e) {
                        logger.error("ZznshOrderService#queryOrderItem",e);
                        throw new BizException(ErrorCode.SYS_EXCEPTION_BEAN_COPY);
                    } catch (InvocationTargetException e) {
                        logger.error("ZznshOrderService#queryOrderItem",e);
                        throw new BizException(ErrorCode.SYS_EXCEPTION_BEAN_COPY);
                    }
                    resultList.add(vo);
                }
            }
        }
        resMap.put("orderItems",resultList);
        resMap.put("total",totalNum+"");
        return resMap;
    }

    @Override
    public String importOrderItems(CommonsMultipartFile file) throws IOException {
        //将文件存入本地服务器
        //todo
        //将文件数据存入redisnpm
        InputStream inputStream = file.getInputStream();
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(inputStream);
        Long incrKey = cacheService.incr(HljnsConstants.REDIS_INC_KEY);
        String key = "ExcelList_" + incrKey;
        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++){
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                if(hssfRow != null) {
                    String idCard = CellUtil.getCellValue(hssfRow.getCell(2));
                    Order order = new Order();
                    order.setIdCardNum(idCard);
                    cacheService.hset(key,idCard,JSONObject.toJSONString(order));
                }
            }
        }
        try {
        	hssfWorkbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return incrKey+"";
    }

    @Override
    public String handleExcelData(String index, String account) throws Exception {
        //取出该账单号下的未处理数据
        Map<String, Order> needConfirmOrders = getNeedConfirmOrders(account);
        //取出excel数据
        String key = "ExcelList_" + index;
        Map<String, String> excelOrders = cacheService.hgetAll(key);
        Map<String, Order> map = new HashMap<String, Order>();
        if(excelOrders != null) {
            excelOrders.forEach((k,v)->{
                Order rpt = JSON.parseObject(v,Order.class);
                map.put(k,rpt);
            });
        }
        //核对
        List<String> idCards = new ArrayList<String>();
        List<String> confirmKeys = new ArrayList<String>();
        List<String> mapKeys = new ArrayList<String>();
        for (Map.Entry<String, Order> excelEntry:map.entrySet()) {
            for (Map.Entry<String, Order> needConfirmEntry:needConfirmOrders.entrySet()) {

                System.out.println(excelEntry.getKey());
                System.out.println(needConfirmEntry.getKey());

                if(excelEntry.getKey().equals(needConfirmEntry.getKey())) {
                    Order biZznshOrderOutRpt1 = excelEntry.getValue();
                    Order biZznshOrderOutRpt2 = needConfirmEntry.getValue();
                        //对比完了一个从map中去掉
//                        needConfirmOrders.remove(needConfirmEntry.getKey());
                        confirmKeys.add(needConfirmEntry.getKey());
                        idCards.add(excelEntry.getKey());
                    }
                }
            }
        return "";
        //再将没有对比的订单记录重新放入redis中
        /*String needConfirm_key="OrderList_"+account;
        for(String temptKey:confirmKeys) {
            cacheService.hdel(needConfirm_key,temptKey);
        }
        for(String temptKey:mapKeys) {
            cacheService.hdel(key,temptKey);
        }
        //将处理完的总数量存redis
        cacheService.set(HljnsConstants.HANDLED_EXCEL_DATA_NUMBER+index, idCards.size()+"");
        if(idCards.size() == 0) {
            return "";
        }
        List<Order> records = new ArrayList<>();
        for(String temptStr:idCards) {
            Order temptOrder = new Order();
            temptOrder.setStatus(1);
            temptOrder.setIdCardNum(temptStr);
            List<Order> temptOrders = crudTemplate.find(temptOrder, Criteria.from(Order.class).where().and("status", Operator.EQ).and("id_card_num", Operator.EQ).endWhere());
            if(temptOrders.size()>0){
                Order tOrder = temptOrders.get(0);
                tOrder.setStatus(1);
                records.add(tOrder);
            }
        }
        crudTemplate.update(records, Criteria.from(UserSession.class).fields("status"));*/
    }

    @Override
    public String queryOrderTaskStatus(String index) {
        String key = "ExcelList_" + index;
        Map<String, String> excelOrders = cacheService.hgetAll(key);
        if(excelOrders != null) {
            if(excelOrders.size() == 0)
                return "1";
        }
        return "0";
    }

    @Override
    public String queryCompletedOrders(String index) {
        return cacheService.get(HljnsConstants.HANDLED_EXCEL_DATA_NUMBER + index);
    }

    @Override
    public void confirmAccount(String account) throws Exception {
        Bill record = new Bill();
        record.setId(new Long(account));
        List<Bill> bills = crudTemplate.find(record, Criteria.from(Bill.class).where().and("id", Operator.EQ).endWhere());
        if(bills.size()>0) {
            Bill bill = bills.get(0);
            bill.setConfirmStatus(1);
            crudTemplate.update(bill,Criteria.from(Bill.class).fields("confirm_status"));
        }
    }

    @Override
    public void updateOrderStatus(UpdateOrderStatusDto dto) throws Exception {
        Order record = new Order();
        record.setCheckStatus(new Integer(dto.getStatus()));
        record.setId(new Long(dto.getId()));
        crudTemplate.update(record,Criteria.from(Order.class).fields("checkStatus"));
        //插入操作记录
        OrderCheckLog checkLog = new OrderCheckLog();
        if(("1".equals(dto.getStatus())) || ("3".equals(dto.getStatus()))) { //假如是拒绝的话 加入拒绝原因
            checkLog.setOperationId(dto.getRefusalId());
        }
        crudTemplate.save(checkLog);
        //重新统计订单总数和订单总金额
        reStaticOrders(dto.getAccount());
    }

    @Override
    public List<Map<String, String>> getRefuseCause() throws Exception {
        OrderOperationCase record = new OrderOperationCase();
        List<OrderOperationCase> biRefuseCases = crudTemplate.find(record,Criteria.from(OrderOperationCase.class));
        List<Map<String, String>> resList = new ArrayList<>();
        for(OrderOperationCase biRefuseCase : biRefuseCases) {
            Map<String, String> temptMap = new HashMap<>();
            temptMap.put("id", biRefuseCase.getId()+"");
            temptMap.put("refuseEx", biRefuseCase.getOperationExpression());
            resList.add(temptMap);
        }
        return resList;
    }

    @Override
    public void confirmAllPrePlotOrders(String billNumber) throws Exception {
        Order record1 = new Order();
//        record1.setOsid(new Long(billNumber));
        record1.setCheckStatus(0);
        Order record2 = new Order();
//        record2.setOsid(new Long(billNumber));
        record2.setCheckStatus(1);
        List<Order> orders1 = crudTemplate.find(record1, Criteria.from(Order.class).where().and("osid", Operator.EQ).and("check_status", Operator.EQ).endWhere());
        List<Order> orders2 = crudTemplate.find(record2, Criteria.from(Order.class).where().and("osid", Operator.EQ).and("check_status", Operator.EQ).endWhere());
        List<Order> orders = new ArrayList<Order>(){{
            this.addAll(orders1);
            this.addAll(orders2);
        }};
        for(Order temptOrder:orders) {
            temptOrder.setCheckStatus(2);
        }
        if(CollectionUtils.isEmpty(orders))
            return;
        crudTemplate.update(orders,Criteria.from(Order.class).fields("checkStatus"));
        //重新统计订单总数和订单总金额
        reStaticOrders(billNumber);
    }

    @Override
    public Map<String, String> getCanLoanNumAndQuitLoanNum(String userId, String account) throws Exception {
        User user = userService.getUsersByUserId(userId);
        Order order1 = new Order();
        Order order2 = new Order();
//        order1.setOsid(new Long(account));
//        order2.setOsid(new Long(account));
        if("1".equals(user.getPermissionSet()+"")) {
            order1.setCheckStatus(0);
            order2.setCheckStatus(1);
        } else if("2".equals(user.getPermissionSet()+"")) {
            order1.setCheckStatus(2);
            order2.setCheckStatus(3);
        } else {
            throw new Exception();
        }
        Long count1 = crudTemplate.count(order1, Criteria.from(Order.class).where().and("osid", Operator.EQ).and("check_status", Operator.EQ).endWhere());
        Long count2 = crudTemplate.count(order2, Criteria.from(Order.class).where().and("osid", Operator.EQ).and("check_status", Operator.EQ).endWhere());
        Map<String,String> resMap = new HashMap<>();
        resMap.put("canLoanNum",String.valueOf(count1));
        resMap.put("quitLoanNum",String.valueOf(count2));
        return resMap;
    }

    @Override
    public List<Order> queryOrderItemForExport(QueryOrderItemDTO dto) throws Exception {
        Order record = new Order();
//        record.setOsid(new Long(dto.getAccount()));
        List<Order> resultList1 = crudTemplate.find(record, Criteria.from(Order.class).where().and("osid", Operator.EQ).and("check_status", Operator.EQ, 0).endWhere());
        List<Order> resultList2 = crudTemplate.find(record, Criteria.from(Order.class).where().and("osid", Operator.EQ).and("check_status", Operator.EQ, 2).endWhere());
        List<Order> resultList3 = crudTemplate.find(record, Criteria.from(Order.class).where().and("osid", Operator.EQ).and("check_status",Operator.IS).endWhere());
        List<Order> resList = new ArrayList<Order>() {{
            this.addAll(resultList1);
            this.addAll(resultList2);
            this.addAll(resultList3);
        }};
        return resList;
    }

    @Override
    public Map<String, Order> getNeedConfirmOrders(String summaryId) {
        String key="OrderList_"+summaryId;
        Map<String,String> map = cacheService.hgetAll(key);
        Map<String,Order> res = new HashMap<>();
        if((map!=null) && (map.size() > 0)){
            map.forEach((k,v)->{
                Order rpt = JSON.parseObject(v,Order.class);
                res.put(k,rpt);
            });
            return res;
        }
        Order record = new Order();
//        record.setStatus(0);
//        record.setOsid(new Long(summaryId));
        List<Order> lst = null;
        try {
            lst = crudTemplate.find(record, Criteria.from(Order.class).where().and("osid", Operator.EQ).and("status",Operator.EQ).endWhere());
        } catch (Exception e) {
            e.printStackTrace();
        }

        lst.forEach((rpt)->{
            res.put(rpt.getIdCardNum(),rpt);
        });
        return res;
    }

    @Override
    public List<Order> queryAllOrders(String billNum) throws Exception {
        Order record = new Order();
//        record.setOsid(new Long(billNum));
        List<Order> orders = crudTemplate.find(record, Criteria.from(Order.class).where().and("osid", Operator.EQ).endWhere());
        return orders;
    }

    /**
     * 重新统计该账单下的可放款订单总数、总金额
     * @param account
     */
    public void reStaticOrders(String account) throws Exception {
        //统计订单数量和总金额
        Order example1 = new Order();
//        example1.setOsid(new Long(account));
        example1.setCheckStatus(2);
        Order example2 = new Order();
//        example2.setOsid(new Long(account));
        Long temptCount1 = crudTemplate.count(example1, Criteria.from(Order.class).where().and("osid", Operator.EQ).and("check_status", Operator.EQ).endWhere());
        Long temptCount2 = crudTemplate.count(example2,Criteria.from(Order.class).where().and("osid",Operator.EQ).and("check_status",Operator.IS).endWhere());
        Long ordersCount = temptCount1+temptCount2;
        //统计订单总额
        Order example3 = new Order();
        example3.setCheckStatus(2);
//        example3.setOsid(new Long(account));
        Order example4 = new Order();
//        example4.setOsid(new Long(account));
        BigDecimal sumAmount1 = crudTemplate.sum(example3, Criteria.from(Order.class).sum("iamount").where().and("osid", Operator.EQ).endWhere());
        BigDecimal sumAmount2 = crudTemplate.sum(example4,Criteria.from(Order.class).sum("iamount").where().and("osid",Operator.EQ).and("check_status",Operator.IS).endWhere());
        BigDecimal sumAmount = new BigDecimal((sumAmount1==null?0:sumAmount1.doubleValue())+(sumAmount2==null?0:sumAmount2.doubleValue()));
        Bill bill = new Bill();
        bill.setTotalAmount(sumAmount);
        bill.setOrderNum(ordersCount);
        bill.setId(new Long(account));
        crudTemplate.update(bill,Criteria.from(Bill.class));
    }

    @Override
    public void makeLoans() throws Exception {
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

        Map<String, String> map = bankContract.bankPayment(new ArrayList<BatchTransferInfosDTO>() {{
            this.add(batchTransferInfosDTO);
        }});

        System.out.println("--");
    }

    @Override
    public void hefs() {

        Order record = new Order();
        List<Order> entities = null;
        try {
            entities = crudTemplate.find(record,Criteria.from(Order.class));
            System.out.println(entities);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Order> subOrders = entities.subList(0, 20000);
        for (int i=1;i<=31;i++) {
            String osid = 2000+i+"";
            List<Order> temptList = new ArrayList<Order>(Arrays.asList(new Order[subOrders.size()]));
            Collections.copy(temptList, subOrders);
            //日期
            Date outDate = null;
            try
            {
                Calendar calendar = new GregorianCalendar();
                calendar.set(Calendar.YEAR,2017);
                calendar.set(Calendar.MONTH,9);
                calendar.set(Calendar.DAY_OF_MONTH,i);
                outDate = new java.sql.Date(calendar.getTime().getTime());
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
            for (int j=0;j<temptList.size();j++) {
                String primaryKey = 20000000+20000*(i-1)+j+"";
                Order temptOrder = temptList.get(j);
//                temptOrder.setBatchDate(outDate);
//                temptOrder.setOsid(new Long(osid));
                temptOrder.setId(new Long(primaryKey));
            }
            try {
                crudTemplate.save(temptList);
                System.out.println("----------------");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("------------");
            }
            System.out.println("------------------");
        }

    }


    @Override
    public void lixf() {
        Bill record = new Bill();
        record.setOrderSource("web");
        Bill t_bill = null;
        try {
            List<Bill> bills = crudTemplate.find(record, Criteria.from(Bill.class));
            t_bill = bills.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(int i=1;i<=30;i++) {
            Date temptDate = t_bill.getBatchDate();
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(temptDate);
            calendar.set(Calendar.YEAR,2017);
            calendar.set(Calendar.MONTH,9);
            calendar.set(Calendar.DAY_OF_MONTH,i);
            t_bill.setBatchDate(new java.sql.Date(calendar.getTime().getTime()));
            //primaryKey
            t_bill.setId(2000l+i);
            t_bill.setOrderSource("app1");
            try {
                crudTemplate.save(t_bill);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("------------");
            }
            System.out.println("------------");
        }
        System.out.println("------------");

    }

    @Override
    public List<Region> allRegion() throws Exception {
        Region record = new Region();
        List<Region> regions = crudTemplate.find(record, Criteria.from(Region.class));
        return regions;
    }

    @Override
    public Map<String, Object> allOrderDetail(OrderDetailDTO orderDetailDTO) {
        List<OrderItemVO> resList = new ArrayList<>();
        List<Order> orders = null;
        Order record = new Order();
        Long count = null;
        try {
            Limit limit = Page.resolve(orderDetailDTO.getPageSeq(), orderDetailDTO.getPageSize());
            orders = crudTemplate.find(record, Criteria.from(Order.class).where().endWhere().limit(limit));
            count = crudTemplate.count(record,Criteria.from(Order.class).where().endWhere());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("orderItems", orders);
        resMap.put("total",count);
        return resMap;
    }

    @Override
    public OrderDetailVO queryOrderById(QueryOrderDTO queryOrderDTO) throws Exception {
        Order record = new Order();
        record.setId(new Long(queryOrderDTO.getOrderId()));
        List<Order> orders = crudTemplate.find(record, Criteria.from(Order.class).where().endWhere());
        if((orders != null)&&(orders.size() > 0)) {
            Order order = orders.get(0);
            OrderDetailVO orderDetailVO = new OrderDetailVO();
            BeanUtils.copyProperties(order, orderDetailVO);
            //学历转换
            if(order.getEducational() != null) {
                if("0".equals(order.getEducational())) {
                    orderDetailVO.setEducational("大专以下");
                } else if("1".equals(order.getEducational())) {
                    orderDetailVO.setEducational("大专");
                } else if("2".equals(order.getEducational())) {
                    orderDetailVO.setEducational("本科");
                } else if("3".equals(order.getEducational())) {
                    orderDetailVO.setEducational("研究生及以上");
                }
            }
            //出生年月日
            orderDetailVO.setBirthday(order.getBirthday() != null?new SimpleDateFormat("yyyy-MM-dd").format(order.getBirthday()):"");
            if(order.getBirthday() != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String birthString = dateFormat.format(order.getBirthday());
                orderDetailVO.setBirthday(birthString);
            }
            //备注查询
            OrderMark markRecord = new OrderMark();
            markRecord.setOrderId(new Long(queryOrderDTO.getOrderId()));
            List<OrderMark> orderMarks = crudTemplate.find(markRecord, Criteria.from(OrderMark.class).where().endWhere());
            if((orderMarks != null) && (orderMarks.size() > 0)) {
                orderDetailVO.setDescription(orderMarks.get(0).getDescription());
            }
            return orderDetailVO;
        }
        return null;
    }

    @Override
    public PersonInfoVO queryPersonInfo(QueryPersonDTO queryPersonDTO) throws Exception {
        PersonInfo record1 = new PersonInfo();
        record1.setIdCardNum(queryPersonDTO.getIdCard());
        List<PersonInfo> personInfos = crudTemplate.find(record1, Criteria.from(PersonInfo.class).where().endWhere());
        if((personInfos != null)&&(personInfos.size() > 0)) {
            PersonInfo personInfo = personInfos.get(0);
            PersonInfoVO personInfoVO = new PersonInfoVO();
            BeanUtils.copyProperties(personInfo,personInfoVO);
            //查找联系人
            Contract record2 = new Contract();
            record2.setPersonId(personInfo.getId().toString());
            List<Contract> contracts = crudTemplate.find(record2, Criteria.from(Contract.class).where().endWhere());
            if((contracts != null)&&(contracts.size() > 0)) {
                List<ContractVO> contractVOS = new ArrayList<>();
                for(Contract contract: contracts) {
                    ContractVO contractVO = new ContractVO();
                    BeanUtils.copyProperties(contract,contractVO);
                    contractVOS.add(contractVO);
                }
                personInfoVO.setContractVOList(contractVOS);
            }
            return personInfoVO;
        }
        return null;
    }

    @Override
    public void updateOrderStatus(UpdateStatusDTO updateStatusDTO) throws Exception {
        Order record1 = new Order();
        record1.setId(new Long(updateStatusDTO.getOrderId()));
        List<Order> orders = crudTemplate.find(record1, Criteria.from(Order.class).where().endWhere());
        if((orders != null)&&(orders.size() > 0)) {
            //改变订单状态以及审核时间
            Order record2 = new Order();
            BeanUtils.copyProperties(orders.get(0),record2);
            if("1".equals(updateStatusDTO.getStatus())) {//通过
                record2.setCheckStatus(1);
            } else if("99".equals(updateStatusDTO.getStatus())){
                record2.setCheckStatus(99);
            } else {
            	record2.setCheckStatus(0);
            }
            record2.setCheckTime(new java.sql.Date(new Date().getTime()));
            crudTemplate.update(record2,Criteria.from(Order.class).where().endWhere());
            //保存操作记录
            OrderCheckLog record3 = new OrderCheckLog();
            record3.setUserId(updateStatusDTO.getUserId());
            record3.setOperationId(updateStatusDTO.getStatus());
            record3.setOrderId(updateStatusDTO.getOrderId());
            record3.setCtime(new java.sql.Date(new Date().getTime()));
            record3.setUtime(new java.sql.Date(new Date().getTime()));
            crudTemplate.save(record3);
            if("99".equals(updateStatusDTO.getStatus())) {//订单待处理
                return;
            }
            //操作完成之后抛q
            Map<String, String> messageMap = new HashMap<>();
            messageMap.put("checkStatus", record3.getOperationId()+"");
            messageMap.put("borrowNid",record2.getBorrowNid());
            new Thread(()->{
	            boolean flag = true;
            	while(flag) {
	            	try{
	            		rabbitTemplate.send("sis.order.topic.exchange", "sis.order.confirm", new Message(JSONObject.toJSON(messageMap).toString().getBytes(), new MessageProperties()));
	            		flag = false;
		            } catch (Exception e) {
		            	try {
		            		logger.error("sis.order.confirm error,after 5 sec retry",e);
							Thread.sleep(5000);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
							break;
						}
		            }
	            }
            }).start();
            
            //处理redis
            String managmentKey = String.format(RedisManageQueueKeyTemplate,updateStatusDTO.getUserId());
            if(redisTemplate.exists(managmentKey)) {//只是针对调查人员
                if(redisTemplate.get(managmentKey).equals(updateStatusDTO.getOrderId().toString())) {//判断redis里边所存在的订单跟当前的这笔订单是否是同一笔订单
                    redisTemplate.del(managmentKey);
                }
            }
        }
    }


    @Override
    public Map<String, Object> queryOrderByCondition(QueryOrderDTO dto) throws Exception {
        String statusStr = "";
        if(dto.getStatus() == null) {
            statusStr = "0,1";
        }  else  {
            statusStr = dto.getStatus()+"";
        }
        String[] statusArr = statusStr.split(",");
        List<Order> allList = new ArrayList<>();
        Arrays.stream(statusArr).forEach((s)-> {
            Order record = new Order();
            Where where = Criteria.from(Order.class).where();
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            if(dto.getFromDate() != null) {
                where = where.and("ctime",Operator.GTE,dto.getFromDate());
            }
            if(dto.getEndDate() != null) {
                where = where.and("ctime",Operator.LTE,dto.getEndDate());
            }
            if(StringUtils.isNotEmpty(dto.getName())) {
                where = where.and("renter",Operator.EQ,dto.getName());
            }
            if(StringUtils.isNotEmpty(dto.getIdCardNum())) {
                where = where.and("id_card_num",Operator.EQ,dto.getIdCardNum());
            }
            if(StringUtils.isNotEmpty(dto.getCellPhone())) {
                where = where.and("cellphone",Operator.EQ,dto.getCellPhone());
            }
            where = where.and("check_status", Operator.EQ, s);
            List<Order> list = null;
            try {
                list = crudTemplate.find(record, where.endWhere());
                allList.addAll(list);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        //分页
        List<Order> endList = new ArrayList<>();
        int count = allList.size();
        if((count != 0) && (count > (dto.getPageSeq()-1)*dto.getPageSize())) {
            if(count>=dto.getPageSeq()*dto.getPageSize()) {
                endList = allList.subList((dto.getPageSeq()-1)*dto.getPageSize(),dto.getPageSeq()*dto.getPageSize());
            } else {
                endList = allList.subList((dto.getPageSeq()-1)*dto.getPageSize(),count);
            }
        }
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("orderItems", endList);
        resMap.put("total",count);
        return resMap;
    }

    @Override
    public OrderItemDTO getOrder(UpdateStatusDTO dto) throws Exception {
        String managmentKey = String.format(RedisManageQueueKeyTemplate, dto.getUserId());
        if(redisTemplate.exists(managmentKey)) {
            String orderId = redisTemplate.get(managmentKey);
            Order record = new Order();
            record.setId(new Long(orderId));
            List<Order> orders = crudTemplate.find(record, Criteria.from(Order.class).where().endWhere());
            for(Order order : orders){
                if(order.getCheckStatus()!=-1){
                    break;
                }
                OrderItemDTO orderItemDTO = new OrderItemDTO();
                BeanUtils.copyProperties(order,orderItemDTO);
                return orderItemDTO;
            }
            //如果没有订单，则清缓存
            redisTemplate.del(managmentKey);
        }
        OrderItemDTO orderItemDTO = applicationAssignmentService.getOrder(dto.getUserId());
        if(orderItemDTO != null) {
            redisTemplate.set(managmentKey, orderItemDTO.getId().toString());
        }
        return orderItemDTO;
    }

@Override
    public void addOrUpdateMark(AddOrUpdateMarkDTO dto) throws Exception {
        OrderMark queryRecord = new OrderMark();
        queryRecord.setOrderId(dto.getOrderId());
        List<OrderMark> orderMarks = crudTemplate.find(queryRecord, Criteria.from(OrderMark.class).where().endWhere());
        if((orderMarks != null) && (orderMarks.size() > 0)) {
            OrderMark updateRecord = orderMarks.get(0);
            updateRecord.setDescription(dto.getDescription());
            crudTemplate.update(updateRecord,Criteria.from(OrderMark.class));
        } else {
            OrderMark addRecord = new OrderMark();
            addRecord.setDescription(dto.getDescription());
            addRecord.setOrderId(dto.getOrderId());
            crudTemplate.save(addRecord);
        }
    }

@Override
public List<Order> getDelayHandleOrders()throws Exception{
        Order record = new Order();
        record.setCheckStatus(99);
        List<Order> orders = crudTemplate.find(record, Criteria.from(Order.class).where().endWhere());
        if((orders == null)||(orders.size() == 0)) {
            return null;
        }
        return orders;
    }
}
