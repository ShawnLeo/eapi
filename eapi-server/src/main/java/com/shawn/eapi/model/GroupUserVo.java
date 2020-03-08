package com.shawn.eapi.model;

import java.util.Date;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

/**
 * 项目组成员
 */
@Validated

public class GroupUserVo {
  @JsonProperty("id")
  private Integer id = null;

  @JsonProperty("userName")
  private String userName = null;

  @JsonProperty("role")
  private String role = null;

  @JsonProperty("joinTime")
  private Date joinTime = null;

  @JsonProperty("groupId")
  private String groupId = null;

  @JsonProperty("userId")
  private String userId = null;

  public GroupUserVo id(Integer id) {
    this.id = id;
    return this;
  }

  /**
   * id
   * @return id
  **/


  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public GroupUserVo userName(String userName) {
    this.userName = userName;
    return this;
  }

  /**
   * Get userName
   * @return userName
  **/


  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public GroupUserVo role(String role) {
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

  public GroupUserVo joinTime(Date joinTime) {
    this.joinTime = joinTime;
    return this;
  }

  /**
   * Get joinTime
   * @return joinTime
  **/


  public Date getJoinTime() {
    return joinTime;
  }

  public void setJoinTime(Date joinTime) {
    this.joinTime = joinTime;
  }

  public GroupUserVo groupId(String groupId) {
    this.groupId = groupId;
    return this;
  }

  /**
   * Get groupId
   * @return groupId
  **/


  public String getGroupId() {
    return groupId;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  public GroupUserVo userId(String userId) {
    this.userId = userId;
    return this;
  }

  /**
   * Get userId
   * @return userId
  **/


  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GroupUserVo groupUserVo = (GroupUserVo) o;
    return Objects.equals(this.id, groupUserVo.id) &&
        Objects.equals(this.userName, groupUserVo.userName) &&
        Objects.equals(this.role, groupUserVo.role) &&
        Objects.equals(this.joinTime, groupUserVo.joinTime) &&
        Objects.equals(this.groupId, groupUserVo.groupId) &&
        Objects.equals(this.userId, groupUserVo.userId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, userName, role, joinTime, groupId, userId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("{\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    userName: ").append(toIndentedString(userName)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    joinTime: ").append(toIndentedString(joinTime)).append("\n");
    sb.append("    groupId: ").append(toIndentedString(groupId)).append("\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
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

