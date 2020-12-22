package com.kuuga.oauth2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * ClassName: OAuth2Application <br/>
 * Description: <br/>
 * date: 2020/12/02 19:42<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
public class OAuth2Application {

    public static void main(String args){
        SpringApplication.run(OAuth2Application.class,args);
    }

}
