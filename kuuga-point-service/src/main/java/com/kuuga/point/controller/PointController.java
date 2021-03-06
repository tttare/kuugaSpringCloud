package com.kuuga.point.controller;

import com.kuuga.api.biz.service.PointService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController()
@RequestMapping(value = "/point")
public class PointController implements PointService {

    @Value("${eureka.instance.metadata-map.version}")
    private String version;


    @PostMapping(value = "/getVersion")
    public String getVersion(){
        return version;
    }

}
