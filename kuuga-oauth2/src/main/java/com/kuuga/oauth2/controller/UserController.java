package com.kuuga.oauth2.controller;

import com.kuuga.api.common.ResponseParam;
import com.kuuga.api.common.SysContant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @ClassName: UserController
 * @Author: qiuyongkang
 * @Description: 通用用户接口
 * @Date: 2020/12/5 21:21
 * @Version: 1.0
 */
@RestController
public class UserController {

    @Autowired
    private ConsumerTokenServices consumerTokenServices;

    @GetMapping("/member")
    public Principal user(Principal member) {
        return member;
    }

    @DeleteMapping(value = "/exit")
    public ResponseParam revokeToken(String access_token) {
        ResponseParam result = new ResponseParam();
        if (consumerTokenServices.revokeToken(access_token)) {
            result.setCode(SysContant.SUCCESS);
            result.setDesc("注销成功");
        } else {
            result.setCode(SysContant.FAIL);
            result.setDesc("注销失败");
        }
        return result;
    }


}
