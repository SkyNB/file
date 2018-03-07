package com.tasfe.sis.application.web.controller;

import com.google.common.base.Strings;
import com.tasfe.sis.application.service.SyncOrderService;
import com.tasfe.sis.base.model.response.ResponseData;
import com.tasfe.sis.base.utils.PropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by dongruixi on 2017/12/8.
 */
@RestController
@RequestMapping("/sync")
public class SyncOrderController {
    Logger logger = LoggerFactory.getLogger(SyncOrderController.class);

    String remoteUrl= PropertyUtil.getProperty("http.syncApplication.api");

    @Autowired
    SyncOrderService syncOrderServiceImpl;

    @RequestMapping(value = "pullOrderPhotos",method = RequestMethod.GET)
    public ResponseData<String> pullOrderPhotos(@RequestParam Long id,@RequestParam String url0,@RequestParam String url1){

        try {
            syncOrderServiceImpl.downloadOrderPhotos(id,url0,url1);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseData<String>(){{
                setCode("1").setData("").setError(e.getMessage());
            }};
        }

        return new ResponseData<String>(){{
            setCode("0").setData("");
        }};
    }

    @RequestMapping(value = "pullApplication",method = RequestMethod.GET)
    public ResponseData<String> pullApplicaion(){

        try {
            int count=syncOrderServiceImpl.pullApplication();
            return new ResponseData<String>(){{
                setCode("0").setData(String.valueOf(count));
            }};
        } catch (Exception e) {
            return new ResponseData<String>(){{
                setCode("1").setData("").setError(e.getMessage());
            }};
        }


    }

    @RequestMapping(value = "pushApprove",method = RequestMethod.GET)
    public ResponseData<String> pushApprove(@RequestParam String orderId,@RequestParam String status) throws Exception {

        syncOrderServiceImpl.pushApprove(orderId,status);

        return new ResponseData<String>(){{
            setCode("0").setData("");
        }};
    }

    @RequestMapping(value = "pushLoans",method = RequestMethod.GET)
    public ResponseData<String> pushLoans(@RequestParam(value = "date",required = false) String sdate) throws Exception {
        Calendar calendar=Calendar.getInstance();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(!Strings.isNullOrEmpty(sdate)){
            date = sdf.parse(sdate);
        }
        else{
            calendar.add(Calendar.DATE,-1);
            date = calendar.getTime();
        }

        syncOrderServiceImpl.pushLoans(date);

        return new ResponseData<String>(){{
            setCode("0").setData("");
        }};
    }

    @RequestMapping(value = "pushRepayment",method = RequestMethod.GET)
    public ResponseData<String> pushRepayment(@RequestParam(value="date",required = false) String sdate) throws Exception {
        Calendar calendar=Calendar.getInstance();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(!Strings.isNullOrEmpty(sdate)){
            date = sdf.parse(sdate);
        }
        else{
            calendar.add(Calendar.DATE,-1);
            date = calendar.getTime();
        }

        syncOrderServiceImpl.pushRepayment(date);

        return new ResponseData<String>(){{
            setCode("0").setData("");
        }};
    }

    @RequestMapping(value = "downloadIdentityPhoto",method = RequestMethod.GET)
    public ResponseData<String> downloadIdentityPhoto(@RequestParam Long orderId, @RequestParam String identity) throws Exception {
        Boolean res = syncOrderServiceImpl.downloadIdentityPhoto(orderId, identity);
        if(res) {
            return new ResponseData<String>() {{
                setCode("0").setData("OK");
            }};
        }
        else{
            return new ResponseData<String>() {{
                setCode("1").setData("failed");
            }};
        }
    }
}
