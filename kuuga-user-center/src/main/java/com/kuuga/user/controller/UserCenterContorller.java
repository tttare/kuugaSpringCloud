package com.kuuga.user.controller;

import com.kuuga.api.biz.service.PayService;
import com.kuuga.user.dao.UserMapper;
import com.kuuga.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@RestController()
@RequestMapping(value = "/user-center")
public class UserCenterContorller {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserMapper userMapper;

    @Autowired
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

    @PostMapping(value = "/userPay")
    public Map userPay(@RequestBody Map<String,Object> param ){
        User user = new User();
        user.setName((String)param.get("userName"));
        user.setVersion(version);
        userMapper.insert(user);
        String sysVersion = systemService.savePay(param);
        param.put("user",version);
        param.put("system",sysVersion);
        return param;
    }

}
