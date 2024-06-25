package io.swagger.api;

import io.swagger.Security.JwtTokenProvider;
import io.swagger.annotations.Api;
import io.swagger.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.entity.*;
import io.swagger.service.AccountService;
import io.swagger.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.aspectj.weaver.ast.ITestVisitor;
import org.aspectj.weaver.ast.Test;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-15T15:18:25.887Z[GMT]")
@RestController
@Api(tags = {"Accounts"})
public class AccountsApiController implements AccountsApi {

    private static final Logger log = LoggerFactory.getLogger(AccountsApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private AccountService accountService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserService userService;
    private String bankIBAN = "NL01INHO0000000001";

    @org.springframework.beans.factory.annotation.Autowired
    public AccountsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }


    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<AccountDTO> createAccount(@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody NewAccountDTO body) {
        ModelMapper modelMapper = new ModelMapper();

        // make sure all fields are filled in
        if (body.getUserID() == "" || body.getAccountType() == null){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Please fill out all the fields!");
        }
        Account account = accountService.createAccount(body);

        AccountDTO accountResponse = modelMapper.map(account, AccountDTO.class);

        return new ResponseEntity<AccountDTO>(accountResponse, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Void> deleteAccounts(@Size(min=18,max=18) @Parameter(in = ParameterIn.PATH, description = "The IBAN", required=true, schema=@Schema()) @PathVariable("IBAN") String IBAN) {
        accountService.deleteAccount(IBAN);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('CUSTOMER')")
    public ResponseEntity<TransactionResponseBody> depositMoney(@Size(min=18,max=18) @Parameter(in = ParameterIn.PATH, description = "The IBAN", required=true, schema=@Schema()) @PathVariable("IBAN") String IBAN,@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody DepositDTO body) {

        User loggedUser = returnUserfromToken();
        Account userAccount = accountService.getAccount(IBAN);

        // if the logged in user is not an employee and the logged in user does not belong to the account, throw exception
        if (!loggedUser.getRoles().contains(Role.ROLE_EMPLOYEE)){
            if (loggedUser.getUserId() != userAccount.getUser().getUserId()){
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "This account does not belong to this user!");
            }
        }
        // prevent transferring from the bank account
        if (IBAN.equals(this.bankIBAN)){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "You are not allowed to transfer from the bank's account!");
        }

        if (body.getAmount() <= 0){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Amount cannot be 0 or less than 0");
        }

        Account account = accountService.deposit(body.getAmount(),IBAN,body.getPincode());

        TransactionResponseBody transactionResponseBody = new TransactionResponseBody();
        transactionResponseBody.setBalance(account.getBalance());
        transactionResponseBody.setIBAN(account.getIban());

        return new ResponseEntity<TransactionResponseBody>(transactionResponseBody, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('CUSTOMER')")
    public ResponseEntity<AccountDTO> getAccount(@Size(min=18,max=18) @Parameter(in = ParameterIn.PATH, description = "The IBAN", required=true, schema=@Schema()) @PathVariable("IBAN") String IBAN) {
        User loggedUser = returnUserfromToken();
        Account userAccount = accountService.getAccount(IBAN);

        // if the logged in user is not an employee and the logged in user does not belong to the account, throw exception
        if (!loggedUser.getRoles().contains(Role.ROLE_EMPLOYEE)){
            if (loggedUser.getUserId() != userAccount.getUser().getUserId()){
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "This account does not belong to this user!");
            }
        }

        if (IBAN.equals(this.bankIBAN)){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "You are not allowed to access the bank's account!");
        }
        Account account = accountService.getAccount(IBAN);

        ModelMapper modelMapper = new ModelMapper();

        AccountDTO accountResponse = modelMapper.map(account, AccountDTO.class);

        return new ResponseEntity<AccountDTO>(accountResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<List<AccountDTO>> getAccounts(@Parameter(in = ParameterIn.QUERY, description = "search for this substring" ,schema=@Schema()) @Valid @RequestParam(value = "name", required = false) String name,@Min(0)@Parameter(in = ParameterIn.QUERY, description = "number of records to skip for pagination" ,schema=@Schema(allowableValues={  }
    )) @Valid @RequestParam(value = "skip", required = false) Integer skip,@Min(0) @Max(50) @Parameter(in = ParameterIn.QUERY, description = "maximum number of records to return" ,schema=@Schema(allowableValues={  }, maximum="50"
    )) @Valid @RequestParam(value = "limit", required = false) Integer limit) {


        List<Account> accounts = accountService.getAccounts();
        List<AccountDTO> accountDTOS = new ArrayList<>();

        ModelMapper modelMapper = new ModelMapper();

        // query filters and checks
        if (skip!=null && limit !=null && name !=null){
            // search should be by user name??
            accountDTOS = accounts.stream()
                    .map(account -> modelMapper.map(account, AccountDTO.class)).skip(skip).limit(limit).filter(account ->account.getIBAN().contains(name)).collect(Collectors.toList());
        }
        else if (skip!=null && limit !=null){
            accountDTOS = accounts.stream()
                    .map(account -> modelMapper.map(account, AccountDTO.class)).skip(skip).limit(limit).collect(Collectors.toList());
        }
        else {
            accountDTOS = accounts.stream()
                    .map(account -> modelMapper.map(account, AccountDTO.class)).collect(Collectors.toList());
        }

        // if the iban contains bank iban, then do not send it
        accountDTOS.removeIf(accountDto -> accountDto.getIBAN().equals("NL01INHO0000000001"));
        return new ResponseEntity<List<AccountDTO>>(accountDTOS, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<AccountDTO> updateAccounts(@Size(min=18,max=18) @Parameter(in = ParameterIn.PATH, description = "The IBAN", required=true, schema=@Schema()) @PathVariable("IBAN") String IBAN,@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody UpdateAccountDTO body) {

        ModelMapper modelMapper = new ModelMapper();


        User loggedUser = returnUserfromToken();
        Account userAccount = accountService.getAccount(IBAN);

        if (!loggedUser.getRoles().contains(Role.ROLE_EMPLOYEE)){
            if (loggedUser.getUserId() != userAccount.getUser().getUserId()){
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "This account does not belong to this user!");
            }
        }

        // prevent updating the bank's bank account
        if (IBAN.equals(this.bankIBAN)){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "You are not allowed to access the bank's accounts");
        }

        if (IBAN.equals(null) || body.getAccountType().equals(null) || body.getAccountStatus().equals(null) || body.getAbsoluteLimit().equals(null)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Please fill out all the fields!");
        }

        Account account = modelMapper.map(body, Account.class);
        Account oldAccount = accountService.getAccount(IBAN);
        account.setUser(oldAccount.getUser());
        account.setBalance(oldAccount.getBalance());
        account.setIban(IBAN);
        account = accountService.updateAccount(account);

        AccountDTO accountResponse = modelMapper.map(account, AccountDTO.class);

        return new ResponseEntity<AccountDTO>(accountResponse, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('CUSTOMER')")
    public ResponseEntity<TransactionResponseBody> withdrawMoney(@Size(min=18,max=18) @Parameter(in = ParameterIn.PATH, description = "The IBAN", required=true, schema=@Schema()) @PathVariable("IBAN") String IBAN,@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody WithdrawDTO body) {

        ModelMapper modelMapper = new ModelMapper();

        User loggedUser = returnUserfromToken();
        Account userAccount = accountService.getAccount(IBAN);

        if (!loggedUser.getRoles().contains(Role.ROLE_EMPLOYEE)){
            if (loggedUser.getUserId() != userAccount.getUser().getUserId()){
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "This account does not belong to this user!");
            }
        }
        if (IBAN.equals(this.bankIBAN)){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "You are not allowed to access the bank's accounts");
        }
        if (body.getAmount() <= 0){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Cannot withdraw an amount of 0 and less");
        }
        Account account=accountService.withdraw(body.getAmount(), IBAN,body.getPincode(),"withdraw");

        TransactionResponseBody transactionResponseBody = new TransactionResponseBody();
        transactionResponseBody.setBalance(account.getBalance());
        transactionResponseBody.setIBAN(account.getIban());

        return new ResponseEntity<TransactionResponseBody>(transactionResponseBody, HttpStatus.CREATED);
    }

    private User returnUserfromToken(){
        String jwtToken = jwtTokenProvider.resolveToken(request);
        if (!jwtTokenProvider.validateToken(jwtToken)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The token is invalid");
        }
        return userService.getUserByEmail(jwtTokenProvider.getUsername(jwtToken));
    }

}
