package com.kuuga.config.configruation;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class AutoRefreshConfig {

    @RefreshScope
    @Bean
    public Map autoRefresh(){
        return new HashMap();
    }
}
