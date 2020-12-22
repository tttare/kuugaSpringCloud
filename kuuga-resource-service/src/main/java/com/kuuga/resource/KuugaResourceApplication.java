package com.kuuga.resource;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * ClassName: KuugaResourceApplication <br/>
 * Description: <br/>
 * date: 2020/10/11 19:42<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
@SpringBootApplication
@EnableEurekaClient//服务注册
@EnableDiscoveryClient//允许服务被发现
@MapperScan("com.kuuga.resource.mapper")//扫描Mapper文件
@EnableTransactionManagement //开启Spring事务支持
public class KuugaResourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(KuugaResourceApplication.class,args);
    }
}
