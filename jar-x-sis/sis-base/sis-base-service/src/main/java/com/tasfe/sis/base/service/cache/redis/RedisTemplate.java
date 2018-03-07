package com.tasfe.sis.base.service.cache.redis;

import com.mobanker.framework.redis.template.RedisClientTemplate;
import com.tasfe.sis.base.service.cache.CacheTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by Lait on 2017/7/28.
 */
@Service("redisCache")
public class RedisTemplate implements CacheTemplate {

    @Autowired
    private RedisClientTemplate redisClientTemplate;

    @Override
    public String set(String key, String value) {
        return redisClientTemplate.set(key, value);
    }

    @Override
    public Long expire(String key, Integer seconds) {
        return redisClientTemplate.expire(key,seconds);
    }

    @Override
    public String get(String key) {
        return redisClientTemplate.get(key);
    }

    @Override
    public Boolean exists(String key) {
        return redisClientTemplate.exists(key);
    }

    @Override
    public Long incr(String key) {
        return redisClientTemplate.incr(key);
    }

    @Override
    public Long hset(String key, String field, String value) {
        return redisClientTemplate.hset(key,field,value);
    }

    @Override
    public Map<String, String> hgetAll(String key) {
        return redisClientTemplate.hgetAll(key);
    }

    @Override
    public Long hdel(String key, String field) {
        return redisClientTemplate.hdel(key,field);
    }

    @Override
    public Long setNX(String key,String value){return redisClientTemplate.setnx(key,value);}

    @Override
    public Long del(String key){return redisClientTemplate.del(key);}

    @Override
    public String getSet(String key,String value){return redisClientTemplate.getSet(key,value);}

    @Override
    public Long EnQueue(String key,String value){
        return redisClientTemplate.rpush(key,value);
    }

    @Override
    public String DeQueue(String key){
        if(redisClientTemplate.llen(key)<=0){
            return null;
        }
        return redisClientTemplate.lpop(key);
    }

    @Override
    public Long LLen(String key) {
        return redisClientTemplate.llen(key);
    }

}
