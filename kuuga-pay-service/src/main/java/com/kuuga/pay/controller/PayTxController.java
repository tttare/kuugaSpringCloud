package com.kuuga.pay.controller;

import com.kuuga.api.biz.service.PayService;
import com.kuuga.api.biz.service.PointService;
import com.kuuga.pay.dao.PayMapper;
import com.kuuga.pay.model.Pay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController()
@RequestMapping(value = "/pay")
@RefreshScope
public class PayTxController implements PayService {

    @Value("${eureka.instance.metadata-map.version}")
    private String version;

    @Value("${gray.stableConfig}")
    private String stableConfig;

    @Value("${gray.grayProjectConfig}")
    private String grayProjectConfig;

    @Autowired
    private PayMapper payMapper;

    @PostMapping(value = "/getVersion")
    public String getVersion(){
        return version;
    }

    @PostMapping(value = "/savePay")
    public String savePay(@RequestBody Map<String,Object> param){
        Pay pay = new Pay();
        pay.setName((String)param.get("payName"));
        pay.setVersion(version);
        payMapper.insert(pay);
        return version;
    }

    @PostMapping("/grayConfig")
    public String getGrayConfig(){

        return stableConfig +"---"+ grayProjectConfig;
    }
}
