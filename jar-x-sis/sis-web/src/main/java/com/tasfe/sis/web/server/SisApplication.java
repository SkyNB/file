package com.tasfe.sis.web.server;

import com.tasfe.framework.netty.TasfeApplication;
import com.tasfe.framework.netty.annotation.Env;
import com.tasfe.framework.netty.annotation.NettyBootstrap;

/**
 * Created by Lait on 2017/8/10.
 */
@NettyBootstrap(springApplicationContext="classpath:sis-application.xml",springServletContext="classpath:sis-web.xml")
@Env(profile = "classpath:/com/tasfe/sis/configs/base/web/properties/base-local-properties.xml")
public class SisApplication {
    public static void main(String[] args) {
        TasfeApplication.run(SisApplication.class,args);
    }
}