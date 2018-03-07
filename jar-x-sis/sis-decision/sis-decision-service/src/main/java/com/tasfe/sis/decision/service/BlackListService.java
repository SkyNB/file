package com.tasfe.sis.decision.service;

import java.util.List;

import com.tasfe.sis.decision.entity.BlackList;
import com.tasfe.sis.decision.model.BlackListToExportVO;
import com.tasfe.sis.decision.model.BlackListVO;

/**
 * Created by dongruixi on 2017/12/6.
 */
public interface BlackListService {
    /**
     * 根据身份证判断是否存在黑名单
     * @param idCardNum
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
     * 根据身份证判断是否存在已授信
     * @param idCardNum
     * @return
     * @throws Exception
     */
    Boolean existsOrderCredit(String idCardNum) throws Exception;

    /**
     * 获取黑名单(用于导出)
     * @param aftTimeStamp
     * 根据时间戳获取黑名单，在该时间错后有更新的黑名单
     * @return
     * 黑名单列表
     */
    List<BlackListToExportVO> getBackListToExport(Long aftTimeStamp) throws Exception;

    /**
     * 获取黑名单
     * @param aftTimeStamp
     * 根据时间戳获取黑名单，在该时间错后有更新的黑名单
     * @return
     * 黑名单列表
     */
    List<BlackListVO> getBackList(Long aftTimeStamp) throws Exception;

    /**
     * 推送黑名单
     * @return
     * @throws Exception
     */
    Boolean pushBlackList() throws Exception;
}
