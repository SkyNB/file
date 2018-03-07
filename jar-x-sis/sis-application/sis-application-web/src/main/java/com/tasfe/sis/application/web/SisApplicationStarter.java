package com.tasfe.sis.application.web;

import com.tasfe.framework.netty.TasfeApplication;
import com.tasfe.framework.netty.annotation.Env;
import com.tasfe.framework.netty.annotation.NettyBootstrap;

/**
 * Created by Lait on 2017/8/10.
 */
@NettyBootstrap(springApplicationContext="classpath:sis-application-application.xml",springServletContext="classpath:sis-application-web.xml")
@Env(profile = "classpath:/com/tasfe/sis/configs/base/web/properties/base-local-properties.xml")
public class SisApplicationStarter {
    public static void main(String[] args) {
        TasfeApplication.run(SisApplicationStarter.class,args);
    }
}
