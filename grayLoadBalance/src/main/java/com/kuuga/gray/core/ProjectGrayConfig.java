package com.kuuga.gray.core;

import java.util.*;

public class ProjectGrayConfig {

    private String projectId;
    private Map<String,String> linkServersVersion;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Map<String, String> getLinkServersVersion() {
        return linkServersVersion;
    }

    public void setLinkServersVersion(Map<String, String> linkServersVersionMap) {
        if(linkServersVersionMap==null || linkServersVersionMap.isEmpty()){
            return;
        }
        Set<String> keys = linkServersVersionMap.keySet();
        Map<String,String> lowKeyMap = new HashMap<>();
        for(String key:keys){
            lowKeyMap.put(key.toLowerCase(),linkServersVersionMap.get(key));
        }
        this.linkServersVersion = lowKeyMap;
    }
}
