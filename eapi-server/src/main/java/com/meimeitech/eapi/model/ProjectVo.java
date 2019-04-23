package com.meimeitech.eapi.model;

import com.meimeitech.eapi.dialect.AbstractProject;
import java.util.Date;

public class ProjectVo extends AbstractProject {

    /**
     * 项目名称
     */
    private String title;

    private String description;

    private String version;

    private String contactEmail;

    private String host;

    private String basePath;

    private String deployUrl;

    private String groupId;

    private String groupName;

    /**
     * 创建人
     */
    private String creater;


    /**
     * 创建人昵称
     */
    private String createrUserName;

    /**
     * 创建时间
     */
    private Date createTime;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getDeployUrl() {
        return deployUrl;
    }

    public void setDeployUrl(String deployUrl) {
        this.deployUrl = deployUrl;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getCreaterUserName() {
        return createrUserName;
    }

    public void setCreaterUserName(String createrUserName) {
        this.createrUserName = createrUserName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
