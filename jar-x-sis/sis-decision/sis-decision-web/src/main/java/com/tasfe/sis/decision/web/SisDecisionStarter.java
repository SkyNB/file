package com.tasfe.sis.decision.web;

import com.tasfe.framework.netty.TasfeApplication;
import com.tasfe.framework.netty.annotation.Env;
import com.tasfe.framework.netty.annotation.NettyBootstrap;

/**
 * Created by dongruixi on 2017/11/9.
 */
@NettyBootstrap(springApplicationContext="classpath:sis-decision-application.xml",springServletContext="classpath:sis-decision-web.xml")
@Env(profile = "classpath:/com/tasfe/sis/configs/base/web/properties/base-local-properties.xml")
public class SisDecisionStarter {
    public static void main(String[] args) {
            TasfeApplication.run(SisDecisionStarter.class,args);

    }
}
 