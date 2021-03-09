package com.kuuga.api.biz.service;

import com.kuuga.gray.feign.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

@Service
@FeignClient(value = "kuuga-point-service",configuration = FeignConfiguration.class)
public interface PointService {

    @PostMapping(value = "/point/getVersion")
    String getVersion();
}
