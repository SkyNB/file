package com.tasfe.sis.base.configs;

//import com.mobanker.framework.server.netty.config.ServerConfig;
//import com.mobanker.framework.server.netty.config.ServerConfigReader;
import org.apache.zookeeper.server.ServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by liangwei on 2017/7/3.
 */
public class HljnsServerConfig {
    /*private static String hljnsServerProperties = "/configs/jdbc.properties";
    private static int SessionTimeOutSeconds = 3600;//登录会话超时时常(秒)
    static final Logger logger = LoggerFactory.getLogger(ServerConfig.class);
    public static int getSessionTimeOutSeconds() {
        return SessionTimeOutSeconds;
    }

    public static void setSessionTimeOutSeconds(int sessionTimeOutSeconds) {
        SessionTimeOutSeconds = sessionTimeOutSeconds;
    }

    public static void init() {
        ServerConfigReader configReader = ServerConfigReader.getInstance().init(hljnsServerProperties);
        Integer tn = configReader.getInt("login.session.timeOut.seconds");
        if(tn.intValue() > 0) {
            SessionTimeOutSeconds = tn.intValue();
            logger.info("login.session.timeOut.seconds："+SessionTimeOutSeconds);
        }
    }*/
}
