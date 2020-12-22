package com.kuuga.resource.redis.impl;

import com.google.gson.Gson;
import com.kuuga.resource.redis.IRedis;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

@Component("redisUtil")
public class IRedisImpl implements IRedis {

    private static final long defaultexpires = 24*60*60l ; // 默认过期时间

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    public String get(String key) {
        String result = redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] value = connection.get(serializer.serialize(key));
                return serializer.deserialize(value);
            }
        });
        return result;
    }

    public void set(String key, String value, long validTime) {
        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                connection.set(serializer.serialize(key), serializer.serialize(value));
                connection.expire(serializer.serialize(key), validTime);
                return true;
            }
        });
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public <T> T getObject(String key,Class<T> clazz){
        Gson gson = new Gson();
        return gson.fromJson(get(key),clazz);
    }


    @Override
    public <T> T popObject(String key, Class<T> clazz) {
        Gson gson = new Gson();
        T a = gson.fromJson(get(key),clazz);
        if(a!=null){
            delete(key);
        }
        return a;
    }

    @Override
    public <T> void setObject(String key,T value,Long times) {
        times = times == null ? defaultexpires : times;
        Gson gson = new Gson();
        set(key,gson.toJson(value),times);
    }



}
