package com.tasfe.sis.base.service.cache;

import java.util.Map;

/**
 * Created by Lait on 2017/7/28.
 */
public interface CacheTemplate {

    String set(String key, String value);

    Long expire(String key, Integer seconds);

    String get(String key);

    Boolean exists(String key);

    Long incr(String key);

    Long hset(String key, String field, String value);

    Map<String, String> hgetAll(String key);

    Long hdel(String key, String field);

    public Long setNX(String key,String value);

    public Long del(String key);

    public String getSet(String key,String value);

    Long EnQueue(String key,String value);

    String DeQueue(String key);

    Long LLen(String key);

}
