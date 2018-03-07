package com.tasfe.sis.base.service.impls;

import com.tasfe.sis.base.service.CacheService;
import com.tasfe.sis.base.service.cache.CacheTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by Lait on 2017/7/28.
 */
@Service
public class CacheServiceImpl implements CacheService {

    @Resource(name = "redisCache")
    private CacheTemplate cacheTemplate;


    @Override
    public String set(String key, String value) {
        return cacheTemplate.set(key, value);
    }

    @Override
    public Long expire(String key, Integer seconds) {
        return cacheTemplate.expire(key, seconds);
    }


    @Override
    public String get(String key) {
        return cacheTemplate.get(key);
    }

    @Override
    public Boolean exists(String key) {
        return cacheTemplate.exists(key);
    }

    @Override
    public Long incr(String key) {
        return cacheTemplate.incr(key);
    }

    @Override
    public Long hset(String key, String field, String value) {
        return cacheTemplate.hset(key, field, value);
    }

    @Override
    public Map<String, String> hgetAll(String key) {
        return cacheTemplate.hgetAll(key);
    }

    @Override
    public Long hdel(String key, String field) {
        return cacheTemplate.hdel(key, field);
    }

    @Override
    public Long setNX(String key, String value) {
        return cacheTemplate.setNX(key,value);
    }

    @Override
    public Long del(String key) {
        return cacheTemplate.del(key);
    }

    @Override
    public String getSet(String key, String value) {
        return cacheTemplate.getSet(key,value);
    }

    @Override
    public Long EnQueue(String key, String value) {
        return cacheTemplate.EnQueue(key,value);
    }

    @Override
    public String DeQueue(String key) {
        return cacheTemplate.DeQueue(key);
    }

    @Override
    public Long LLen(String key) {
        return cacheTemplate.LLen(key);
    }

}
