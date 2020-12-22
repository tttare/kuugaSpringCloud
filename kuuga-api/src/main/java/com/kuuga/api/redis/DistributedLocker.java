package com.kuuga.api.redis;

import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName: DistributedLocker
 * @Author: qiuyongkang
 * @Description: 基于redisson的分布式锁
 * @Date: 2020/12/12 10:58
 * @Version: 1.0
 */
public interface DistributedLocker {

    RLock lock(String lockKey);

    RLock lock(String lockKey, int timeout);

    RLock lock(String lockKey, TimeUnit unit, int timeout);

    boolean tryLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime);

    void unlock(String lockKey);

    void unlock(RLock lock);
}
