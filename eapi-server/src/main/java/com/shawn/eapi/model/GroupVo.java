package com.shawn.eapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

/**
 * 项目组
 */
@Validated

public class GroupVo {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("verify")
  private String verify = null;

  @JsonProperty("verifyRole")
  private String verifyRole = null;

  @JsonProperty("description")
  private String description = null;

  public GroupVo id(String id) {
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

  public GroupVo name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
  **/


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public GroupVo verify(String verify) {
    this.verify = verify;
    return this;
  }

  /**
   * 用户申请验证方式（1-需要管理员验证 2-自动通过）
   * @return verify
  **/


  public String getVerify() {
    return verify;
  }

  public void setVerify(String verify) {
    this.verify = verify;
  }

  public GroupVo verifyRole(String verifyRole) {
    this.verifyRole = verifyRole;
    return this;
  }

  /**
   * 自动通过为
   * @return verifyRole
  **/


  public String getVerifyRole() {
    return verifyRole;
  }

  public void setVerifyRole(String verifyRole) {
    this.verifyRole = verifyRole;
  }

  public GroupVo description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
  **/


  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GroupVo groupVo = (GroupVo) o;
    return Objects.equals(this.id, groupVo.id) &&
        Objects.equals(this.name, groupVo.name) &&
        Objects.equals(this.verify, groupVo.verify) &&
        Objects.equals(this.verifyRole, groupVo.verifyRole) &&
        Objects.equals(this.description, groupVo.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, verify, verifyRole, description);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("{\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    verify: ").append(toIndentedString(verify)).append("\n");
    sb.append("    verifyRole: ").append(toIndentedString(verifyRole)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
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

