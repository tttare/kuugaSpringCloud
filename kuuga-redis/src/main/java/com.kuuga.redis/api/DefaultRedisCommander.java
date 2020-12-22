package com.kuuga.redis.api;

import com.kuuga.api.redis.RedisCommander;
import com.kuuga.redis.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: RedisCommander
 * @Author: qiuyongkang
 * @Description: redis 操作类
 * @Date: 2020/12/11 22:14
 * @Version: 1.0
 */
@RestController
@RequestMapping(value = "/redis/command/")
public class DefaultRedisCommander implements RedisCommander {

    @Autowired
    private RedisUtil redisUtil;

    @PostMapping(value = "/set")
    public boolean set(String key,Object value,long times){
        return redisUtil.set(key,value,times);
    };

    @PostMapping(value = "/get")
    public Object get(String key){
        return redisUtil.get(key);
    }

    @PostMapping(value = "/delete")
    public void delete(String... key) {
        redisUtil.del(key);
    }
}
