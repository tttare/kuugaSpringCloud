package com.kuuga.oauth2.controller;

import com.baomidou.mybatisplus.core.toolkit.EncryptUtils;
import com.kuuga.api.common.ResponseParam;
import com.kuuga.api.common.SysContant;
import com.kuuga.api.redis.RedisCommander;
import com.kuuga.api.system.model.User;
import com.kuuga.api.vo.UploadObject;
import com.kuuga.api.vo.UserLogonRequest;
import com.kuuga.oauth2.mapper.UserDao;
import com.kuuga.oauth2.utils.AesUtils;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import com.wf.captcha.utils.CaptchaUtil;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.File;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.kuuga.oauth2.utils.RandomUtils;

/**
 * @ClassName: RegisterController
 * @Author: qiuyongkang
 * @Description: 注册和登录
 * @Date: 2020/12/19 20:54
 * @Version: 1.0
 */
@RestController
@RequestMapping(value = "/register")
@Log
public class RegisterController {

    @Autowired
    private RedisCommander redisCommander;
    @Autowired
    private UserDao  userDao;

    /**
     * 登录时,加密密码所用key
     * */
    @RequestMapping(value = "/getKey",method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getKey(){
        String key = create16String();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("key",key);
        return map;
    }

    /**
     * 用户登录
     * */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseParam login(HttpServletRequest request, @RequestBody Map<String,String> param) throws Exception{
        String userName = param.get("username");
        //加密的密码
        String encryptedPassword = param.get("password");
        //密码加密key
        String key = param.get("key");
        //验证码
        String verifyCode = param.get("verifyCode").toLowerCase();
        if(redisCommander.get(verifyCode)==null){
            return new ResponseParam(SysContant.FAIL,"验证码不正确或已失效,请点击验证码重新输入");
        }
        String password = AesUtils.decrypt(encryptedPassword,key);

        return null;
    }

    /**
     * 用户注册
     * */
    @RequestMapping(value = "/logon", method = RequestMethod.POST)
    @ResponseBody
    public ResponseParam logon(HttpServletRequest request,@RequestBody UserLogonRequest param) throws Exception{
        ResponseParam rp =null;
        String userName = param.getUserName();
        String name = param.getNickName();
        String password = param.getPassword();
        String birthDate = param.getBirthDate();
        String email = param.getEmail();
        String emailCode = param.getEmailCode();
        String emailCodeCache = (String)redisCommander.get(email);
        if(StringUtils.isEmpty(emailCodeCache)){
            rp = new ResponseParam(SysContant.FAIL,"验证码已过期");
        }else{
            if(emailCodeCache.equals(emailCode)){
                //作废缓存
                redisCommander.delete(email);
                //插入用户
                User user = new User();
                user.setUserName(userName);
                user.setEmail(email);
                user.setState("0");
                Date date = new Date();
                user.setCreateDate(date);
                // 所有用户默认只有一个月有效期
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.MONTH,1);
                user.setBirthDate(birthDate);
                user.setExpiredDate(new Date(calendar.getTime().getTime()));
                /**
                 * SpringSecurity Encoder
                 * */
                PasswordEncoder encoder = new BCryptPasswordEncoder();
                String encryptPwd = encoder.encode(password);
                user.setPassword(encryptPwd);
                user.setNickName(name);
                //处理用户上传的图像---图像处理失败不能影响用户注册
                try {
                    //用户头像保存
                    //TODO
                }catch (Exception e){
                    log.info("用户图像新增失败:"+e.getMessage());
                }
                try{
                    userDao.insert(user);
                    rp = new ResponseParam(SysContant.SUCCESS,"注册成功");
                }catch (Exception e){
                    rp = new ResponseParam(SysContant.FAIL,"数据异常:"+e.getMessage());
                }
            }else{
                rp = new ResponseParam(SysContant.FAIL,"验证码已过期");
            }
        }
        return null;
    }

    /**
     * 登录验证码
     * */
    @RequestMapping("/getVerifyCode")
    public void defaultKaptcha(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // 设置请求头为输出图片类型
        response.setContentType("image/gif");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        // 三个参数分别为宽、高、位数
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 5);
        // 设置字体
        specCaptcha.setFont(new Font("Verdana", Font.PLAIN, 32));  // 有默认字体，可以不用设置
        // 设置类型，纯数字、纯字母、字母数字混合
        specCaptcha.setCharType(Captcha.TYPE_NUM_AND_UPPER);
        String verifyCode = specCaptcha.text().toLowerCase();
        //验证码保存入redis 2分钟
        redisCommander.set(verifyCode,new Date().getTime(),120);
        // 输出图片流
        specCaptcha.out(response.getOutputStream());
    }

    private String create16String()
    {
        return RandomUtils.generateString(16);
    }

    public static void main(String[] args) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String encoderPassword = encoder.encode("root");//加密后的密码60位,decode时,会生成随机盐,字符串中包括了盐
        boolean root = encoder.matches("root",encoderPassword);//解密就是取出加密密码串中的盐,在与未加密的密码一起混淆加密,两个加密后的密码比较是否相同
        System.out.println(root);
    }
}
