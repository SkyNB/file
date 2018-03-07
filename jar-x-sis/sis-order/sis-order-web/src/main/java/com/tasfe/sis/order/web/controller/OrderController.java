package com.tasfe.sis.order.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import javax.servlet.http.HttpServletResponse;

import com.tasfe.sis.order.model.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.tasfe.sis.bank.model.BankSettings;
import com.tasfe.sis.bank.service.BankSettingsService;
import com.tasfe.sis.base.configs.HljnsConstants;
import com.tasfe.sis.base.exception.BizException;
import com.tasfe.sis.base.model.code.ErrorCode;
import com.tasfe.sis.base.model.response.ResponseData;
import com.tasfe.sis.order.api.dto.OrderItemDTO;
import com.tasfe.sis.order.config.OrderConfig;
import com.tasfe.sis.order.entity.Order;
import com.tasfe.sis.order.entity.Region;
import com.tasfe.sis.order.model.vo.OrderDetailVO;
import com.tasfe.sis.order.model.vo.PersonInfoVO;
import com.tasfe.sis.order.service.OrderImpService;
import com.tasfe.sis.order.service.OrderService;
import com.tasfe.sis.order.util.FileUtil;
import com.tasfe.sis.user.service.SessionService;

/**
 * Created by Lait on 2017/8/8.
 */
