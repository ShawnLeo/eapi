package com.shawn.sys.entity;


import com.shawn.sys.dialect.AbstractUser;
import com.shawn.sys.dialect.Schema.Tables;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = Tables.USER)
public class User extends AbstractUser {

    private static final long serialVersionUID = -5228484640414835579L;
    /**
     *真实姓名
     */
    @Column(name = "REAL_NAME",length = 40)
    private String realName;

    /**
     *用户状态 	LOCKED(-1, "锁定"),NORMAL(0, "正常"),UNLOCK(1, "解锁");
     */
    @Column(name = "STATUS",length = 2)
    private String status;

    /**
     *性别 male(0,"男")，female(1,"女")
     */
    @Column(name = "GENDER",length = 1)
    private String gender;

    /**
     *手机号
     */
    @Column(name = "PHONE",length = 20)
    private String phone;

    /**
     *邮箱
     */
    @Column(name = "EMAIL",length = 80)
    private String email;

    /**
     *用户头像
     */
    @Column(name = "PORTRAIT_PATH",length = 128)
    private String  portraitPath;

    /**
     *备注说明
     */
    @Column(name = "REMARKS",length = 200)
    private String  remarks;

    /**
     *创建者
     */
    @Column(name = "CREATE_BY",length = 40)
    private String  createBy;

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

    /**
     * 删除者
     */
    @Column(name = "DELETE_BY",length = 40)
    private String  deleteBy;

    /**
     * 删除时间
     */
    @Column(name = "DELETE_TIME")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date deleteTime;

//    @ManyToMany(targetEntity = Role.class,fetch = FetchType.LAZY, cascade = { REFRESH, DETACH })
//    @JoinTable(
//            name = Tables.USERROLE,
//            joinColumns = @JoinColumn(name = Columns.USERROLE_USERID) ,
//            inverseJoinColumns = @JoinColumn(name = Columns.USERROLE_ROLEID)
//    )
//
//	private Set<Role> roles = new HashSet<Role>();


    @Transient
    private Set<Long> roleIds;


//    public Set<Long> getRoleIds() {
//        if (roleIds == null) {
//            roleIds = new HashSet<Long>();
//            if (getRoles() != null) {
//                for (Role role : getRoles()) {
//                    roleIds.add(role.getId());
//                }
//            }
//        }
//        return roleIds;
//    }

    public void setRoleIds(Set<Long> roleNames) {
        this.roleIds = roleIds;
    }




    @OneToMany(/*cascade = CascadeType.ALL,*/mappedBy = "user")
//    @JoinColumn(name = "id")
    private Set<UserAuth> userAuths = new HashSet<UserAuth>();


    @Transient
    private Set<String> authIds;

    public Set<String> getAuthIds() {
        if (authIds == null) {
            authIds = new HashSet<String>();
            if (getUserAuths() != null) {
                for (UserAuth auth : getUserAuths()) {
                    authIds.add(auth.getAuthId());
                }
            }
        }
        return authIds;
    }

    public void setAuthIds(Set<String> authIds) {
        this.authIds = authIds;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPortraitPath() {
        return portraitPath;
    }

    public void setPortraitPath(String portraitPath) {
        this.portraitPath = portraitPath;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
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

    public String getDeleteBy() {
        return deleteBy;
    }

    public void setDeleteBy(String deleteBy) {
        this.deleteBy = deleteBy;
    }

    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }
//
//    public Set<Role> getRoles() {
//        return roles;
//    }
//
//    public void setRoles(Set<Role> roles) {
//        this.roles = roles;
//    }

    public Set<UserAuth> getUserAuths() {
        return userAuths;
    }

    public void setUserAuths(Set<UserAuth> userAuths) {
        this.userAuths = userAuths;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("User{");
        sb.append("realName='").append(realName).append('\'');
        sb.append(", status='").append(status).append('\'');
        sb.append(", gender='").append(gender).append('\'');
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", portraitPath='").append(portraitPath).append('\'');
        sb.append(", remarks='").append(remarks).append('\'');
        sb.append(", createBy='").append(createBy).append('\'');
        sb.append(", createTime=").append(createTime);
        sb.append(", updateBy=").append(updateBy);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", deleteBy='").append(deleteBy).append('\'');
        sb.append(", deleteTime=").append(deleteTime);
//        sb.append(", roles=").append(roles);
        sb.append(", userAuths=").append(userAuths);
        sb.append('}');
        return sb.toString();
    }
}
