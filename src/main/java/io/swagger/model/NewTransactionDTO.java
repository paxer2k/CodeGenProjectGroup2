package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * NewTransactionDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-15T15:18:25.887Z[GMT]")


public class NewTransactionDTO   {
  @JsonProperty("fromIBAN")
  private String fromIBAN = null;

  @JsonProperty("toIBAN")
  private String toIBAN = null;

  @JsonProperty("amount")
  private Double amount = null;

  @JsonProperty("userID")
  private String userID = null;

  @JsonProperty("pincode")
  private String pincode = null;

  public NewTransactionDTO fromIBAN(String fromIBAN) {
    this.fromIBAN = fromIBAN;
    return this;
  }

  public NewTransactionDTO() {
  }

  public NewTransactionDTO(String fromIBAN, String toIBAN, Double amount, String userID, String pincode) {
    this.fromIBAN = fromIBAN;
    this.toIBAN = toIBAN;
    this.amount = amount;
    this.userID = userID;
    this.pincode = pincode;
  }

  /**
   * Get fromIBAN
   * @return fromIBAN
   **/
  @Schema(example = "NL01INHO0000000001", required = true, description = "")
      @NotNull

  @Size(min=18,max=100)   public String getFromIBAN() {
    return fromIBAN;
  }

  public void setFromIBAN(String fromIBAN) {
    this.fromIBAN = fromIBAN;
  }

  public NewTransactionDTO toIBAN(String toIBAN) {
    this.toIBAN = toIBAN;
    return this;
  }

  /**
   * Get toIBAN
   * @return toIBAN
   **/
  @Schema(example = "NL01INHO0000000002", required = true, description = "")
      @NotNull

  @Size(min=18,max=100)   public String getToIBAN() {
    return toIBAN;
  }

  public void setToIBAN(String toIBAN) {
    this.toIBAN = toIBAN;
  }

  public NewTransactionDTO amount(Double amount) {
    this.amount = amount;
    return this;
  }

  /**
   * Get amount
   * @return amount
   **/
  @Schema(example = "0", required = true, description = "")
      @NotNull

    public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  public NewTransactionDTO userID(String userID) {
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

  public NewTransactionDTO pincode(String pincode) {
    this.pincode = pincode;
    return this;
  }

  /**
   * Get pincode
   * @return pincode
   **/
  @Schema(example = "xxxxxx", required = true, description = "")
      @NotNull

    public String getPincode() {
    return pincode;
  }

  public void setPincode(String pincode) {
    this.pincode = pincode;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NewTransactionDTO newTransactionDTO = (NewTransactionDTO) o;
    return Objects.equals(this.fromIBAN, newTransactionDTO.fromIBAN) &&
        Objects.equals(this.toIBAN, newTransactionDTO.toIBAN) &&
        Objects.equals(this.amount, newTransactionDTO.amount) &&
        Objects.equals(this.userID, newTransactionDTO.userID) &&
        Objects.equals(this.pincode, newTransactionDTO.pincode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fromIBAN, toIBAN, amount, userID, pincode);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NewTransactionDTO {\n");
    
    sb.append("    fromIBAN: ").append(toIndentedString(fromIBAN)).append("\n");
    sb.append("    toIBAN: ").append(toIndentedString(toIBAN)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    userID: ").append(toIndentedString(userID)).append("\n");
    sb.append("    pincode: ").append(toIndentedString(pincode)).append("\n");
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
