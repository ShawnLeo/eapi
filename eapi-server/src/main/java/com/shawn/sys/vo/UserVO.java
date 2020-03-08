package com.shawn.sys.vo;

import com.shawn.sys.util.PropertyScriptAssert;
import com.shawn.sys.util.UserStatus;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Max;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 用户管理vo
 * Created by wanglu-jf on 17/9/7.
 */
@PropertyScriptAssert(
        script = "_this.authPass == _this.confirmPassword",
        lang = "javascript",
        alias = "_this",
        property = "confirmPassword",
        message = "密码与确认密码不一致"
)
public class UserVO implements Serializable{
    protected static final long serialVersionUID = 4223968887407642044L;
    /**
     *真实姓名
     */
    @NotBlank(message = "真实姓名不能为空")
    @Length(max = 40,message = "真实姓名的长度在1~40个字符")
    private String realName;

    /**
     * 登录帐号
     */
    @NotBlank(message = "登陆账号不能为空")
    @Length(max = 40,message = "登陆账号的长度在1~40个字符")
    private String authId;

    /**
     * 认证密码
     */
    @NotBlank(message = "登陆密码不能为空")
    @Pattern(regexp = "^[0-9a-zA-Z]{6,18}$",message = "密码格式输入错误(6~18位字母数字的组合)")
    private String authPass;

    /**
     * 新登录密码
     */
    private String newPassword;

    /**
     * 密码确认
     */
    @NotBlank(message = "确认密码不能为空")
    @Pattern(regexp = "^[0-9a-zA-Z]{6,18}$",message = "确认密码格式输入错误(6~18位字母数字的组合)")
    private String confirmPassword;

    /**
     * 用户角色:
     */
    private String[] roles ;

    /**
     *性别 male(0,"男")，female(1,"女")
     */
    @NotBlank(message = "请选择用户性别")
    @Max(value = 1,message = "用户性别选择有误")
    private String gender;

    /**
     *用户状态 	LOCKED(-1, "锁定"),NORMAL(0, "正常");
     */
    @DecimalMax(value = "6",message = "用户状态有误")
    private String status = String.valueOf(UserStatus.NORMAL.getCode());

    /**
     *手机号
     */
    @NotBlank(message = "手机号码不能为空")
    @Pattern(regexp = "^1[34578]\\d{9}$",message = "手机号码格式错误")
    private String phone;

    /**
     *邮箱
     */
    @Email(message = "邮箱格式输入错误")
    @Length(max = 80,message = "邮箱的长度不能超过80个字符")
    private String email;

    /**
     *用户头像
     */
    @Length(max = 128,message = "用户头像地址的长度不能超过128个字符")
    private String  portraitPath;

    /**
     * 认证方式 1：手机号 2：email  3：昵称/账号
     */
    @DecimalMax(value = "6",message = "认证方式有误")
    private String authType = "3";

    /**
     *备注说明
     */
    @Length(max = 200,message = "备注说明的长度不能超过200个字符")
    private String  remarks;

    private Long id;
//
//    private Set<Role> roleSet= new HashSet<>();
//
//    public Set<Role> getRoleSet() {
//        return roleSet;
//    }
//
//    public void setRoleSet(Set<Role> roleSet) {
//        this.roleSet = roleSet;
//    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public String getAuthPass() {
        return authPass;
    }

    public void setAuthPass(String authPass) {
        this.authPass = authPass;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }


    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("UserVO{");
        sb.append("realName='").append(realName).append('\'');
        sb.append(", authId='").append(authId).append('\'');
        sb.append(", authPass='").append(authPass).append('\'');
        sb.append(", newPassword='").append(newPassword).append('\'');
        sb.append(", confirmPassword='").append(confirmPassword).append('\'');
        sb.append(", roles=").append(roles);
        sb.append(", gender='").append(gender).append('\'');
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", status='").append(status).append('\'');
        sb.append(", portraitPath='").append(portraitPath).append('\'');
        sb.append(", authType='").append(authType).append('\'');
        sb.append(", remarks='").append(remarks).append('\'');
        sb.append('}');
        return sb.toString();
    }


}
