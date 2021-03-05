package com.kuuga.gray.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public void setLinkServersVersion(Map<String, String> linkServersVersion) {
        this.linkServersVersion = linkServersVersion;
    }
}
