package com.meimeitech.sys.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.meimeitech.sys.dialect.Schema.Tables;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = Tables.USERAUTH)
public class UserAuth /*extends AbstractUserAuth*/ implements Serializable {

    private static final long serialVersionUID = -993022480404121880L;

    /**
     * 认证账号
     */
    @Id
    @Column(name = "AUTH_ID",length = 40)
    private String authId;

    /**
     * 用户ID
     */
    @ManyToOne(cascade= CascadeType.ALL)
    @JoinColumn(name = "USER_ID",referencedColumnName = "id")
    @JsonIgnore
    private User user;

    /**
     * 认证方式 1：手机号 2：email  3：昵称
     */
    @Column(name = "AUTH_TYPE",length = 1)
    private String authType;

    /**
     * 认证密码
     */
    @Column(name = "AUTH_PASS",length = 256)
    private String authPass;

    /**
     * 登陆次数
     */
    @Column(name = "LOGIN_COUNT",length = 6)
    private String loginCount;

    /**
     * 最后登陆时间
     */
    @Column(name = "LAST_LOGIN_TIME")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date lastLoginTime;

    /**
     * 修改密码时间
     */
    @Column(name = "PASS_TIME")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date passTime;

    /**
     *创建时间
     */
    @Column(name = "CREATE_TIME")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createTime = new Date();

    /**
     *更新者
     */
    @Column(name = "UPDATE_BY",length = 40)
    private String updateBy;

    /**
     *更新时间
     */
    @Column(name = "UPDATE_TIME")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date updateTime;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getAuthPass() {
        return authPass;
    }

    public void setAuthPass(String authPass) {
        this.authPass = authPass;
    }

    public String getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(String loginCount) {
        this.loginCount = loginCount;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Date getPassTime() {
        return passTime;
    }

    public void setPassTime(Date passTime) {
        this.passTime = passTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("UserAuth{");
        sb.append("authId=").append(authId);
//        sb.append(", userId=").append(user.getId());
        sb.append(", authType='").append(authType).append('\'');
        sb.append(", authPass='").append(authPass).append('\'');
        sb.append(", loginCount='").append(loginCount).append('\'');
        sb.append(", lastLoginTime=").append(lastLoginTime);
        sb.append(", passTime=").append(passTime);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateBy=").append(updateBy);
        sb.append(", updateTime=").append(updateTime);
        sb.append('}');
        return sb.toString();
    }
}
