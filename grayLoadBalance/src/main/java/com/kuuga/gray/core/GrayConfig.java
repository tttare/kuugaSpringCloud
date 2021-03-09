package com.kuuga.gray.core;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@PropertySource(value = "classpath:application.yml", factory = YamlPropertyLoaderFactory.class)
@ConfigurationProperties(prefix = "gray")
public class GrayConfig {
    private Map<String,String> stableConfig;
    private List<ProjectGrayConfig> grayProjectConfig;

    public Map<String, String> getStableConfig() {
        return stableConfig;
    }

    public List<ProjectGrayConfig> getGrayProjectConfig() {
        return grayProjectConfig;
    }

    public void setGrayProjectConfig(List<ProjectGrayConfig> grayProjectConfig) {
        this.grayProjectConfig = grayProjectConfig;
    }

    public void setStableConfig(Map<String,String> stableConfigMap){
        if(stableConfigMap==null || stableConfigMap.isEmpty()){
            return;
        }
        Set<String> keys = stableConfigMap.keySet();
        Map<String,String> lowKeyMap = new HashMap<>();
        for(String key:keys){
            lowKeyMap.put(key.toLowerCase(),stableConfigMap.get(key));
        }
        this.stableConfig = lowKeyMap;
    }
}
