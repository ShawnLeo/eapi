package com.shawn.sys.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

/**
 * 用户注册
 */
@Validated

public class RegisterReq   {
  @JsonProperty("email")
  private String email = null;

  @JsonProperty("password")
  private String password = null;

  @JsonProperty("kaptchaCodeId")
  private String kaptchaCodeId = null;

  public RegisterReq email(String email) {
    this.email = email;
    return this;
  }

  /**
   * 邮箱
   * @return email
  **/


  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public RegisterReq password(String password) {
    this.password = password;
    return this;
  }

  /**
   * 密码
   * @return password
  **/


  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public RegisterReq kaptchaCodeId(String kaptchaCodeId) {
    this.kaptchaCodeId = kaptchaCodeId;
    return this;
  }

  /**
   * 验证码Id
   * @return kaptchaCodeId
  **/


  public String getKaptchaCodeId() {
    return kaptchaCodeId;
  }

  public void setKaptchaCodeId(String kaptchaCodeId) {
    this.kaptchaCodeId = kaptchaCodeId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegisterReq registerReq = (RegisterReq) o;
    return Objects.equals(this.email, registerReq.email) &&
        Objects.equals(this.password, registerReq.password) &&
        Objects.equals(this.kaptchaCodeId, registerReq.kaptchaCodeId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(email, password, kaptchaCodeId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("{\n");
    
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    kaptchaCodeId: ").append(toIndentedString(kaptchaCodeId)).append("\n");
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

