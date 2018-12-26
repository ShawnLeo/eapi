package com.meimeitech.sys.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * 用户管理vo
 */
@Validated

public class UserVO   {
  @JsonProperty("realName")
  private String realName = null;

  @JsonProperty("authId")
  private String authId = null;

  @JsonProperty("authPass")
  private String authPass = null;

  @JsonProperty("newPassword")
  private String newPassword = null;

  @JsonProperty("confirmPassword")
  private String confirmPassword = null;

  @JsonProperty("roles")
  @Valid
  private List<String> roles = null;

  @JsonProperty("gender")
  private String gender = null;

  @JsonProperty("status")
  private String status = null;

  @JsonProperty("phone")
  private String phone = null;

  @JsonProperty("email")
  private String email = null;

  @JsonProperty("portraitPath")
  private String portraitPath = null;

  @JsonProperty("authType")
  private String authType = null;

  @JsonProperty("remarks")
  private String remarks = null;

  @JsonProperty("id")
  private String id = null;

  public UserVO realName(String realName) {
    this.realName = realName;
    return this;
  }

  /**
   * Get realName
   * @return realName
  **/


  public String getRealName() {
    return realName;
  }

  public void setRealName(String realName) {
    this.realName = realName;
  }

  public UserVO authId(String authId) {
    this.authId = authId;
    return this;
  }

  /**
   * Get authId
   * @return authId
  **/


  public String getAuthId() {
    return authId;
  }

  public void setAuthId(String authId) {
    this.authId = authId;
  }

  public UserVO authPass(String authPass) {
    this.authPass = authPass;
    return this;
  }

  /**
   * Get authPass
   * @return authPass
  **/


  public String getAuthPass() {
    return authPass;
  }

  public void setAuthPass(String authPass) {
    this.authPass = authPass;
  }

  public UserVO newPassword(String newPassword) {
    this.newPassword = newPassword;
    return this;
  }

  /**
   * Get newPassword
   * @return newPassword
  **/


  public String getNewPassword() {
    return newPassword;
  }

  public void setNewPassword(String newPassword) {
    this.newPassword = newPassword;
  }

  public UserVO confirmPassword(String confirmPassword) {
    this.confirmPassword = confirmPassword;
    return this;
  }

  /**
   * Get confirmPassword
   * @return confirmPassword
  **/


  public String getConfirmPassword() {
    return confirmPassword;
  }

  public void setConfirmPassword(String confirmPassword) {
    this.confirmPassword = confirmPassword;
  }

  public UserVO roles(List<String> roles) {
    this.roles = roles;
    return this;
  }

  public UserVO addRolesItem(String rolesItem) {
    if (this.roles == null) {
      this.roles = new ArrayList<String>();
    }
    this.roles.add(rolesItem);
    return this;
  }

  /**
   * Get roles
   * @return roles
  **/


  public List<String> getRoles() {
    return roles;
  }

  public void setRoles(List<String> roles) {
    this.roles = roles;
  }

  public UserVO gender(String gender) {
    this.gender = gender;
    return this;
  }

  /**
   * Get gender
   * @return gender
  **/


  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public UserVO status(String status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
  **/


  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public UserVO phone(String phone) {
    this.phone = phone;
    return this;
  }

  /**
   * Get phone
   * @return phone
  **/


  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public UserVO email(String email) {
    this.email = email;
    return this;
  }

  /**
   * Get email
   * @return email
  **/


  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public UserVO portraitPath(String portraitPath) {
    this.portraitPath = portraitPath;
    return this;
  }

  /**
   * Get portraitPath
   * @return portraitPath
  **/


  public String getPortraitPath() {
    return portraitPath;
  }

  public void setPortraitPath(String portraitPath) {
    this.portraitPath = portraitPath;
  }

  public UserVO authType(String authType) {
    this.authType = authType;
    return this;
  }

  /**
   * Get authType
   * @return authType
  **/


  public String getAuthType() {
    return authType;
  }

  public void setAuthType(String authType) {
    this.authType = authType;
  }

  public UserVO remarks(String remarks) {
    this.remarks = remarks;
    return this;
  }

  /**
   * Get remarks
   * @return remarks
  **/


  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public UserVO id(String id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
  **/


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserVO userVO = (UserVO) o;
    return Objects.equals(this.realName, userVO.realName) &&
        Objects.equals(this.authId, userVO.authId) &&
        Objects.equals(this.authPass, userVO.authPass) &&
        Objects.equals(this.newPassword, userVO.newPassword) &&
        Objects.equals(this.confirmPassword, userVO.confirmPassword) &&
        Objects.equals(this.roles, userVO.roles) &&
        Objects.equals(this.gender, userVO.gender) &&
        Objects.equals(this.status, userVO.status) &&
        Objects.equals(this.phone, userVO.phone) &&
        Objects.equals(this.email, userVO.email) &&
        Objects.equals(this.portraitPath, userVO.portraitPath) &&
        Objects.equals(this.authType, userVO.authType) &&
        Objects.equals(this.remarks, userVO.remarks) &&
        Objects.equals(this.id, userVO.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(realName, authId, authPass, newPassword, confirmPassword, roles, gender, status, phone, email, portraitPath, authType, remarks, id);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("{\n");
    
    sb.append("    realName: ").append(toIndentedString(realName)).append("\n");
    sb.append("    authId: ").append(toIndentedString(authId)).append("\n");
    sb.append("    authPass: ").append(toIndentedString(authPass)).append("\n");
    sb.append("    newPassword: ").append(toIndentedString(newPassword)).append("\n");
    sb.append("    confirmPassword: ").append(toIndentedString(confirmPassword)).append("\n");
    sb.append("    roles: ").append(toIndentedString(roles)).append("\n");
    sb.append("    gender: ").append(toIndentedString(gender)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    phone: ").append(toIndentedString(phone)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    portraitPath: ").append(toIndentedString(portraitPath)).append("\n");
    sb.append("    authType: ").append(toIndentedString(authType)).append("\n");
    sb.append("    remarks: ").append(toIndentedString(remarks)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

