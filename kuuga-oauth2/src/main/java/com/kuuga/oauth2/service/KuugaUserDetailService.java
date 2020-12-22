package com.kuuga.oauth2.service;

import com.baomidou.mybatisplus.core.toolkit.EncryptUtils;
import com.kuuga.api.system.model.SysRole;
import com.kuuga.api.system.model.User;
import com.kuuga.oauth2.mapper.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author: qiuyongkang
 * @Description: 获取数据库用户和权限服务类
 * @Date: 2020/12/2 20:06
 * @Version: 1.0
 */
@Service
public class KuugaUserDetailService implements UserDetailsService {

    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User sysUser = userDao.findUserByName(userName);
        if(sysUser == null){
            throw new UsernameNotFoundException(userName);
        }
        //登录用户的权限
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        //登录用户可用性信息
        // 可用性 :true:可用 false:不可用
        boolean enabled = true;
        // 过期性 :true:没过期 false:过期
        boolean accountNonExpired = true;
        // 有效性 :true:凭证有效 false:凭证无效
        boolean credentialsNonExpired = true;
        // 锁定性 :true:未锁定 false:已锁定
        boolean accountNonLocked = true;
        List<SysRole> roleList = sysUser.getRoleList();
        for (SysRole role:roleList) {
            //角色必须是ROLE_开头，可以在数据库中设置
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getRole());
            grantedAuthorities.add(grantedAuthority);
        }
        return new org.springframework.security.core.userdetails.User(userName,passwordEncoder.encode(sysUser.getPassword()),enabled,accountNonExpired
                ,credentialsNonExpired,accountNonLocked,grantedAuthorities);
    }
}
