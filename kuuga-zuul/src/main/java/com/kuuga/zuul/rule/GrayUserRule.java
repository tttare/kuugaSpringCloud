package com.kuuga.zuul.rule;

import com.google.gson.Gson;
import com.netflix.loadbalancer.*;
import com.netflix.niws.loadbalancer.DiscoveryEnabledNIWSServerList;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import com.netflix.zuul.context.RequestContext;
import org.springframework.cloud.client.loadbalancer.LoadBalancerRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 灰度发布分流Rule
 * 按用户指定的版本,为用户分流
 * */
public class GrayUserRule extends ZoneAvoidanceRule {

    private LoadBalancerStats loadBalancerStats;

    @Override
    public Server choose(Object key) {
        //灰度发布不应该影响原有的负载均衡逻辑
        List<Server> grayServer = new ArrayList<Server>();//灰度测试服务器集合
        List<Server> preServer =  new ArrayList<Server>();//较早版本的服务器集合
        //获取HttpRequest
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String garyConfig = (String)request.getAttribute("garyConfig");
        Gson gson = new Gson();
        Map garyConfigMap = gson.fromJson(garyConfig, Map.class);
        if (this.loadBalancerStats == null) {
            return super.choose(key);
        } else {
            String appName = this.loadBalancerStats.getName();
            List<Server> serverList = this.getLoadBalancer().getAllServers();
            int minimalConcurrentConnections = 2147483647;
            long currentTime = System.currentTimeMillis();
            Server chosen = null;
            Iterator var7 = serverList.iterator();
            Map<Integer,Server> serverMap = new HashMap<Integer,Server>();
            Integer total = 0;
            while(var7.hasNext()) {
                Server server = (Server)var7.next();
                ServerStats serverStats = this.loadBalancerStats.getSingleServerStat(server);
                if (!serverStats.isCircuitBreakerTripped(currentTime)) {
                    //通过用户灰度设置分流
                    Server.MetaInfo metaInfo = server.getMetaInfo();//获取服务器版本信息
                    //robbin 与 eureka结合时,服务器信息将维护在  DiscoveryEnabledServer 类中
                    if(server instanceof DiscoveryEnabledServer ){
                        DiscoveryEnabledServer disServer = (DiscoveryEnabledServer)server;
                        //获取当前服务器版本
                        Map<String, String> metadata = disServer.getInstanceInfo().getMetadata();
                        if(metadata==null||metadata.get("version")==null){
                            throw new IllegalArgumentException("参数非法，服务器实例需配置eureka.instance.metadata-map.version！");
                        }
                        String instanceId = disServer.getMetaInfo().getInstanceId();
                        String grayUserVersion = garyConfigMap.get(appName)==null?null:garyConfigMap.get(appName).toString();//灰度用户分流的服务版本
                        String serverVersion = metadata.get("version");//服务实例版本
                        if(grayUserVersion!=null && grayUserVersion.equals(serverVersion)){
                            grayServer.add(disServer);
                        }else{
                            preServer.add(server);
                        }
                    }else{
                        throw new IllegalArgumentException("参数非法，不是DiscoveryEnabledServer实例！");
                    }
                }
            }
        }
        return super.choose(key);
    }

    public void setLoadBalancer(ILoadBalancer lb) {
        super.setLoadBalancer(lb);
        if (lb instanceof AbstractLoadBalancer) {
            this.loadBalancerStats = ((AbstractLoadBalancer)lb).getLoadBalancerStats();
        }

    }
}
