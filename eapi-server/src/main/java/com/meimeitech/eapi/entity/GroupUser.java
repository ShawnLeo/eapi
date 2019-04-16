package com.meimeitech.eapi.entity;

import com.meimeitech.eapi.dialect.AbstractGroupUser;
import com.meimeitech.eapi.dialect.Schema;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 项目组成员
 */
@Entity
@Table(name = Schema.Tables.GROUP_USER)
public class GroupUser extends AbstractGroupUser {
  /**
   * 用户名称
   */
  @Column(name = "USER_NAME",length = 40)
  private String userName;

  /**
   * 用户角色
   */
  @Column(name = "ROLE",length = 40)
  private String role;

  /**
   * 项目组ID
   */
  @Column(name = "GROUPID",length = 40)
  private String groupId;

  /**
   * 用户Id
   */
  @Column(name = "USERID",length = 40)
  private String userId;

  /**
   * 加入时间
   */
  @Column(name = "JOIN_TIME",length = 40)
  private Date joinTime;

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getGroupId() {
    return groupId;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public Date getJoinTime() {
    return joinTime;
  }

  public void setJoinTime(Date joinTime) {
    this.joinTime = joinTime;
  }
}

