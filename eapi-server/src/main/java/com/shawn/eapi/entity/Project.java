package com.shawn.eapi.entity;

import com.shawn.eapi.dialect.AbstractProject;
import com.shawn.eapi.dialect.Schema;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = Schema.Tables.PROJECT)
public class Project extends AbstractProject {

    /**
     * 项目名称
     */
    @Column(name = "TITLE",length = 100)
    private String title;

    @Column(name = "DESCRIPTION",length = 2000)
    private String description;

    @Column(name = "VERSION",length = 32)
    private String version;

    @Column(name = "CONTACT_EMAIL",length = 100)
    private String contactEmail;

    @Column(name = "HOST",length = 100)
    private String host;

    @Column(name = "BASE_PATH",length = 200)
    private String basePath;

    @Column(name = "DEPLOY_URL",length = 200)
    private String deployUrl;

    @Column(name = "GROUPID",length = 40)
    private String groupId;
//    @Column(name = "description",length = 100)
//    schemes

    /**
     * 是否开启通用响应
     */
    @Column(name = "COMMON_RESPONSE")
    private Boolean commonResponse;

    /**
     * 通用数据模型
     */
    @Column(name = "COMMON_RESPONSE_ID", length = 100)
    private String commonResponseId;

    /**
     * 字段名
     */
    @Column(name = "COMMON_RESPONSE_FIELD", length = 100)
    private String commonResponseField;

    /**
     * 创建人
     */
    @Column(name = "CREATER",length = 40)
    private String creater;


    /**
     * 创建人昵称
     */
    @Column(name = "CREATER_USERNAME",length = 64)
    private String createrUserName;

    /**
     * 创建时间
     */
    @Column(name = "CREATE_TIME")
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

    public Boolean getCommonResponse() {
        return commonResponse;
    }

    public void setCommonResponse(Boolean commonResponse) {
        this.commonResponse = commonResponse;
    }

    public String getCommonResponseId() {
        return commonResponseId;
    }

    public void setCommonResponseId(String commonResponseId) {
        this.commonResponseId = commonResponseId;
    }

    public String getCommonResponseField() {
        return commonResponseField;
    }

    public void setCommonResponseField(String commonResponseField) {
        this.commonResponseField = commonResponseField;
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
}
