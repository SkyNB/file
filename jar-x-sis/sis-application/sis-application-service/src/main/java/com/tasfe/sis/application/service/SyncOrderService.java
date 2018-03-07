package com.tasfe.sis.application.service;

import java.util.Date;
import java.util.List;

/**
 * Created by dongruixi on 2017/12/8.
 */
public interface SyncOrderService {


    Boolean downloadFiles(List<String> remoteFiles) throws Exception;

    Boolean downloadIdentityPhoto(Long orderId, String idCardNo) throws Exception;

    Integer pullApplication() throws Exception;

    Boolean pushApprove(String orderId, String status) throws Exception;

    Boolean pushLoans(Date date) throws Exception;

    Boolean pushRepayment(Date date) throws Exception;

    Boolean downloadOrderPhotos(Long orderId,String url0,String url1) throws Exception;

}
