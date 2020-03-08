package com.shawn.sys.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

/**
 * EmailResetReq
 */
@Validated

public class EmailResetReq   {
  @JsonProperty("resetCode")
  private String resetCode = null;

  @JsonProperty("password")
  private String password = null;

  @JsonProperty("newPassword")
  private String newPassword = null;

  public EmailResetReq resetCode(String resetCode) {
    this.resetCode = resetCode;
    return this;
  }

  /**
   * Get resetCode
   * @return resetCode
  **/


  public String getResetCode() {
    return resetCode;
  }

  public void setResetCode(String resetCode) {
    this.resetCode = resetCode;
  }

  public EmailResetReq password(String password) {
    this.password = password;
    return this;
  }

  /**
   * Get password
   * @return password
  **/


  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public EmailResetReq newPassword(String newPassword) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EmailResetReq emailResetReq = (EmailResetReq) o;
    return Objects.equals(this.resetCode, emailResetReq.resetCode) &&
        Objects.equals(this.password, emailResetReq.password) &&
        Objects.equals(this.newPassword, emailResetReq.newPassword);
  }

  @Override
  public int hashCode() {
    return Objects.hash(resetCode, password, newPassword);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("{\n");
    
    sb.append("    resetCode: ").append(toIndentedString(resetCode)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    newPassword: ").append(toIndentedString(newPassword)).append("\n");
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

