/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.34).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package io.swagger.api;

import io.swagger.model.AccountDTO;
import io.swagger.model.DepositDTO;
import io.swagger.model.NewAccountDTO;
import io.swagger.model.TransactionResponseBody;
import io.swagger.model.UpdateAccountDTO;
import io.swagger.model.WithdrawDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-15T15:18:25.887Z[GMT]")
@Validated
@CrossOrigin(origins = "*", allowedHeaders = "*")
public interface AccountsApi {

    @Operation(summary = "create account", description = "This call creates bank account", security = {
        @SecurityRequirement(name = "bearerAuth")    }, tags={ "Accounts" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "201", description = "created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountDTO.class))),
        
        @ApiResponse(responseCode = "401", description = "Access token is missing or invalid"),
        
        @ApiResponse(responseCode = "403", description = "Access is denied") })
    @RequestMapping(value = "/accounts",
        produces = { "application/json" }, 
        consumes = { "application/json" }, 
        method = RequestMethod.POST)
    ResponseEntity<AccountDTO> createAccount(@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody NewAccountDTO body);


    @Operation(summary = "delete bank account", description = "This call deletes bank account", security = {
        @SecurityRequirement(name = "bearerAuth")    }, tags={ "Accounts" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "OK"),
        
        @ApiResponse(responseCode = "400", description = "Bad input parameter"),
        
        @ApiResponse(responseCode = "401", description = "Access token is missing or invalid"),
        
        @ApiResponse(responseCode = "403", description = "Access is denied") })
    @RequestMapping(value = "/accounts/{IBAN}",
        method = RequestMethod.DELETE)
    ResponseEntity<Void> deleteAccounts(@Size(min=18,max=18) @Parameter(in = ParameterIn.PATH, description = "The IBAN", required=true, schema=@Schema()) @PathVariable("IBAN") String IBAN);


    @Operation(summary = "Add money to account", description = "This call add money to bank account", security = {
        @SecurityRequirement(name = "bearerAuth")    }, tags={ "Accounts" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "201", description = "created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionResponseBody.class))),
        
        @ApiResponse(responseCode = "400", description = "Bad input parameter"),
        
        @ApiResponse(responseCode = "401", description = "Access token is missing or invalid"),
        
        @ApiResponse(responseCode = "403", description = "Access is denied") })
    @RequestMapping(value = "/accounts/{IBAN}/deposit",
        produces = { "application/json" }, 
        consumes = { "application/json" }, 
        method = RequestMethod.POST)
    ResponseEntity<TransactionResponseBody> depositMoney(@Size(min=18,max=18) @Parameter(in = ParameterIn.PATH, description = "The IBAN", required=true, schema=@Schema()) @PathVariable("IBAN") String IBAN, @Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody DepositDTO body);


    @Operation(summary = "Provides bank account", description = "This call provides bank account", security = {
        @SecurityRequirement(name = "bearerAuth")    }, tags={ "Accounts" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountDTO.class))),
        
        @ApiResponse(responseCode = "400", description = "Bad input parameter"),
        
        @ApiResponse(responseCode = "401", description = "Access token is missing or invalid"),
        
        @ApiResponse(responseCode = "403", description = "Access is denied"),
        
        @ApiResponse(responseCode = "404", description = "Object does not exist.") })
    @RequestMapping(value = "/accounts/{IBAN}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<AccountDTO> getAccount(@Size(min=18,max=18) @Parameter(in = ParameterIn.PATH, description = "The IBAN", required=true, schema=@Schema()) @PathVariable("IBAN") String IBAN);


    @Operation(summary = "Provides bank accounts", description = "This call provides bank accounts", security = {
        @SecurityRequirement(name = "bearerAuth")    }, tags={ "Accounts" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AccountDTO.class)))),
        
        @ApiResponse(responseCode = "400", description = "Bad input parameter"),
        
        @ApiResponse(responseCode = "401", description = "Access token is missing or invalid"),
        
        @ApiResponse(responseCode = "403", description = "Access is denied") })
    @RequestMapping(value = "/accounts",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<AccountDTO>> getAccounts(@Parameter(in = ParameterIn.QUERY, description = "search for this substring" ,schema=@Schema()) @Valid @RequestParam(value = "name", required = false) String name, @Min(0)@Parameter(in = ParameterIn.QUERY, description = "number of records to skip for pagination" ,schema=@Schema(allowableValues={  }
)) @Valid @RequestParam(value = "skip", required = false) Integer skip, @Min(0) @Max(50) @Parameter(in = ParameterIn.QUERY, description = "maximum number of records to return" ,schema=@Schema(allowableValues={  }, maximum="50"
)) @Valid @RequestParam(value = "limit", required = false) Integer limit);


    @Operation(summary = "Update bank account", description = "This call update bank account", security = {
        @SecurityRequirement(name = "bearerAuth")    }, tags={ "Accounts" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountDTO.class))),
        
        @ApiResponse(responseCode = "400", description = "Bad input parameter"),
        
        @ApiResponse(responseCode = "401", description = "Access token is missing or invalid"),
        
        @ApiResponse(responseCode = "403", description = "Access is denied") })
    @RequestMapping(value = "/accounts/{IBAN}",
        produces = { "application/json" }, 
        consumes = { "application/json" }, 
        method = RequestMethod.PUT)
    ResponseEntity<AccountDTO> updateAccounts(@Size(min=18,max=18) @Parameter(in = ParameterIn.PATH, description = "The IBAN", required=true, schema=@Schema()) @PathVariable("IBAN") String IBAN, @Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody UpdateAccountDTO body);


    @Operation(summary = "withdraw money from account", description = "This call withdraw money from bank account", security = {
        @SecurityRequirement(name = "bearerAuth")    }, tags={ "Accounts" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "201", description = "created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionResponseBody.class))),
        
        @ApiResponse(responseCode = "400", description = "Bad input parameter"),
        
        @ApiResponse(responseCode = "401", description = "Access token is missing or invalid"),
        
        @ApiResponse(responseCode = "403", description = "Access is denied") })
    @RequestMapping(value = "/accounts/{IBAN}/withdraw",
        produces = { "application/json" }, 
        consumes = { "application/json" }, 
        method = RequestMethod.POST)
    ResponseEntity<TransactionResponseBody> withdrawMoney(@Size(min=18,max=18) @Parameter(in = ParameterIn.PATH, description = "The IBAN", required=true, schema=@Schema()) @PathVariable("IBAN") String IBAN, @Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody WithdrawDTO body);

}
