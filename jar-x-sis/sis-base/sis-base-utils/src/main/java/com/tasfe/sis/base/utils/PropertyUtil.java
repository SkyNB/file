package com.tasfe.sis.base.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by dongruixi on 2017/12/8.
 */
public class PropertyUtil {

    static Map<String,String> properties=new HashMap<>();

    public static String getProperty(String name){
        String value = "";
        if(!properties.containsKey(name)){
            InputStream inputStream = PropertyUtil.class.getClassLoader().getResourceAsStream("server.properties");
            Properties p = new Properties();
            try{
                p.load(inputStream);
            }
            catch (IOException ie){
                ie.printStackTrace();
            }
            finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            value = p.getProperty(name);
        }
        return value;
    }
}
