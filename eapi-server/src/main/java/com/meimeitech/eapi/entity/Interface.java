package com.meimeitech.eapi.entity;

import com.meimeitech.eapi.dialect.AbstractInterface;
import com.meimeitech.eapi.dialect.Schema;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.CascadeType.REFRESH;

@Entity
@Table(name = Schema.Tables.INTERFACE)
public class Interface extends AbstractInterface {

    /**
     * 名称
     */
    @Column(name = "NAME",length = 120)
    private String name;

    /**
     * 方法
     */
    @Column(name = "METHOD",length = 10)
    private String method;

    /**
     * 路径
     */
    @Column(name = "PATH",length = 100)
    private String path;

    /**
     * 接口标签
     */
    @ManyToMany(targetEntity = Tag.class,fetch = FetchType.LAZY, cascade = { REFRESH })
    @JoinTable(
            name = Schema.Tables.INTERFACE_TAG,
            joinColumns = @JoinColumn(name = Schema.Columns.INTERFACE_ID) ,
            inverseJoinColumns = @JoinColumn(name = Schema.Columns.TAG_ID)
    )
    private Set<Tag> tags = new HashSet<>();

    /**
     * 操作方法
     */
    @Column(name = "OPERATION_ID",length = 32)
    private String operationId;

    /**
     * 描述
     */
    @Column(name = "DESCRIPTION",length = 2000)
    private String description;

    /**
     * 弃用
     */
    @Column(name = "DEPRECATED")
    private boolean deprecated;

    /**
     * 状态  100-未开始 200-开发中 300-测试中 400-已完成
     */
    @Column(name = "STATUS")
    private short status;

    /**
     * 排序
     */
    @Column(name = "DISPLAYORDER",length = 10)
    private Integer displayOrder;

    /**
     * 项目Id
     */
    @Column(name = "PROJECT_ID",length = 40)
    private String projectId;

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

    /**
     * 请求类型
     */
    @Column(name = "REQUEST_TYPE",length = 10)
    private String requestType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDeprecated() {
        return deprecated;
    }

    public void setDeprecated(boolean deprecated) {
        this.deprecated = deprecated;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
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

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }
}
