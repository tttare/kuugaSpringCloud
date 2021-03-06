package com.kuuga.pay;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication()
@EnableEurekaClient//服务注册
@EnableDiscoveryClient//允许服务被发现
@MapperScan("com.kuuga.pay.dao")
@EnableFeignClients(basePackages = {"com.kuuga.api"})
public class KuugaPayApplication {

    public static void main(String[] args) {
        SpringApplication.run(KuugaPayApplication.class);
    }
}
