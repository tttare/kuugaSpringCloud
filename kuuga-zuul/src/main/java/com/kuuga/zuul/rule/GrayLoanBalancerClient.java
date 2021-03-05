package com.kuuga.zuul.rule;

import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerRequest;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 主要为实现在 Rule类 中 拿到HttpRequest中有关灰度发布相关的信息
 * */
public class GrayLoanBalancerClient extends RibbonLoadBalancerClient {

    public GrayLoanBalancerClient(SpringClientFactory clientFactory) {
        super(clientFactory);
    }

    @Override
//    public <T> T execute(String serviceId, LoadBalancerRequest<T> request) throws IOException {
//        ILoadBalancer loadBalancer = getLoadBalancer(serviceId);
//        // 通过 request 到的request对象
//        // Server server = getServer(loadBalancer);
//        //获取 request对象向下传递
//        Server server = getServer(loadBalancer, request);
//        if (server == null) {
//            throw new IllegalStateException("No instances available for " + serviceId);
//        }
//        RibbonServer ribbonServer = new RibbonServer(serviceId, server, isSecure(server,
//                serviceId), serverIntrospector(serviceId).getMetadata(server));
//
//        return execute(serviceId, ribbonServer, request);
//    }

    //重写调用rule的
    protected Server getServer(ILoadBalancer loadBalancer, Object request) {
        //创建Map 传递到 rule类中
        Map<String,Object> key = new HashMap<String,Object>();
        key.put("loadBalancer",loadBalancer);
        key.put("request",request);
        return loadBalancer == null ? null : loadBalancer.chooseServer(key);
    }
}
