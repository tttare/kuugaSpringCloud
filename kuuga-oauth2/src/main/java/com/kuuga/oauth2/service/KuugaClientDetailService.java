package com.kuuga.oauth2.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuuga.api.system.model.SysClient;
import com.kuuga.oauth2.mapper.ClientDao;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author: qiuyongkang
 * @Description: 获取数据库用户和权限服务类
 * @Date: 2020/12/19 22:38
 * @Version: 1.0
 */
@Service
@Log
public class KuugaClientDetailService implements ClientDetailsService {

    @Autowired
    private ClientDao clientDao;

    /**
     * 根据客户端id查询
     * @param clientId
     * @return org.springframework.security.oauth2.provider.client.BaseClientDetails
     */
    public BaseClientDetails selectById(String clientId) {
        QueryWrapper<SysClient> wrapper = new QueryWrapper<SysClient>();
        wrapper.eq("client_id",clientId);
        SysClient sysClient = clientDao.selectOne(wrapper);
        BaseClientDetails clientDetails = new BaseClientDetails();
        clientDetails.setAuthorities(new ArrayList<>());
        clientDetails.setClientId(sysClient.getClientId());
        // 这个客户端秘钥和密码一样存BCryptPasswordEncoder加密后的接口，具体看定义的加密器
        clientDetails.setClientSecret(sysClient.getClientSecret());
        // 设置accessToken和refreshToken的时效，如果不设置则使tokenServices的配置的
        clientDetails.setAccessTokenValiditySeconds((int) TimeUnit.HOURS.toSeconds(sysClient.getAccessTokenValiditySecondsa()));
        clientDetails.setRefreshTokenValiditySeconds((int)TimeUnit.DAYS.toSeconds(sysClient.getRefreshTokenValiditySeconds()));
        // 资源id列表，需要注意的是这里配置的需要与ResourceServerConfig中配置的相匹配
        clientDetails.setResourceIds(sysClient.getResourceIdList());
        //客户端授权域
        clientDetails.setScope(sysClient.getScopeList());
        //认证方式
        clientDetails.setAuthorizedGrantTypes(sysClient.getGrantTypeList());
        //验证成功跳转url
        clientDetails.setRegisteredRedirectUri(sysClient.getRedirUrlSet());
        List<String> autoApproveScopes = new ArrayList<>(1);
        autoApproveScopes.add("sever");
        // 自动批准作用于，授权码模式时使用，登录验证后直接返回code，不再需要下一步点击授权
        clientDetails.setAutoApproveScopes(autoApproveScopes);
        return clientDetails;
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        log.info("客户端查询:" + clientId);
        BaseClientDetails baseClientDetails = this.selectById(clientId);
        if (baseClientDetails == null) {
            throw new NoSuchClientException("not found clientId:" + clientId);
        }
        return baseClientDetails;
    }
}
