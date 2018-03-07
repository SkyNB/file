package com.tasfe.sis.order.service;

import java.util.Date;
import java.util.List;

import com.tasfe.sis.order.entity.BlackList;

/**
 * Created by dongruixi on 2017/12/6.
 */
public interface BlackListService {
    /**
     * 根据身份证判断是否存在黑名单
     * @param idCardNum1
     * 身份证号
     * @return
     * 存在黑名单，返回True，否则为False
     */
    Boolean exists(String idCardNum) throws Exception;
    /**
     * 存储黑名单bo
     * @param bo
     * @return
     * @throws Exception
     */
    boolean save(BlackList bo) throws Exception;
    
    /**
	 * 查询黑名单列表
	 */
    
    public List<BlackList> queryList(String idCardNum) throws Exception;
    /**
	 * 查询时间范围的列表
	 */
    public List<BlackList> queryListByCTime(Date startDate, Date endDate) throws Exception;
}
