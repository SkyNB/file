package com.tasfe.sis.decision.web.controller;

import com.google.common.base.Strings;
import com.tasfe.sis.base.model.response.ResponseData;
import com.tasfe.sis.decision.model.DecisionVO;
import com.tasfe.sis.decision.service.DecisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by dongruixi on 2017/12/6.
 */

@RestController
@RequestMapping("decision")
public class DecisionController {
    @Autowired
    DecisionService decisionServiceImpl;

    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public ResponseData<String> getCurrentDecision(@RequestParam String name,@RequestParam String version) throws Exception {
        if(Strings.isNullOrEmpty(version)){
            version = decisionServiceImpl.getCurrentDecision(name);
        }
        String script =  decisionServiceImpl.getDecision(name,version);
        return new ResponseData<String>(){{
            setCode("0").setData(script);
        }};
    }

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public ResponseData<List<DecisionVO>> listDecision(@RequestParam String name) throws Exception {
        List<DecisionVO> rst = decisionServiceImpl.listDecision(name);
        return new ResponseData<List<DecisionVO>>(){{
            setCode("0").setData(rst);
        }};
    }

    @RequestMapping(value = "/put",method = RequestMethod.POST)
    public ResponseData<String> putDecision(@RequestParam String name, @RequestParam String version, @RequestBody String  body) throws Exception {
        decisionServiceImpl.updateDecision(name,version,body);
        return new ResponseData<String>(){{
            this.setCode("0").setData("");
        }};
    }

    @RequestMapping(value = "/setCurrent",method = RequestMethod.GET)
    public ResponseData<String> putDecision(@RequestParam String name, @RequestParam String version) throws Exception {
        decisionServiceImpl.currentDecision(name,version);
        return new ResponseData<String>(){{
            this.setCode("0").setData("");
        }};
    }
}
