package com.kuuga.zuul.filter;

import com.google.gson.Gson;
import com.kuuga.gray.core.GrayContants;
import com.kuuga.gray.core.ProjectGrayConfig;
import com.kuuga.zuul.config.GrayConfig;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 灰度用户过滤器
 * 用于 给配置中心的灰度用户,指定需要访问的微服务版本
 * */
@Component
@RefreshScope
public class GrayUserFilter extends ZuulFilter {

    @Value("${gray.stableConfig}")
    private String stableConfig;

    @Value("${gray.grayProjectConfig}")
    private String grayProjectConfig;


    /**
     * 过滤器的类型。可选值有：
     * pre - 前置过滤
     * route - 路由后过滤
     * error - 异常过滤
     * post - 远程服务调用后过滤
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 同种类的过滤器的执行顺序。
     * 按照返回值的自然升序执行。
     */
    @Override
    public int filterOrder() {
        return 0;
    }


    /**
     * 返回boolean类型。代表当前filter是否生效。
     * 默认值为false。
     * 返回true代表开启filter。
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * run方法就是过滤器的具体逻辑。
     * return 可以返回任意的对象，当前实现忽略。（spring-cloud-zuul官方解释）
     * 直接返回null即可。
     */
    @Override
    public Object run() throws ZuulException {
        GrayConfig grayConfig = readGrayConfig();
        Gson gson = new Gson();
        // 通过zuul，获取请求上下文
        RequestContext rc = RequestContext.getCurrentContext();
        HttpServletRequest request = rc.getRequest();
        //请求头拿到projectId(实际情况应该是token,再获取projectId)
        String projectId = request.getHeader(GrayContants.PROJECT_ID);
        //拿到 灰度用户配置(实际情况应该是配置中心获取);
        List<ProjectGrayConfig> grayProjectConfigList = grayConfig.getGrayProjectConfig();
        for (ProjectGrayConfig projectGrayConfig:grayProjectConfigList) {
            if(projectGrayConfig.getProjectId().equals(projectId)){
                Map<String, String> projectVersions = projectGrayConfig.getLinkServersVersion();
                if(projectVersions!=null&&!projectVersions.isEmpty()){
                    request.setAttribute(GrayContants.GRAY_PROJECT_CONFIG,gson.toJson(projectVersions).toString());
                    rc.addZuulRequestHeader(GrayContants.GRAY_PROJECT_CONFIG,gson.toJson(projectVersions).toString());
                }
            }
        }
        Map<String, String> stableMap = grayConfig.getStableConfig();
        //将版本信息存入请求头 全链路 灰度用户都依照此 选取服务版本
        if(stableMap==null||stableMap.isEmpty()){
            throw new IllegalArgumentException("参数非法，未配置灰度发布服务基础配置参数"+GrayContants.STABLE_CONFIG+"！");
        }
        request.setAttribute(GrayContants.STABLE_CONFIG,gson.toJson(stableMap).toString());
        rc.addZuulRequestHeader(GrayContants.STABLE_CONFIG,gson.toJson(stableMap).toString());
        return null;
    }

    /**
     * 读取配置文件中的灰度配置
     * */
    public GrayConfig readGrayConfig(){
        GrayConfig grayConfig = new GrayConfig();
        if(StringUtils.isEmpty(stableConfig)){
            throw new IllegalArgumentException("参数非法，未配置灰度发布服务基础配置参数"+GrayContants.STABLE_CONFIG+"！");
        }
        Gson gson = new Gson();
        HashMap stableConfigMap = gson.fromJson(stableConfig, HashMap.class);
        grayConfig.setStableConfig(stableConfigMap);
        if(!StringUtils.isEmpty(grayProjectConfig)){
            ProjectGrayConfig[] projectGrayConfigs = gson.fromJson(grayProjectConfig, ProjectGrayConfig[].class);
            grayConfig.setGrayProjectConfig(Arrays.asList(projectGrayConfigs));
        }
        return grayConfig;
    }

}