@RestController
@RequestMapping("order")
public class OrderController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OrderService orderService;
    
    @Autowired
    private OrderImpService orderImpService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private BankSettingsService bankSettingsService;

    @Autowired
	private OrderConfig config;

    @RequestMapping(value = "/orderItems", method = RequestMethod.POST, produces = "application/json")
    public ResponseData<Map<String, Object>> getOrderItems(@RequestBody QueryOrderItemDTO queryOrderItemDTO) throws Exception {
        ResponseData responseData = new ResponseData();
        Long userId = sessionService.validateSessionCode(queryOrderItemDTO.getSessionCode());
        if (userId == null) {
            responseData.setError("sessionCode失效");
            return responseData;
        }
        queryOrderItemDTO.setUserId(userId);
        BankSettings bankSettings = bankSettingsService.getBankSettingsWithUserId(userId + "");
        queryOrderItemDTO.setBankSettings(bankSettings);
        //查看当前用户权限下的可以借款数量和拒绝放款数量
        Map<String, String> numMap = new HashMap<>();
//        numMap = orderService.getCanLoanNumAndQuitLoanNum(userId + "", queryOrderItemDTO.getAccount());
        Map<String, Object> resMap = orderService.getOrderItems(queryOrderItemDTO);
        responseData.setData(resMap);
        responseData.setStatus("1");
        responseData.setError("00000000");
        return responseData;
    }

    

    @RequestMapping(value = "/exportCustomers", method = RequestMethod.GET, produces = "application/json")
    public byte[] exportCustomers(@RequestParam String date, @RequestParam String billNumber, @RequestParam String sessionCode, HttpServletResponse response) throws Exception {
        QueryOrderItemDTO dto = new QueryOrderItemDTO();
        dto.setAccount(billNumber);
        Long userId = sessionService.validateSessionCode(sessionCode);
        if (userId == null) {
            throw new BizException(ErrorCode.RESULT_TOKEN_EXPIRE);
        }
        BankSettings bankSettings = bankSettingsService.getBankSettingsWithUserId(userId + "");
        dto.setBankSettings(bankSettings);
        List<Order> list = null;
        list = orderService.queryOrderItemForExport(dto);
        OutputStream fOut = null;
        byte[] bfile = {};
        // 产生工作簿对象
        HSSFWorkbook workbook = new HSSFWorkbook();
        try {
            fOut = response.getOutputStream();
            //产生工作表对象
            HSSFSheet sheet = workbook.createSheet();
            workbook.setSheetName(0, "customers");
            //构造表格
            if (CollectionUtils.isNotEmpty(list)) {
                generateCustomerFirstLine(sheet);
                generateCustomerDataLines(sheet, list, dto.getBankSettings());
            } else {
                throw new BizException(ErrorCode.RESULT_NOTHING);
            }
            //表格对象转换成流
            workbook.write(fOut);
            //转换成byte
            fOut.write(bfile);
            String filename = "customers_" + date + ".xls";
            response.setHeader("Content-Disposition",
                    "attachment; filename=\"" + filename + "\"");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/octet-stream");

        } catch (Exception e) {
            logger.error("导出字段异常", e);
        } finally {
            try {
            	workbook.close();
            	fOut.flush();
                fOut.close();
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bfile;
    }

    @RequestMapping(value = "/exportOrderItems",  method = RequestMethod.GET, produces = "application/octet-stream")
    @ResponseBody
    public byte[] exportOrderItems(@RequestParam String billNumber,@RequestParam String date,@RequestParam String sessionCode,HttpServletResponse response) throws Exception {
        QueryOrderItemDTO dto = new QueryOrderItemDTO();
        dto.setAccount(billNumber);
        dto.setSessionCode(sessionCode);
        Long userId = sessionService.validateSessionCode(sessionCode);
        if (userId == null) {
            throw new BizException(ErrorCode.RESULT_TOKEN_EXPIRE);
        }
        BankSettings bankSettings = bankSettingsService.getBankSettingsWithUserId(userId + "");
        dto.setBankSettings(bankSettings);
        List<Order> list = null;
        if (HljnsConstants.CUSTOMER_CODE_ZZNSH.equals(dto.getBankSettings().getCustomerCode())){
            //获取模块字段
            list = orderService.queryOrderItemForExport(dto);
        }else{
            /**
             *  若新增客户，在此添加代码（service实现OrderContract的接口），此处类似工厂方法
             */
            throw new BizException(ErrorCode.RESULT_NOTHING);

        }
        OutputStream fOut = null;
        byte[] bfile={};
     // 产生工作簿对象
        HSSFWorkbook workbook = new HSSFWorkbook();
        try
        {
            fOut=response.getOutputStream();
            //产生工作表对象
            HSSFSheet sheet = workbook.createSheet();
            workbook.setSheetName(0,"orderItems");
            //构造表格
            if (CollectionUtils.isNotEmpty(list)){
                generateOrderItemFirstLine(sheet);
                generateOrderItemDataLines(sheet,list,dto.getBankSettings());
            }else{
                throw new BizException(ErrorCode.RESULT_NOTHING);
            }
            //表格对象转换成流
            workbook.write(fOut);
            //转换成byte
            fOut.write(bfile);
            String filename="orderItems_"+date+".xls";
            response.setHeader("Content-Disposition",
                    "attachment; filename=\""+filename+"\"");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type","application/octet-stream");
        }
        catch (Exception e)
        {
            logger.error("导出字段异常",e);
        }
        finally
        {
            try
            {
            	workbook.close();
                fOut.flush();
                fOut.close();
            }
            catch (IOException e)
            {}
        }
        return bfile;
    }

    private void generateOrderItemDataLines(HSSFSheet sheet, List<Order> list,BankSettings customerSetting){
        Order orderItemVO = null;
        String agricultureRelated = customerSetting.getAgriculturerelated();
        Double loanRate = new Double(customerSetting.getLoanRate().toString());
        Double overdueRange = new Double(customerSetting.getOverdueRange().doubleValue());
        String debitAccount = customerSetting.getDebitAccount();
        String loadAccount = customerSetting.getLoadAccount();
        String isCoorperate = customerSetting.getIsCoorperate();
        String coorperateCode = customerSetting.getCoorperateCode();
        String productName = customerSetting.getProductName();
        String subproductcode = customerSetting.getSubProductCode();
        SimpleDateFormat sdf= new SimpleDateFormat(HljnsConstants.DATE_FOMAT_1);
        HSSFRow row = null;
        for (int i = 0; i < list.size(); i++)
        {
            orderItemVO=list.get(i);
            row = sheet.createRow(i+1);//创建一行
            row.createCell(0,CellType.STRING).setCellValue(orderItemVO.getIdCardNum());
            row.createCell(2,CellType.STRING).setCellValue(agricultureRelated);
            row.createCell(5,CellType.STRING).setCellValue(loanRate);
            row.createCell(6,CellType.NUMERIC).setCellValue(overdueRange);
            row.createCell(7,CellType.STRING).setCellValue(debitAccount);
            row.createCell(8,CellType.STRING).setCellValue(loadAccount);
            row.createCell(9,CellType.STRING).setCellValue(isCoorperate);
            row.createCell(10,CellType.STRING).setCellValue(coorperateCode);
            row.createCell(11,CellType.STRING).setCellValue(productName);
            row.createCell(12,CellType.STRING).setCellValue(subproductcode);
        }
    }

    private void generateOrderItemFirstLine(HSSFSheet sheet) {
        HSSFRow row = sheet.createRow(0);//创建一行
        row.createCell((int) 0, CellType.STRING).setCellValue("证件号码");
        row.createCell(1, CellType.STRING).setCellValue("申请金额");
        row.createCell(2, CellType.STRING).setCellValue("是否涉农");
        row.createCell(3, CellType.STRING).setCellValue("开始时间");
        row.createCell(4, CellType.STRING).setCellValue("结束时间");
        row.createCell(5, CellType.STRING).setCellValue("款利率[‰]");
        row.createCell(6, CellType.STRING).setCellValue("逾期利率浮动比例[%]");
        row.createCell(7, CellType.STRING).setCellValue("扣款账号");
        row.createCell(8, CellType.STRING).setCellValue("发放账号");
        row.createCell(9, CellType.STRING).setCellValue("是否合作项目");
        row.createCell(10, CellType.STRING).setCellValue("合作项目编号");
        row.createCell(11, CellType.STRING).setCellValue("产品名称");
        row.createCell(12, CellType.STRING).setCellValue("子产品编号");
    }

    /**
     * 生成表头
     *
     * @param sheet
     */
    private void generateCustomerFirstLine(HSSFSheet sheet) {
        HSSFRow row = sheet.createRow(0);//创建一行
        row.createCell((int) 0, CellType.STRING).setCellValue("客户名称");
        row.createCell(1, CellType.STRING).setCellValue("证件号码");
        row.createCell(2, CellType.STRING).setCellValue("婚姻状况");
        row.createCell(3, CellType.STRING).setCellValue("手机号码");
        row.createCell(4, CellType.STRING).setCellValue("家庭住址");
        row.createCell(5, CellType.STRING).setCellValue("客户类型");
    }

    /**
     * 填充数据
     *
     * @param sheet
     * @param list
     */
    private void generateCustomerDataLines(HSSFSheet sheet, List<Order> list, BankSettings customerSetting) {
        Order orderItemVO = null;
        String customerType = customerSetting.getBorrowCustomerType();
        HSSFRow row = null;
        for (int i = 0; i < list.size(); i++) {
            orderItemVO = list.get(i);
            row = sheet.createRow(i + 1);//创建一行
            row.createCell(0, CellType.STRING).setCellValue(orderItemVO.getRenter());
            row.createCell(1, CellType.STRING).setCellValue(orderItemVO.getIdCardNum());
            row.createCell(2, CellType.STRING).setCellValue(orderItemVO.getMarried());
            row.createCell(3, CellType.STRING).setCellValue(orderItemVO.getCellphone());
            row.createCell(5, CellType.STRING).setCellValue(customerType);
        }
    }

    @RequestMapping(value = "/importOrderItems", method = RequestMethod.POST, produces = "application/json")
    public ResponseData<String> importOrderItems(@RequestParam("file") CommonsMultipartFile file) {
        ResponseData responseData = new ResponseData();
        String incre_index = "";
        try {
            incre_index = orderService.importOrderItems(file);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        responseData.setStatus("1");
        responseData.setError("00000000");
        return responseData.setData(incre_index);
    }

    @RequestMapping(value = "/handleOrderItems", method = RequestMethod.POST, produces = "application/json")
    public ResponseData<Void> handleOrderItems(@RequestBody HandleOrderDto handleOrderDto) {
        ResponseData responseData = new ResponseData();
        CompletableFuture.runAsync(() -> {
            try {
                orderService.handleExcelData(handleOrderDto.getIncr_index(), handleOrderDto.getAccount());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        responseData.setStatus("1");
        responseData.setError("00000000");
        return responseData;
    }

    @RequestMapping(value = "/queryTaskStatus", method = RequestMethod.POST, produces = "application/json")
    public ResponseData<String> queryTaskStatus(@RequestBody QueryTaskStatusDto dto) {
        ResponseData responseData = new ResponseData();
        orderService.queryOrderTaskStatus(dto.getIndex());
        responseData.setStatus("1");
        responseData.setError("00000000");
        return responseData;
    }

    @RequestMapping(value = "/queryCompleteNumber", method = RequestMethod.POST, produces = "application/json")
    public ResponseData<String> queryCompleteNumber(@RequestBody QueryTaskStatusDto dto) {
        ResponseData responseData = new ResponseData();
        String index = orderService.queryCompletedOrders(dto.getIndex());
        responseData.setData(index);
        responseData.setStatus("1");
        responseData.setError("00000000");
        return responseData;
    }

    @RequestMapping(value = "/completeAccount", method = RequestMethod.POST, produces = "application/json")
    public ResponseData<Void> completeAccount(@RequestBody HandleAccountDto dto) throws Exception {
        orderService.confirmAccount(dto.getAccount());
        ResponseData responseData = new ResponseData();
        responseData.setStatus("1");
        responseData.setError("00000000");
        return responseData;
    }

    @RequestMapping(value = "/updateCheckStatus", method = RequestMethod.POST, produces = "application/json")
    public ResponseData<Void> updateCheckStatus(@RequestBody UpdateOrderStatusDto dto) throws Exception {
        ResponseData responseData = new ResponseData();
        Long userId = sessionService.validateSessionCode(dto.getSessionCode());
        if (userId == null) {
            responseData.setError("sessionCode失效");
            return responseData;
        }
        BankSettings bankSettings = bankSettingsService.getBankSettingsWithUserId(String.valueOf(userId));
        dto.setBankSettings(bankSettings);
        orderService.updateOrderStatus(dto);
        responseData.setStatus("1");
        responseData.setError("00000000");
        return responseData;
    }

    @RequestMapping(value = "/confirmAllPrePlotOrders", method = RequestMethod.POST, produces = "application/json")
    public ResponseData<Void> confirmAllPrePlotOrders(@RequestBody HandleAccountDto dto) throws Exception {
        ResponseData responseData = new ResponseData();
        orderService.confirmAllPrePlotOrders(dto.getAccount());
        responseData.setStatus("1");
        responseData.setError("00000000");
        return responseData;
    }

    @RequestMapping(value = "/downloadAssetPackage",  method = RequestMethod.GET, produces = "application/octet-stream")
    @ResponseBody
    public byte[] downloadAssetPackage(@RequestParam String billNumber,@RequestParam String date,@RequestParam String sessionCode,HttpServletResponse response) throws Exception {
        QueryOrderItemDTO dto = new QueryOrderItemDTO();
        Long userId = sessionService.validateSessionCode(sessionCode);
        if (userId == null) {
            throw new Exception();
        }
        BankSettings bankSettings = bankSettingsService.getBankSettingsWithUserId(String.valueOf(userId));
        dto.setBankSettings(bankSettings);
        List<Order> list = orderService.queryAllOrders(billNumber);
        OutputStream fOut = null;
        byte[] bfile={};
     // 产生工作簿对象
        HSSFWorkbook workbook = new HSSFWorkbook();
        try
        {
            fOut = response.getOutputStream();
            //产生工作表对象
            HSSFSheet sheet = workbook.createSheet();
            workbook.setSheetName(0,"assetPackage");
            //构造表格
            if (CollectionUtils.isNotEmpty(list)){
                generateAssetPackageFirstLine(sheet);

                generateAssetPackageDataLines(sheet,list,dto.getBankSettings());
            }else{
                throw new BizException(ErrorCode.RESULT_NOTHING);
            }
            //表格对象转换成流
            workbook.write(fOut);
            //转换成byte
            fOut.write(bfile);
            String filename="customers_"+date+".xls";
            response.setHeader("Content-Disposition",
                    "attachment; filename=\""+filename+"\"");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type","application/octet-stream");
        }
        catch (Exception e)
        {
            logger.error("导出字段异常",e);
        }
        finally
        {
            try
            {
            	workbook.close();
                fOut.flush();
                fOut.close();
            }
            catch (IOException e)
            {}
        }
        return bfile;
    }

    private void generateAssetPackageFirstLine(HSSFSheet sheet){
        HSSFRow row = sheet.createRow(0);//创建一行
        row.createCell((int)0,CellType.STRING).setCellValue("客户名称");
        row.createCell(1,CellType.STRING).setCellValue("证件号码");
        row.createCell(2,CellType.STRING).setCellValue("婚姻状况");
        row.createCell(3,CellType.STRING).setCellValue("手机号码");
        row.createCell(4,CellType.STRING).setCellValue("家庭住址");
        row.createCell(5,CellType.STRING).setCellValue("客户类型");
        row.createCell(6,CellType.STRING).setCellValue("申请金额");
        row.createCell(7,CellType.STRING).setCellValue("是否涉农");
        row.createCell(8,CellType.STRING).setCellValue("开始时间");
        row.createCell(9,CellType.STRING).setCellValue("结束时间");
        row.createCell(10,CellType.STRING).setCellValue("款利率[‰]");
        row.createCell(11,CellType.STRING).setCellValue("逾期利率浮动比例[%]");
        row.createCell(12,CellType.STRING).setCellValue("扣款账号");
        row.createCell(13,CellType.STRING).setCellValue("发放账号");
        row.createCell(14,CellType.STRING).setCellValue("是否合作项目");
        row.createCell(15,CellType.STRING).setCellValue("合作项目编号");
        row.createCell(16,CellType.STRING).setCellValue("产品名称");
        row.createCell(17,CellType.STRING).setCellValue("子产品编号");

    }

    private void generateAssetPackageDataLines(HSSFSheet sheet, List<Order> list, BankSettings customerSetting){
        Order orderItemVO = null;
        String agricultureRelated = customerSetting.getAgriculturerelated();
        Double loanRate = new Double(customerSetting.getLoanRate().toString());
        Double overdueRange = new Double(customerSetting.getOverdueRange().doubleValue());
        String debitAccount = customerSetting.getDebitAccount();
        String loadAccount = customerSetting.getLoadAccount();
        String isCoorperate = customerSetting.getIsCoorperate();
        String coorperateCode = customerSetting.getCoorperateCode();
        String productName = customerSetting.getProductName();
        String subproductcode = customerSetting.getSubProductCode();
        SimpleDateFormat sdf= new SimpleDateFormat(HljnsConstants.DATE_FOMAT_1);
        String customerType = customerSetting.getBorrowCustomerType();
        HSSFRow row = null;
        for (int i = 0; i < list.size(); i++)
        {
            orderItemVO=list.get(i);
            row = sheet.createRow(i+1);//创建一行
            row.createCell(0,CellType.STRING).setCellValue(orderItemVO.getRenter());
            row.createCell(1,CellType.STRING).setCellValue(orderItemVO.getIdCardNum());
            row.createCell(2,CellType.STRING).setCellValue(orderItemVO.getMarried());
            row.createCell(3,CellType.STRING).setCellValue(orderItemVO.getCellphone());
            row.createCell(7,CellType.STRING).setCellValue(agricultureRelated);
            row.createCell(10,CellType.STRING).setCellValue(loanRate);
            row.createCell(11,CellType.NUMERIC).setCellValue(overdueRange);
            row.createCell(12,CellType.STRING).setCellValue(debitAccount);
            row.createCell(13,CellType.STRING).setCellValue(loadAccount);
            row.createCell(14,CellType.STRING).setCellValue(isCoorperate);
            row.createCell(15,CellType.STRING).setCellValue(coorperateCode);
            row.createCell(16,CellType.STRING).setCellValue(productName);
            row.createCell(17,CellType.STRING).setCellValue(subproductcode);
        }
    }

    @RequestMapping(value = "/makeLoans", method = RequestMethod.POST, produces = "application/json")
    public ResponseData<Void> makeLoans() throws Exception {
        orderService.makeLoans();
        return null;
    }

    @RequestMapping(value = "/hefs", method = RequestMethod.POST, produces = "application/json")
    public ResponseData<Void> hefs() throws Exception {
        orderService.hefs();
        return null;
    }

    @RequestMapping(value = "/lixf", method = RequestMethod.POST, produces = "application/json")
    public ResponseData<Void> lixf() throws Exception {
        orderService.lixf();
        return null;
    }

    @RequestMapping(value = "/allRegion", method = RequestMethod.POST, produces = "application/json")
    public ResponseData<List<Region>> allRegion() throws Exception {
        List<Region> regions = orderService.allRegion();
        ResponseData<List<Region>> responseData = new ResponseData<>();
        responseData.setData(regions);
        responseData.setStatus("1");
        responseData.setError("00000000");
        return responseData;
    }

    @RequestMapping(value = "/allOrderDetail", method = RequestMethod.POST, produces = "application/json")
    public ResponseData<Map<String, Object>> allOrderDetail(@RequestBody OrderDetailDTO orderDetailDTO) throws Exception {
        ResponseData<Map<String, Object>> responseData = new ResponseData<>();
        Map<String, Object> resMap = orderService.allOrderDetail(orderDetailDTO);
        responseData.setData(resMap);
        responseData.setStatus("1");
        responseData.setError("00000000");
        return responseData;
    }


    @RequestMapping(value = "/queryOrder", method = RequestMethod.POST, produces = "application/json")
    public ResponseData<OrderDetailVO> queryOrder(@RequestBody QueryOrderDTO queryOrderDTO) throws Exception {
        OrderDetailVO order = orderService.queryOrderById(queryOrderDTO);
        ResponseData responseData = new ResponseData();
        if(order != null) {
            responseData.setStatus("1");
        } else {
            responseData.setStatus("0");
        }
        responseData.setData(order);
        return responseData;
    }

    @RequestMapping(value = "/queryPersonDetail", method = RequestMethod.POST, produces = "application/json")
    public ResponseData<PersonInfoVO> queryPersonDetail(@RequestBody QueryPersonDTO queryPersonDTO) throws Exception {
        PersonInfoVO personInfoVO = orderService.queryPersonInfo(queryPersonDTO);
        ResponseData responseData = new ResponseData();
        responseData.setData(personInfoVO);
        if(personInfoVO != null) {
            responseData.setStatus("1");
        } else {
            responseData.setStatus("0");
        }
        return responseData;
    }

    @RequestMapping(value = "/allOperationCase", method = RequestMethod.POST, produces = "application/json")
    public ResponseData<List<Map<String, String>>> getResuseCause() throws Exception {
        ResponseData responseData = new ResponseData();
        List<Map<String, String>> refuseCause = orderService.getRefuseCause();
        responseData.setData(refuseCause);
        responseData.setStatus("1");
        responseData.setError("00000000");
        return responseData;
    }

    @RequestMapping(value = "/updateOrderStatus", method = RequestMethod.POST, produces = "application/json")
    public ResponseData<PersonInfoVO> updateOrderStatus(@RequestBody UpdateStatusDTO updateStatusDTO) throws Exception {
        ResponseData responseData = new ResponseData();
        Long userId = sessionService.validateSessionCode(updateStatusDTO.getSessionCode());
        if (userId == null) {
            responseData.setError("sessionCode失效");
            return responseData;
        }
        updateStatusDTO.setUserId(userId);
        orderService.updateOrderStatus(updateStatusDTO);
        responseData.setStatus("1");
        return responseData;
    }

    /**
     * 上传文件
     * @param file
     * @return
     */
    @RequestMapping(value = "/importOutFile", method = RequestMethod.POST, produces = "application/json")
    public ResponseData<String> importOutFile(@RequestParam("file") MultipartFile file) {
        ResponseData responseData = new ResponseData();
        try {
            java.io.File tempFile = new java.io.File(config.ORDERTRANSACTION_TEMPFILEPATH+UUID.randomUUID().toString());
            file.transferTo(tempFile);
            logger.info(file.getName());
            orderImpService.importOutFile(tempFile); 
        } catch (Exception e) {
            e.printStackTrace();
        }
        responseData.setStatus("1");
        return responseData;
    }
    
    /**
     * 上传内-》外 订单文件
     * @param file
     * @return
     */
    @RequestMapping(value = "/importInnerFile", method = RequestMethod.POST, produces = "application/json")
    public ResponseData<String> importOrderFile(@RequestParam("file") MultipartFile file) {
        ResponseData responseData = new ResponseData();
        try {
            java.io.File tempFile = new java.io.File(config.ORDERTRANSACTION_TEMPFILEPATH+UUID.randomUUID().toString());
            file.transferTo(tempFile);
            orderImpService.importInnerFile(tempFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        responseData.setStatus("1");
        return responseData;
    }
    /**
     * 内前置下载文件
     * @param
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/downInnerFile",  method = RequestMethod.GET, produces = "application/octet-stream")
    @ResponseBody
    public void downInnerFile(HttpServletResponse response,@RequestParam String startDate,@RequestParam String endDate) throws Exception {
    	File file =orderImpService.downInnerFile(startDate,endDate);
    	try {
	    	response.setContentType("application/octet-stream");
	    	response.setHeader("Content-Disposition", "attachment;filename=".concat(endDate.concat("_IN"+config.ORDERTRANSACTION_FILEEXT)));
	    	OutputStream output = response.getOutputStream();
	    	byte [] fileByte = FileUtil.File2byte(file);
	    	output.write(fileByte);
	    	output.flush();
	    	return ;
    	}catch(Exception e) {
    		e.printStackTrace();
    	}finally {
    		FileUtil.deleteFile(file);
    	}
    }
    /**
     * 外前置下载文件
     * @param
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/downOutFile",  method = RequestMethod.GET, produces = "application/octet-stream")
    @ResponseBody
    public void downOutFile(HttpServletResponse response,@RequestParam String startDate,@RequestParam String endDate) throws Exception {
    	File file =orderImpService.downOutFile(startDate,endDate);
    	try {
	    	response.setContentType("application/octet-stream");
	    	response.setHeader("Content-Disposition", "attachment;filename=".concat(endDate.concat("_OUT"+config.ORDERTRANSACTION_FILEEXT)));
	    	OutputStream output = response.getOutputStream();
	    	byte [] fileByte = FileUtil.File2byte(file);
	    	output.write(fileByte);
	    	output.flush();
	    	return;
    	}catch(Exception e) {
    		e.printStackTrace();
    	}finally {
    		FileUtil.deleteFile(file);
    	}
    }
    
    /**
     * 内前置文件生成文件
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/reqReptile", method = RequestMethod.GET, produces = "application/json")
    public ResponseData innerFileReptile(@RequestParam String startDate) throws Exception {
        ResponseData responseData = new ResponseData();
        orderImpService.innerFileReptile(startDate);
        responseData.setStatus("1");
        return responseData;
    }
    
    /**
     * 查询征信报告
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/queryCredit", method = RequestMethod.POST, produces = "application/json")
    public ResponseData<String> queryCredit(HttpServletResponse response,@RequestBody QueryCreditDto dto) throws Exception {
    	ResponseData<String> responseData = new ResponseData<String>();
    	String fileUrl =orderImpService.queryCrccReport(dto.getName(), dto.getIdCard());
        if(fileUrl!=null) {
        	responseData.setStatus("1");
            responseData.setData(fileUrl);
        }else {
        	responseData.setError("无征信账号可用!");
            responseData.setStatus("0");
        }
        return responseData;
    }
    
    /**
     * 定时清理人行征信的请求次数
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/cleanTaskCredit", method = RequestMethod.GET, produces = "application/json")
    public ResponseData<String> cleanTaskCredit() throws Exception {
    	ResponseData<String> responseData = new ResponseData<String>();
    	orderImpService.cleanTaskCredit();
    	responseData.setError("定时清理人行征信的请求次数");
    	responseData.setStatus("1");
        return responseData;
    }
    /**
     * 定时查询征信报告
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/timeTaskCredit", method = RequestMethod.GET, produces = "application/json")
    public ResponseData<String> timeTaskCredit() throws Exception {
    	ResponseData<String> responseData = new ResponseData<String>();
    	orderImpService.timeTaskCredit();
    	responseData.setError("定时请求人行征信");
    	responseData.setStatus("1");
        return responseData;
    }

    @RequestMapping(value = "/queryOrderByCondition", method = RequestMethod.POST, produces = "application/json")
    public ResponseData<Map<String, Object>> queryOrderByCondition(@RequestBody QueryOrderDTO dto) throws Exception {
        ResponseData responseData = new ResponseData();
        Long userId = sessionService.validateSessionCode(dto.getSessionCode());
        if (userId == null) {
            responseData.setError("sessionCode失效");
            responseData.setStatus("0");
            return responseData;
        }
        dto.setUserId(userId);
        //按照条件搜索订单
        Map<String, Object> resMap = orderService.queryOrderByCondition(dto);
        responseData.setStatus("1");
        responseData.setData(resMap);
        return responseData;
    }

    @RequestMapping(value = "/getToBeAssignedOrder", method = RequestMethod.POST, produces = "application/json")
    public ResponseData<OrderItemDTO> getToBeAssignedOrder(@RequestBody UpdateStatusDTO dto) throws Exception {
        ResponseData responseData = new ResponseData();
        Long userId = sessionService.validateSessionCode(dto.getSessionCode());
        if (userId == null) {
            responseData.setError("sessionCode失效");
            responseData.setStatus("0");
            return responseData;
        }
        dto.setUserId(userId);
        OrderItemDTO orderItemDTO = orderService.getOrder(dto);
        if(orderItemDTO != null) {
            responseData.setStatus("1");
        } else {
            responseData.setStatus("0");
        }
        responseData.setData(orderItemDTO);
        return responseData;
    }

@RequestMapping(value = "/addOrUpdateMark", method = RequestMethod.POST, produces = "application/json")
    public ResponseData<Void> addOrUpdateMark(@RequestBody AddOrUpdateMarkDTO dto) throws Exception {
        ResponseData responseData = new ResponseData();
        orderService.addOrUpdateMark(dto);
        responseData.setStatus("1");
        return responseData;
    }

@RequestMapping(value = "/getDelayHandleOrders", method = RequestMethod.POST, produces = "application/json")
    public ResponseData<List<Order>> getDelayHandleOrders() throws Exception {
        ResponseData responseData = new ResponseData();
        List<Order> orders =  orderService.getDelayHandleOrders();
        responseData.setStatus("1");
        responseData.setData(orders);
        return responseData;
    }
}