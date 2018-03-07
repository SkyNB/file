package com.tasfe.sis.order.service;

import java.io.File;

/**
 * 内部的服务接口,不提供对外暴露
 * Created by Lait on 2017/7/27.
 */
public interface OrderImpService {

    public boolean importOutFile(File file)throws Exception;
    
    public boolean importInnerFile(File file) throws Exception;

    public boolean innerFileReptile(String startDate)throws Exception;
    
    public File downOutFile(String startDate,String endDate)throws Exception;
    
    public File downInnerFile(String startDate,String endDate)throws Exception;
    
    public String queryCrccReport(String userName,String idCard) throws Exception;
    
    public void timeTaskCredit() throws Exception;
    
    public void cleanTaskCredit() throws Exception;
}