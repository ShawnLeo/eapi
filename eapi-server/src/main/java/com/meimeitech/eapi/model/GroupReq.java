package com.meimeitech.eapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * GroupReq
 */
@Validated

public class GroupReq   {
  @JsonProperty("name")
  private String name = null;

  @JsonProperty("description")
  private String description = null;

  public GroupReq name(String name) {
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

  public GroupReq description(String description) {
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
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GroupReq groupReq = (GroupReq) o;
    return Objects.equals(this.name, groupReq.name) &&
        Objects.equals(this.description, groupReq.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, description);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("{\n");

    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
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

