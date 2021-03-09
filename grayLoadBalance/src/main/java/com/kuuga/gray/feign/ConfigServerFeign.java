package com.kuuga.gray.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

//@Service
//@FeignClient(value = "kuuga-server-config")
public interface ConfigServerFeign {

    @GetMapping(value = "/kuuga-zuul/dev")
    Map<String,Object> getGrayConfig();
}
