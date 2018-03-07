package com.tasfe.sis.user.ws.server;

import com.tasfe.framework.netty.TasfeApplication;
import com.tasfe.framework.netty.annotation.Env;
import com.tasfe.framework.netty.annotation.NettyBootstrap;

/**
 * Created by hefusang on 2017/8/10.
 */
@NettyBootstrap(springApplicationContext="classpath:sis-user-application.xml",springServletContext="classpath:sis-user-web.xml")
@Env(profile = "classpath:/com/tasfe/sis/configs/base/web/properties/base-local-properties.xml")
public class SisUserApplication {
    public static void main(String[] args) {
        TasfeApplication.run(SisUserApplication.class,args);
    }
}