package com.kuuga.user.config;


import com.kuuga.gray.rule.GrayBalanceRule;
import com.netflix.loadbalancer.IRule;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.client.RestTemplate;

/**
 * 〈Security配置〉
 * @author Yang HaiJi, 2019-05-17
 * @version Araf v1.0
 */
@Configuration
@Order(99)//必加
public class SecurityConfig {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }

    @Bean
    public IRule ribbonRule() {
        //使用 灰度发布rule
        return new GrayBalanceRule();
    }

}
