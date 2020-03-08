package com.shawn.sys.entity;


import com.shawn.sys.dialect.AbstractRegUser;
import com.shawn.sys.dialect.Schema.Tables;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = Tables.REG_USER)
public class RegUser extends AbstractRegUser {

    private static final long serialVersionUID = -5228484640414835579L;
    /**
     *邮箱
     */
    @Column(name = "EMAIL",length = 80)
    private String email;

    /**
     *创建时间
     */
    @Column(name = "CREATE_TIME")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createTime = new Date();


    /**
     * 认证密码
     */
    @Column(name = "PASSWORD",length = 256)
    private String password;

    /**
     * 激活码
     */
    @Column(name = "ACTIVATE_CODE",length = 256)
    private String activateCode;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getActivateCode() {
        return activateCode;
    }

    public void setActivateCode(String activateCode) {
        this.activateCode = activateCode;
    }
}
