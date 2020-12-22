package com.kuuga.portal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * ClassName: KuugaPortalApplication <br/>
 * Description: <br/>
 * date: 2020/10/16 14:20<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
@EnableEurekaClient //开启Eureka 客户端
@EnableFeignClients(basePackages = {"com.kuuga.api"})
public class KuugaPortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(KuugaPortalApplication.class,args);
    }
}
