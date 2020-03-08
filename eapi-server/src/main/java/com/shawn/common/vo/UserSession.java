package com.shawn.common.vo;

import java.io.Serializable;
import java.util.Collection;

/**
 * <B>系统名称：</B>美美金融平台<BR>
 * <B>模块名称：</B>模块<BR>
 * <B>中文类名：</B><BR>
 * <B>概要说明：</B><BR>
 *
 * @author 北京鹏润美美科技有限公司
 * @since 2017年07月24日 18时45分
 */

public final class UserSession implements Serializable {

    private static final long serialVersionUID = -2970205842358364430L;

    private Long id;

    private String loginName;

    private boolean disabled;

    private String mobile;

    private Collection<String> roleNames;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Collection<String> getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(Collection<String> roleNames) {
        this.roleNames = roleNames;
    }

    @Override
    public String toString() {
        return "UserContext{" +
                "id=" + id +
                ", loginName='" + loginName + '\'' +
                ", disabled=" + disabled +
                ", mobile='" + mobile + '\'' +
                ", roleNames=" + roleNames +
                '}';
    }
}
