package com.kuuga.api.biz.service;

import com.kuuga.api.biz.callback.ArticleServiceFallback;
import com.kuuga.gray.feign.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@Service
@FeignClient(value = "kuuga-pay-service",configuration = FeignConfiguration.class)
public interface PayService {

    @PostMapping(value = "/pay/getVersion")
    String getVersion();

    @PostMapping(value = "/pay/savePay")
    String savePay(Map<String,Object> param);

}
