package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.model.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;

import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * NewUserDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-15T15:18:25.887Z[GMT]")


public class NewUserDTO   {
  @JsonProperty("firstName")
  private String firstName = null;

  @JsonProperty("lastName")
  private String lastName = null;

  /**
   * Gets or Sets userType
   */
  public enum UserTypeEnum {
    CUSTOMER("customer"),

    EMPLOYEE("employee");

    private String value;

    UserTypeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static UserTypeEnum fromValue(String text) {
      for (UserTypeEnum b : UserTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("roles")
  @Valid
  @ElementCollection(fetch = FetchType.EAGER)
  private List<Role> roles = new ArrayList<Role>();

  @JsonProperty("email")
  private String email = null;

  @JsonProperty("password")
  private String password = null;

  @JsonProperty("address")
  private String address = null;

  @JsonProperty("phoneNumber")
  private String phoneNumber = null;

  public NewUserDTO firstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  /**
   * Get firstName
   * @return firstName
   **/
  @Schema(example = "Jessica", required = true, description = "")
      @NotNull

    public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public NewUserDTO lastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  /**
   * Get lastName
   * @return lastName
   **/
  @Schema(example = "Smith", required = true, description = "")
      @NotNull

    public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public NewUserDTO roles(List<Role> roles) {
    this.roles = roles;
    return this;
  }

  public NewUserDTO addRolesItem(Role rolesItem) {
    this.roles.add(rolesItem);
    return this;
  }

  /**
   * Get userType
   * @return userType
   **/
  @Schema(required = true, description = "")
      @NotNull

    public List<Role> getRoles() {
    return roles;
  }

  public void setRoles(List<Role> roles) {
    this.roles = roles;
  }

  public NewUserDTO email(String email) {
    this.email = email;
    return this;
  }

  /**
   * Get email
   * @return email
   **/
  @Schema(example = "user1@inholland.nl", required = true, description = "")
      @NotNull

    public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public NewUserDTO password(String password) {
    this.password = password;
    return this;
  }

  /**
   * Get password
   * @return password
   **/
  @Schema(example = "xxxxxxxxxxxxxxx", required = true, description = "")
      @NotNull

    public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public NewUserDTO address(String address) {
    this.address = address;
    return this;
  }

  /**
   * Get address
   * @return address
   **/
  @Schema(example = "haarlemstreet 1254 KH, Haarlem", required = true, description = "")
      @NotNull

    public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public NewUserDTO phoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
    return this;
  }

  /**
   * Get phoneNumber
   * @return phoneNumber
   **/
  @Schema(example = "12412347", required = true, description = "")
      @NotNull

    public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NewUserDTO newUserDTO = (NewUserDTO) o;
    return Objects.equals(this.firstName, newUserDTO.firstName) &&
        Objects.equals(this.lastName, newUserDTO.lastName) &&
        Objects.equals(this.roles, newUserDTO.roles) &&
        Objects.equals(this.email, newUserDTO.email) &&
        Objects.equals(this.password, newUserDTO.password) &&
        Objects.equals(this.address, newUserDTO.address) &&
        Objects.equals(this.phoneNumber, newUserDTO.phoneNumber);
  }

  @Override
  public int hashCode() {
    return Objects.hash(firstName, lastName, roles, email, password, address, phoneNumber);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NewUserDTO {\n");
    
    sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
    sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
    sb.append("    roles: ").append(toIndentedString(roles)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    phoneNumber: ").append(toIndentedString(phoneNumber)).append("\n");
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

  public NewUserDTO() {
  }

  public NewUserDTO(String firstName, String lastName, String email, String password, String address, String phoneNumber) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.password = password;
    this.address = address;
    this.phoneNumber = phoneNumber;
  }
}
