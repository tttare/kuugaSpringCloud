package com.kuuga.zuul.config;

import com.kuuga.gray.core.ProjectGrayConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@PropertySource(value = "classpath:application.yml", factory = YamlPropertyLoaderFactory.class)
@ConfigurationProperties(prefix = "gray")
public class GrayConfig {
    private Map<String,String> stableConfig;
    private List<ProjectGrayConfig> grayProjectConfig;

    public Map<String, String> getStableConfig() {
        return stableConfig;
    }

    public void setStableConfig(Map<String, String> stableConfig) {
        this.stableConfig = stableConfig;
    }

    public List<ProjectGrayConfig> getGrayProjectConfig() {
        return grayProjectConfig;
    }

    public void setGrayProjectConfig(List<ProjectGrayConfig> grayProjectConfig) {
        this.grayProjectConfig = grayProjectConfig;
    }
}
