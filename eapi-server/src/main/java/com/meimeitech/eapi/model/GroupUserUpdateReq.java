package com.meimeitech.eapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * GroupUserUpdateReq
 */
@Validated

public class GroupUserUpdateReq   {
  @JsonProperty("groupUserId")
  private String groupUserId = null;

  @JsonProperty("role")
  private String role = null;

  public GroupUserUpdateReq groupUserId(String groupUserId) {
    this.groupUserId = groupUserId;
    return this;
  }

  /**
   * 用户组Id
   * @return groupUserId
  **/


  public String getGroupUserId() {
    return groupUserId;
  }

  public void setGroupUserId(String groupUserId) {
    this.groupUserId = groupUserId;
  }

  public GroupUserUpdateReq role(String role) {
    this.role = role;
    return this;
  }

  /**
   * Get role
   * @return role
  **/


  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GroupUserUpdateReq groupUserUpdateReq = (GroupUserUpdateReq) o;
    return Objects.equals(this.groupUserId, groupUserUpdateReq.groupUserId) &&
        Objects.equals(this.role, groupUserUpdateReq.role);
  }

  @Override
  public int hashCode() {
    return Objects.hash(groupUserId, role);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("{\n");
    
    sb.append("    groupUserId: ").append(toIndentedString(groupUserId)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

