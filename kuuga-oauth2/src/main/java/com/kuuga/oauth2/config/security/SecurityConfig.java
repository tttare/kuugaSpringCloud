package com.kuuga.oauth2.config.security;

import com.kuuga.oauth2.service.KuugaUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @Author: qiuyongkang
 * @Description: spring security配置,配合认证服务器
 * @Date: 2020/12/2 22:02
 * @Version: 1.0
 */
@Configuration
@EnableWebSecurity
@Order(2)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private KuugaUserDetailService userDetailService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        //密码需要加密
        //BCryptPasswordEncoder中,encode(str)方法,会生成随机盐,故一个密码每次加密都会不同,生成的加密字符串有60位,字符串中包括一定长度的密码盐
        //match(rawPsw,encodePsw),校验密码是否正确,从数据库中加密密码中取出密码盐,用此密码盐与参数中未加密的生密码混淆加密,比较两个加密后的密码是否相同
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // HttpSecurity 它允许对特定的http请求基于安全考虑进行配置。默认情况下，适用于所有的请求，但可以使用requestMatcher(RequestMatcher)或者其它相似的方法进行限制。
        http.requestMatchers().antMatchers("/oauth/**")
                .and()
                .authorizeRequests()
                .antMatchers("/oauth/**").authenticated()
                .and()
                .csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
    }

    /**
     * 不定义没有password grant_type,密码模式需要AuthenticationManager支持
     *
     * @return
     * @throws Exception
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
