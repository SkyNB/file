package com.tasfe.sis.collection.web;

import com.tasfe.framework.netty.TasfeApplication;
import com.tasfe.framework.netty.annotation.Env;
import com.tasfe.framework.netty.annotation.NettyBootstrap;

/**
 * Created by Lait on 2017/8/1.
 */
@NettyBootstrap
@Env(profile = "classpath:/com/tasfe/sis/configs/collection/web/properties/collection-local-properties.xml")
public class BaseApplication {

    public static void main(String[] args) {
        TasfeApplication.run(BaseApplication.class,args);
    }

}
