package com.meimeitech.eapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * GroupUserReq
 */
@Validated

public class GroupUserReq {
  @JsonProperty("groupId")
  private String groupId = null;

  @JsonProperty("role")
  private String role = null;

  @JsonProperty("users")
  @Valid
  private List<String> users = null;

  public GroupUserReq groupId(String groupId) {
    this.groupId = groupId;
    return this;
  }

  /**
   * 用户组Id
   * @return groupId
  **/


  public String getGroupId() {
    return groupId;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  public GroupUserReq role(String role) {
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

  public GroupUserReq users(List<String> users) {
    this.users = users;
    return this;
  }

  public GroupUserReq addUsersItem(String usersItem) {
    if (this.users == null) {
      this.users = new ArrayList<String>();
    }
    this.users.add(usersItem);
    return this;
  }

  /**
   * Get users
   * @return users
  **/


  public List<String> getUsers() {
    return users;
  }

  public void setUsers(List<String> users) {
    this.users = users;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GroupUserReq groupUserReq = (GroupUserReq) o;
    return Objects.equals(this.groupId, groupUserReq.groupId) &&
        Objects.equals(this.role, groupUserReq.role) &&
        Objects.equals(this.users, groupUserReq.users);
  }

  @Override
  public int hashCode() {
    return Objects.hash(groupId, role, users);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("{\n");

    sb.append("    groupId: ").append(toIndentedString(groupId)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    users: ").append(toIndentedString(users)).append("\n");
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

