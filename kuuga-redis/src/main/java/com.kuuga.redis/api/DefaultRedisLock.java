package com.kuuga.redis.api;

import com.kuuga.api.redis.RedisLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.Duration;

/**
 * @ClassName: DefaultRedisLock
 * @Author: qiuyongkang
 * @Description: redis实现分布式锁
 * @Date: 2020/12/11 22:52
 * @Version: 1.0
 */
@RestController
@RequestMapping(value = "/redis/lock/")
public class DefaultRedisLock implements RedisLock {

    private String lock_key = "redis_lock"; //锁键

    protected long internalLockLeaseTime = 30000;//锁过期时间

    private long timeout = 999999; //获取锁的超时时间

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 基于redisTemplate锁
     * */

    /**
     * 加锁
     * 实现逻辑:通过redis的setnx方式（不存在则设置）,往redis上设置一个带有过期时间的key，如果设置成功，则获得了分布式锁。这里设置过期时间，是防止在释放锁的时候出现异常导致锁释放不掉。
     * 执行完业务操作之后，删除该锁。
     * */
    public boolean lock(String lockId){
        Boolean success = redisTemplate.opsForValue().setIfAbsent(lockId, "lock", Duration.ofMillis(internalLockLeaseTime));
        return success != null && success;
    }

    /**
     * 解锁
     * 删除lockId对应的key
     * */
    public void unLock(String lockId) {
        redisTemplate.delete(lockId);
    }

    /**
     * 基于redisson的锁,reisson基于redis框架包,提供了很多分布式项目应用的功能
     * */


}
