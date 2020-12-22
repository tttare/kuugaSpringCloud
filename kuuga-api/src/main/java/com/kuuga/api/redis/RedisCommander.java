package com.kuuga.api.redis;

import com.kuuga.api.biz.callback.ArticleServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @ClassName: RedisCommander
 * @Author: qiuyongkang
 * @Description: ${description}
 * @Date: 2020/12/11 22:16
 * @Version: 1.0
 */
@Service
@FeignClient(value = "kuuga-redis")
public interface RedisCommander {

    @PostMapping(value = "/set")
    boolean set(String key,Object value,long times);

    @PostMapping(value = "/get")
    Object get(String key);

    @PostMapping(value = "/delete")
    void delete(String... key);

}
