package com.kuuga.api.biz.service;

import com.kuuga.api.biz.callback.ArticleServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

@Service
@FeignClient(value = "kuuga-pay-service")
public interface PayService {

    @PostMapping(value = "/pay/getVersion")
    String getVersion();
}
