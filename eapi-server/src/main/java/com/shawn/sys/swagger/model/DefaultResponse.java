package com.shawn.sys.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;

/**
 * 默认响应
 */
@Validated

public class DefaultResponse   {
  @JsonProperty("header")
  private ResponseHeader header = null;

  @JsonProperty("body")
  private String body = null;

  public DefaultResponse header(ResponseHeader header) {
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

  public DefaultResponse body(String body) {
    this.body = body;
    return this;
  }

  /**
   * Get body
   * @return body
  **/


  public String getBody() {
    return body;
  }

  public void setBody(String body) {
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
    DefaultResponse defaultResponse = (DefaultResponse) o;
    return Objects.equals(this.header, defaultResponse.header) &&
        Objects.equals(this.body, defaultResponse.body);
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

