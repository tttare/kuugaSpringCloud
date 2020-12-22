package com.kuuga.api.system.model;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.util.Date;
import java.util.List;

/**
 * @ClassName: SysClient
 * @Author: qiuyongkang
 * @Description: 系统用户接口
 * @Date: 2020/12/19 19:59
 * @Version: 1.0
 */
@Setter
@Getter
@TableName("tb_article")
@NoArgsConstructor
public class User {

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String userId;
    private String userName; //登录用户名
    private String nickName;//名称（昵称或者真实姓名，根据实际情况定义）
    private String password;
    @TableField(value = "birth_date")
    private String birthDate;
    @TableField(value = "status")
    private String state;//用户状态,0:创建未认证（比如没有激活，没有输入验证码等等）--等待验证的用户 , 1:正常状态,2：用户被锁定.
    private List<SysRole> roleList;// 一个用户具有多个角色
    @TableField(value = "create_date")
    private Date createDate;//创建时间
    @TableField(value = "expired_date")
    private Date expiredDate;//过期日期
    @TableField(value = "lastLogin_date")
    private Date lastLoginDate;//上次登录时间
    @TableField(value = "update_date")
    private Data update_date;//上次更新时间
    private String email;//邮箱
    @TableField(value = "avatar")
    private String iconPath;//头像路径

}
