package com.kuuga.oauth2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuuga.api.system.model.User;
import org.springframework.stereotype.Repository;

/**
 * @ProjectName: springcloud
 * @Package: com.kuuga.oauth2.mapper
 * @ClassName: UserDao
 * @Author: mi
 * @Description: ${description}
 * @Date: 2020/12/2 20:08
 * @Version: 1.0
 */
@Repository
public interface UserDao extends BaseMapper<User> {

    User findUserByName(String userName);
}
