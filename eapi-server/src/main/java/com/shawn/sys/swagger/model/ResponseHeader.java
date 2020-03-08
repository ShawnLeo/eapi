package com.shawn.sys.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

/**
 * 响应头
 */
@Validated

public class ResponseHeader   {
  @JsonProperty("code")
  private String code = null;

  @JsonProperty("message")
  private String message = null;

  @JsonProperty("desc")
  private String desc = null;

  @JsonProperty("timestamp")
  private String timestamp = null;

  @JsonProperty("repeat")
  private Integer repeat = null;

  @JsonProperty("signature")
  private String signature = null;

  public ResponseHeader code(String code) {
    this.code = code;
    return this;
  }

  /**
   * Get code
   * @return code
  **/


  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public ResponseHeader message(String message) {
    this.message = message;
    return this;
  }

  /**
   * Get message
   * @return message
  **/


  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public ResponseHeader desc(String desc) {
    this.desc = desc;
    return this;
  }

  /**
   * Get desc
   * @return desc
  **/


  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public ResponseHeader timestamp(String timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
  **/


  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public ResponseHeader repeat(Integer repeat) {
    this.repeat = repeat;
    return this;
  }

  /**
   * Get repeat
   * @return repeat
  **/


  public Integer getRepeat() {
    return repeat;
  }

  public void setRepeat(Integer repeat) {
    this.repeat = repeat;
  }

  public ResponseHeader signature(String signature) {
    this.signature = signature;
    return this;
  }

  /**
   * Get signature
   * @return signature
  **/


  public String getSignature() {
    return signature;
  }

  public void setSignature(String signature) {
    this.signature = signature;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResponseHeader responseHeader = (ResponseHeader) o;
    return Objects.equals(this.code, responseHeader.code) &&
        Objects.equals(this.message, responseHeader.message) &&
        Objects.equals(this.desc, responseHeader.desc) &&
        Objects.equals(this.timestamp, responseHeader.timestamp) &&
        Objects.equals(this.repeat, responseHeader.repeat) &&
        Objects.equals(this.signature, responseHeader.signature);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, message, desc, timestamp, repeat, signature);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("{\n");
    
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    desc: ").append(toIndentedString(desc)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    repeat: ").append(toIndentedString(repeat)).append("\n");
    sb.append("    signature: ").append(toIndentedString(signature)).append("\n");
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

