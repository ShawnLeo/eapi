package com.shawn.sys.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;

/**
 * 用户信息响应体
 */
@Validated

public class UserInfoResponse   {
  @JsonProperty("header")
  private ResponseHeader header = null;

  @JsonProperty("body")
  private UserVO body = null;

  public UserInfoResponse header(ResponseHeader header) {
    this.header = header;
    return this;
  }

  /**
   * Get header
   * @return header
  **/

  @Valid

  public ResponseHeader getHeader() {
    return header;
  }

  public void setHeader(ResponseHeader header) {
    this.header = header;
  }

  public UserInfoResponse body(UserVO body) {
    this.body = body;
    return this;
  }

  /**
   * Get body
   * @return body
  **/

  @Valid

  public UserVO getBody() {
    return body;
  }

  public void setBody(UserVO body) {
    this.body = body;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserInfoResponse userInfoResponse = (UserInfoResponse) o;
    return Objects.equals(this.header, userInfoResponse.header) &&
        Objects.equals(this.body, userInfoResponse.body);
  }

  @Override
  public int hashCode() {
    return Objects.hash(header, body);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("{\n");
    
    sb.append("    header: ").append(toIndentedString(header)).append("\n");
    sb.append("    body: ").append(toIndentedString(body)).append("\n");
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

