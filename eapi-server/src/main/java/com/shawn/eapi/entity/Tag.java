package com.shawn.eapi.entity;

import com.shawn.eapi.dialect.AbstractTag;
import com.shawn.eapi.dialect.Schema;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = Schema.Tables.TAG)
public class Tag extends AbstractTag {

    /**
     * 名称
     */
    @Column(name = "NAME",length = 100)
    private String name;

    /**
     * 描述
     */
    @Column(name = "DESCRIPTION",length = 2000)
    private String description;

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
    private Date createTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
