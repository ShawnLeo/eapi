package com.meimeitech.sys.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * EmailActivateReq
 */
@Validated

public class EmailActivateReq   {
  @JsonProperty("activateCode")
  private String activateCode = null;

  public EmailActivateReq activateCode(String activateCode) {
    this.activateCode = activateCode;
    return this;
  }

  /**
   * Get activateCode
   * @return activateCode
  **/


  public String getActivateCode() {
    return activateCode;
  }

  public void setActivateCode(String activateCode) {
    this.activateCode = activateCode;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EmailActivateReq emailActivateReq = (EmailActivateReq) o;
    return Objects.equals(this.activateCode, emailActivateReq.activateCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(activateCode);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("{\n");
    
    sb.append("    activateCode: ").append(toIndentedString(activateCode)).append("\n");
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

