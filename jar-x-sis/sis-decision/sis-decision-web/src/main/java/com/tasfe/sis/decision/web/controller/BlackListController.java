package com.tasfe.sis.decision.web.controller;

import com.tasfe.sis.base.model.response.ResponseData;
import com.tasfe.sis.decision.service.BlackListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by dongruixi on 2017/12/12.
 */
@RestController
@RequestMapping("blacklist")
public class BlackListController {

    @Autowired
    BlackListService blackListServiceImpl;

    @RequestMapping(value = "/push",method = RequestMethod.GET)
    public ResponseData<String> push() throws Exception {
        blackListServiceImpl.pushBlackList();
        return new ResponseData<String>(){{
            setCode("0").setData("");
        }};
    }

}
