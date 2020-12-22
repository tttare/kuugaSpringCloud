package com.kuuga.resource.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Mybatis Plus配置
 *
 * @author tttare
 * @date 2020/10/15
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * Mybatis-Plus 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
