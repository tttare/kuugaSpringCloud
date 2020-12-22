package com.kuuga.api.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @ClassName: SysClient
 * @Author: qiuyongkang
 * @Description: oauth2 客户端设置类
 * @Date: 2020/12/8 22:59
 * @Version: 1.0
 */
@Data
@TableName("tb_client")
@NoArgsConstructor
public class SysClient {

    @TableField(value = "client_id")
    private String clientId;
    @TableField(value = "client_secret")
    private String clientSecret;
    @TableField(value = "access_token_times")
    private Integer accessTokenValiditySecondsa;//token有效时长
    @TableField(value = "refresh_token_times")
    private Integer refreshTokenValiditySeconds;//token刷新时长
    @TableField(value = "resource_ids")
    private String resourceIds;//client对应的资源ids,用 , 拼接
    private String scopes;//client对应的scope,用 , 拼接
    @TableField(value = "grant_types")
    private String grantTypes;//客户端对应的授权模式,用 , 拼接
    @TableField(value = "registered_redir_urls")
    private String registeredRedirectUrls;//授权或验证成功后 跳转的url,用 , 拼接

    public List<String> getResourceIdList(){
        if(StringUtils.isEmpty(resourceIds)){
            return new ArrayList<String>();
        }
        return new ArrayList<>(Arrays.asList(resourceIds.split(",")));
    }

    public List<String> getScopeList(){
        if(StringUtils.isEmpty(scopes)){
            return new ArrayList<String>();
        }
        return new ArrayList<>(Arrays.asList(scopes.split(",")));
    }

    public List<String> getGrantTypeList(){
        if(StringUtils.isEmpty(grantTypes)){
            return new ArrayList<String>();
        }
        return new ArrayList<>(Arrays.asList(grantTypes.split(",")));
    }

    public Set<String> getRedirUrlSet(){
        if(StringUtils.isEmpty(registeredRedirectUrls)){
            return new HashSet<String>();
        }
        return new HashSet<>(Arrays.asList(registeredRedirectUrls.split(",")));
    }

}
