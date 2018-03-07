package com.tasfe.sis.order.service;

import com.tasfe.sis.order.api.dto.OrderItemDTO;
import com.tasfe.sis.order.entity.Order;
import com.tasfe.sis.order.entity.Region;
import com.tasfe.sis.order.model.*;
import com.tasfe.sis.order.model.vo.OrderDetailVO;
import com.tasfe.sis.order.model.vo.PersonInfoVO;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 内部的服务接口,不提供对外暴露
 * Created by Lait on 2017/7/27.
 */
public interface OrderService {

    public Map<String, Object> getOrderItems(QueryOrderItemDTO queryOrderItemDTO) throws Exception;

    public String importOrderItems(CommonsMultipartFile file) throws IOException;

    public String handleExcelData(String index,String account) throws Exception;

    public String queryOrderTaskStatus(String index);

    public String queryCompletedOrders(String index);

    public void confirmAccount(String account) throws Exception;

    public void updateOrderStatus(UpdateOrderStatusDto dto) throws Exception;

    public List<Map<String, String>> getRefuseCause() throws Exception;

    public void confirmAllPrePlotOrders(String billNumber) throws Exception;

    /**
     * 查看当前用户权限下的可以借款数量和拒绝放款数量
     */
    public Map<String, String> getCanLoanNumAndQuitLoanNum(String userId, String account) throws Exception;

    public List<Order> queryOrderItemForExport(QueryOrderItemDTO dto) throws Exception;

    /**
     * 获取代确认订单列表.<br>
     *
     * @return
     */
    public Map<String,Order> getNeedConfirmOrders(String summaryId);


    public List<Order> queryAllOrders(String billNum) throws Exception;

    public void makeLoans() throws Exception;

    public void hefs();

    public void lixf();

    public List<Region> allRegion() throws Exception;

    public Map<String, Object> allOrderDetail(OrderDetailDTO orderDetailDTO);

    public OrderDetailVO queryOrderById(QueryOrderDTO queryOrderDTO) throws Exception;

    public PersonInfoVO queryPersonInfo(QueryPersonDTO queryPersonDTO) throws Exception;

    public void updateOrderStatus(UpdateStatusDTO updateStatusDTO) throws Exception;

    public Map<String, Object> queryOrderByCondition(QueryOrderDTO dto) throws Exception;

    public OrderItemDTO getOrder(UpdateStatusDTO dto) throws Exception;

    public OrderItemDTO getOrder(Long orderId) throws Exception;

    public void addOrUpdateMark(AddOrUpdateMarkDTO dto) throws Exception;

    public List<Order> getDelayHandleOrders()throws Exception;

}
