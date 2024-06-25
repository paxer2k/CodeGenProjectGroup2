package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.model.entity.Account;
import io.swagger.model.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;

import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * UserDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-15T15:18:25.887Z[GMT]")


public class UserDTO   {
  @JsonProperty("userID")
  private String userID = null;

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

  private List<Account> accounts = null;

  public List<Account> getAccounts() {
    return accounts;
  }

  public void setAccounts(List<Account> accounts) {
    this.accounts = accounts;
  }

  @JsonProperty("address")
  private String address = null;

  @JsonProperty("phoneNumber")
  private String phoneNumber = null;

  @JsonProperty("dayLimit")
  private Integer dayLimit = null;

  private Integer currentDayLimit = null;

  public Integer getCurrentDayLimit() {
    return currentDayLimit;
  }

  public void setCurrentDayLimit(Integer currentDayLimit) {
    this.currentDayLimit = currentDayLimit;
  }

  @JsonProperty("transactionLimit")
  private BigDecimal transactionLimit = null;

  public UserDTO userID(String userID) {
    this.userID = userID;
    return this;
  }

  /**
   * Get userID
   * @return userID
   **/
  @Schema(example = "58e007480aac31001185ecef", required = true, description = "")
      @NotNull

    public String getUserID() {
    return userID;
  }

  public void setUserID(String userID) {
    this.userID = userID;
  }

  public UserDTO firstName(String firstName) {
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

  public UserDTO lastName(String lastName) {
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

  public UserDTO roles(List<Role> roles) {
    this.roles = roles;
    return this;
  }

  public UserDTO addRolesItem(Role rolesItem) {
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

  public UserDTO email(String email) {
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

  public UserDTO address(String address) {
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

  public UserDTO phoneNumber(String phoneNumber) {
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

  public UserDTO dayLimit(Integer dayLimit) {
    this.dayLimit = dayLimit;
    return this;
  }

  /**
   * Get dayLimit
   * @return dayLimit
   **/
  @Schema(example = "20", required = true, description = "")
      @NotNull

    public Integer getDayLimit() {
    return dayLimit;
  }

  public void setDayLimit(Integer dayLimit) {
    this.dayLimit = dayLimit;
  }

  public UserDTO transactionLimit(BigDecimal transactionLimit) {
    this.transactionLimit = transactionLimit;
    return this;
  }

  /**
   * Get transactionLimit
   * @return transactionLimit
   **/
  @Schema(example = "1000", required = true, description = "")
      @NotNull

    @Valid
    public BigDecimal getTransactionLimit() {
    return transactionLimit;
  }

  public void setTransactionLimit(BigDecimal transactionLimit) {
    this.transactionLimit = transactionLimit;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserDTO userDTO = (UserDTO) o;
    return Objects.equals(this.userID, userDTO.userID) &&
        Objects.equals(this.firstName, userDTO.firstName) &&
        Objects.equals(this.lastName, userDTO.lastName) &&
        Objects.equals(this.roles, userDTO.roles) &&
        Objects.equals(this.email, userDTO.email) &&
        Objects.equals(this.address, userDTO.address) &&
        Objects.equals(this.phoneNumber, userDTO.phoneNumber) &&
        Objects.equals(this.dayLimit, userDTO.dayLimit) &&
            Objects.equals(this.currentDayLimit, userDTO.currentDayLimit) &&
        Objects.equals(this.transactionLimit, userDTO.transactionLimit);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userID, firstName, lastName, roles, email, address, phoneNumber, dayLimit,currentDayLimit, transactionLimit);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserDTO {\n");
    
    sb.append("    userID: ").append(toIndentedString(userID)).append("\n");
    sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
    sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
    sb.append("    roles: ").append(toIndentedString(roles)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    phoneNumber: ").append(toIndentedString(phoneNumber)).append("\n");
    sb.append("    dayLimit: ").append(toIndentedString(dayLimit)).append("\n");
    sb.append("    currentDayLimit: ").append(toIndentedString(currentDayLimit)).append("\n");
    sb.append("    transactionLimit: ").append(toIndentedString(transactionLimit)).append("\n");
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
