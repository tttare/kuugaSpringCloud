package com.kuuga.oauth2.service;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @ProjectName: springcloud
 * @Package: com.kuuga.oauth2.service
 * @ClassName: KuugaPasswordEncoder
 * @Author: mi
 * @Description: ${description}
 * @Date: 2020/12/2 21:17
 * @Version: 1.0
 */
public class KuugaPasswordEncoder  implements PasswordEncoder {

    @Override
    public String encode(CharSequence charSequence) {
        return null;
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return false;
    }

    public static void main(String[] args) {
        String password = "root";
        PasswordEncoder encoder = new KuugaPasswordEncoder();


    }
}
