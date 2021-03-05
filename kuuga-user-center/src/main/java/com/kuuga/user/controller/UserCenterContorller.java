package com.kuuga.user.controller;

import com.kuuga.api.biz.service.PayService;
import com.kuuga.api.biz.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@RestController()
@RequestMapping(value = "/user-center")
public class UserCenterContorller {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired(required=false)
    private PayService systemService = null;

    @Value("${eureka.instance.metadata-map.version}")
    private String version;

    @PostMapping(value = "/getVersion")
    public Map getVersion(){
        String sysVersion = systemService.getVersion();
        Map param = new HashMap();
        param.put("user",version);
        param.put("system",sysVersion);
        return param;
    }

}
