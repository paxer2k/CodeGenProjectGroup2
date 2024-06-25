package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * WithdrawDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-15T15:18:25.887Z[GMT]")


public class WithdrawDTO   {

  @JsonProperty("amount")
  private Double amount = null;

  @JsonProperty("pincode")
  private String pincode = null;

  public WithdrawDTO amount(Double amount) {
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

  public WithdrawDTO pincode(String pincode) {
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
    WithdrawDTO withdrawDTO = (WithdrawDTO) o;
    return Objects.equals(this.amount, withdrawDTO.amount) &&
        Objects.equals(this.pincode, withdrawDTO.pincode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(amount, pincode);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WithdrawDTO {\n");
    
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
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

  public WithdrawDTO(Double amount, String pincode) {
    this.amount = amount;
    this.pincode = pincode;
  }
}
