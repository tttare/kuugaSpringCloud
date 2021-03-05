package com.kuuga.pay.controller;

import com.kuuga.api.biz.service.PayService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController()
@RequestMapping(value = "/pay")
public class PayTxController implements PayService {

    @Value("${eureka.instance.metadata-map.version}")
    private String version;

    @PostMapping(value = "/getVersion")
    public String getVersion(){
        return version;
    }
}
