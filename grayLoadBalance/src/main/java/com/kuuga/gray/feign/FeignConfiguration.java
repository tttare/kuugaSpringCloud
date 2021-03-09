package com.kuuga.gray.feign;

import com.kuuga.gray.core.GrayContants;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Configuration
public class FeignConfiguration implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //传递灰度信息，request header - > restTemplate header
        if(request.getHeader(GrayContants.STABLE_CONFIG.toLowerCase())!=null){
            template.header(GrayContants.STABLE_CONFIG.toLowerCase(), (String)request.getHeader(GrayContants.STABLE_CONFIG.toLowerCase()));
        }
        if(request.getHeader(GrayContants.GRAY_PROJECT_CONFIG.toLowerCase())!=null){
            template.header(GrayContants.GRAY_PROJECT_CONFIG.toLowerCase(), (String)request.getHeader(GrayContants.GRAY_PROJECT_CONFIG.toLowerCase()));
        }


    }
}