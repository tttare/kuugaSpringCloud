package com.kuuga.gray.rule;

import com.google.common.base.Optional;
import com.google.gson.Gson;
import com.kuuga.gray.core.GrayContants;
import com.netflix.loadbalancer.*;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * date: 2021/03/04 16:54<br/>
 * @author: tttare<br />
 * 灰度发布分流Rule
 * 按配置中心的灰度规则进行新旧版本的测试
 * 为指定的灰度测试用户指定特定的新版本服务器
 * 未被指定未灰度测试的用户继续走默认的旧版本服务器
 * 负载均衡策略 轮询
 * */
public class GrayBalanceRule extends ZoneAvoidanceRule {

    private LoadBalancerStats loadBalancerStats;

    /**
     * 筛选服务
     * */
    @Override
    public Server choose(Object key) {
        String targetVersion = null;
        //eureka上服务很多 但不是所有服务都是直接可选的，需要通过服务器版本规则筛选
        if (this.loadBalancerStats == null) {
            return super.choose(key);
        }
        String appName = this.loadBalancerStats.getName().toLowerCase();
        //微服务注册的服务器集合
        List<Server> serverList = this.getLoadBalancer().getAllServers();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String stableConfigStr = (String)request.getAttribute(GrayContants.STABLE_CONFIG);
        if(StringUtils.isEmpty(stableConfigStr)){
            throw new IllegalArgumentException("参数非法，未配置灰度发布服务基础配置参数"+GrayContants.STABLE_CONFIG+"！");
        }
        Gson gson = new Gson();
        Map<String,String> stableConfigMap = gson.fromJson(stableConfigStr, Map.class);
        String stableVersion = stableConfigMap.get(appName);
        if(StringUtils.isEmpty(stableVersion)){
            throw new IllegalArgumentException("参数非法，服务"+appName+"未配置稳定版本！");
        }
        String grayProjectConfigStr = (String)request.getAttribute(GrayContants.GRAY_PROJECT_CONFIG);
        if(StringUtils.isEmpty(grayProjectConfigStr)){
            targetVersion = stableVersion;
        }else{
            Map<String,String> grayProjectConfigMap = gson.fromJson(grayProjectConfigStr, Map.class);
            String grayVersion = grayProjectConfigMap.get(appName);
            if(StringUtils.isEmpty(grayVersion)){
                targetVersion = stableVersion;//选择稳定版本
            }else{
                targetVersion = grayVersion;//选中灰度版本
            }
        }
        //使用 targetVersion 过滤server
        List<Server> targetServer = new ArrayList<Server>();
        for (Server server:serverList) {
            //DiscoveryEnabledServer Robbin与eureka 整合后的 Server对象
            DiscoveryEnabledServer disServer = (DiscoveryEnabledServer)server;
            //获取当前服务器版本
            Map<String, String> metadata = disServer.getInstanceInfo().getMetadata();
            if(metadata==null||metadata.get("version")==null){
                throw new IllegalArgumentException("参数非法，"+appName+"服务实例需配置eureka.instance.metadata-map.version！");
            }
            if(metadata.get("version").equals(targetVersion)){
                targetServer.add(server);
            }
        }
        if(targetServer.isEmpty()){
            throw new IllegalArgumentException("服务："+appName+"未注册版本为："+targetVersion+"的实例");
        }
        //从服务器中，轮询获取可用实例
        Optional<Server> server = this.getPredicate().chooseRoundRobinAfterFiltering(targetServer, key);
        return server.isPresent() ? (Server)server.get() : null;
    }

    public void setLoadBalancer(ILoadBalancer lb) {
        super.setLoadBalancer(lb);
        if (lb instanceof AbstractLoadBalancer) {
            this.loadBalancerStats = ((AbstractLoadBalancer)lb).getLoadBalancerStats();
        }

    }
}
