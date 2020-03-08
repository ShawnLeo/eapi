package com.shawn.eapi.entity;

import com.shawn.eapi.dialect.AbstractGroup;
import com.shawn.eapi.dialect.Schema;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = Schema.Tables.GROUP)
public class Group extends AbstractGroup {

    /**
     * Group名称
     */
    @Column(name = "NAME",length = 40)
    private String name ;

    /**
     * 用户申请验证方式
     */
    @Column(name = "VERIFY",length = 40)
    private String verify;

    /**
     * 自动通过为（角色）
     */
    @Column(name = "VERIFY_ROLE",length = 40)
    private String verifyRole;

    /**
     * 描述
     */
    @Column(name = "DESCRIPTION",length = 40)
    private String description;

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


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }

    public String getVerifyRole() {
        return verifyRole;
    }

    public void setVerifyRole(String verifyRole) {
        this.verifyRole = verifyRole;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
