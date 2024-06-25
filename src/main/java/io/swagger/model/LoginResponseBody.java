package io.swagger.model;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.model.entity.Role;
import io.swagger.model.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import org.threeten.bp.OffsetDateTime;
import org.springframework.validation.annotation.Validated;
import org.w3c.dom.stylesheets.LinkStyle;

import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * LoginResponseBody
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-15T15:18:25.887Z[GMT]")


public class LoginResponseBody   {
  @JsonProperty("JWTtoken")
  private String jwTtoken = null;

  @JsonProperty("token_type")
  private String tokenType = null;

  @JsonProperty("userID")
  private String userID = null;

  @JsonProperty("roles")
  private List<Role> roles = null;

  public List<Role> getRoles() {
    return roles;
  }

  public void setRoles(List<Role> roles) {
    this.roles = roles;
  }

  public String getUserID() {
    return userID;
  }

  public void setUserID(String userID) {
    this.userID = userID;
  }

  @JsonProperty("expires_at")
  private Date expiresAt = null;

  public LoginResponseBody jwTtoken(String jwTtoken) {
    this.jwTtoken = jwTtoken;
    return this;
  }

  /**
   * Get jwTtoken
   * @return jwTtoken
   **/
  @Schema(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c", description = "")
  
    public String getJwTtoken() {
    return jwTtoken;
  }

  public void setJwTtoken(String jwTtoken) {
    this.jwTtoken = jwTtoken;
  }

  public LoginResponseBody tokenType(String tokenType) {
    this.tokenType = tokenType;
    return this;
  }

  /**
   * Get tokenType
   * @return tokenType
   **/
  @Schema(example = "Bearer", description = "")
  
    public String getTokenType() {
    return tokenType;
  }

  public void setTokenType(String tokenType) {
    this.tokenType = tokenType;
  }

  public LoginResponseBody expiresAt(Date expiresAt) {
    this.expiresAt = expiresAt;
    return this;
  }

  /**
   * Get expiresAt
   * @return expiresAt
   **/
  @Schema(description = "")
  
    @Valid
    public Date getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(Date expiresAt) {
    this.expiresAt = expiresAt;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LoginResponseBody loginResponseBody = (LoginResponseBody) o;
    return Objects.equals(this.jwTtoken, loginResponseBody.jwTtoken) &&
        Objects.equals(this.tokenType, loginResponseBody.tokenType) &&
        Objects.equals(this.expiresAt, loginResponseBody.expiresAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(jwTtoken, tokenType, expiresAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LoginResponseBody {\n");
    
    sb.append("    jwTtoken: ").append(toIndentedString(jwTtoken)).append("\n");
    sb.append("    tokenType: ").append(toIndentedString(tokenType)).append("\n");
    sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
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
