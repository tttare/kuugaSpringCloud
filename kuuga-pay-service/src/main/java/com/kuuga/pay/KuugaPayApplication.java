package com.kuuga.pay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
@EnableEurekaClient//服务注册
@EnableDiscoveryClient//允许服务被发现
public class KuugaPayApplication {

    public static void main(String[] args) {
        SpringApplication.run(KuugaPayApplication.class);
    }
}
