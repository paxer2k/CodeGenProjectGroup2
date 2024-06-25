package io.swagger.api;

import java.math.BigDecimal;

import io.swagger.Security.JwtTokenProvider;
import io.swagger.annotations.Api;
import io.swagger.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.entity.*;
import io.swagger.service.AccountService;
import io.swagger.service.TransactionService;
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
import org.hibernate.validator.internal.engine.messageinterpolation.parser.ELState;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-15T15:18:25.887Z[GMT]")
@RestController
@Api(tags = {"Transactions"})
public class TransactionsApiController implements TransactionsApi {

    private static final Logger log = LoggerFactory.getLogger(TransactionsApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;
    private String bankIBAN = "NL01INHO0000000001";

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserService userService;

    @org.springframework.beans.factory.annotation.Autowired
    public TransactionsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('CUSTOMER')")
    public ResponseEntity<List<TransactionDTOInner>> getTransactions(@Size(min = 18, max = 18) @Parameter(in = ParameterIn.PATH, description = "The IBAN", required = true, schema = @Schema()) @PathVariable("IBAN") String IBAN, @Parameter(in = ParameterIn.QUERY, description = "start date", schema = @Schema()) @Valid @RequestParam(value = "startDate", required = false) String startDate, @Parameter(in = ParameterIn.QUERY, description = "end date", schema = @Schema()) @Valid @RequestParam(value = "endDate", required = false) String endDate, @Size(min = 18, max = 18) @Parameter(in = ParameterIn.QUERY, description = "from IBAN", schema = @Schema()) @Valid @RequestParam(value = "fromIBAN", required = false) String fromIBAN, @Size(min = 18, max = 18) @Parameter(in = ParameterIn.QUERY, description = "to IBAN", schema = @Schema()) @Valid @RequestParam(value = "toIBAN", required = false) String toIBAN, @Parameter(in = ParameterIn.QUERY, description = "balance", schema = @Schema()) @Valid @RequestParam(value = "equalBalance", required = false) BigDecimal equalBalance, @Parameter(in = ParameterIn.QUERY, description = "more than specific balance", schema = @Schema()) @Valid @RequestParam(value = "moreBalance", required = false) BigDecimal moreBalance, @Parameter(in = ParameterIn.QUERY, description = "less than specific balance", schema = @Schema()) @Valid @RequestParam(value = "lessBalance", required = false) BigDecimal lessBalance) {

        User user = returnUserfromToken();
        Account account = accountService.getAccount(IBAN);

        // preventing from bank account to be accessed
        if (IBAN.equals(this.bankIBAN)){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "You are not allowed to access this account!");
        }
        //prevent customers from accessing other customer's account
        if (!user.getRoles().contains(Role.ROLE_EMPLOYEE)){
            if (account.getUser() != user){
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "This account does not belong to this user!");
            }
        }
        List<Transaction> transactions = new ArrayList<>();

        // transactions = transactionService.getTransactionsFiltered(IBAN,startDate,endDate,moreBalance,lessBalance,equalBalance,fromIBAN,toIBAN);

        if (startDate != null || endDate != null || lessBalance != null || moreBalance != null || equalBalance != null || fromIBAN!=null || toIBAN !=null) {
            if (fromIBAN != null) {
                transactions = transactionService.getTransactionsFiltered(fromIBAN, startDate, endDate, moreBalance,
                        lessBalance, equalBalance, IBAN);
            } else {
                transactions = transactionService.getTransactionsFiltered(IBAN, startDate, endDate, moreBalance,
                        lessBalance, equalBalance, toIBAN);
            }
        }
        else {
            transactions = transactionService.getTransactions(IBAN);
        }

        ModelMapper modelMapper = new ModelMapper();

        List<TransactionDTOInner> transactionDTOS = transactions.stream()
                .map(transaction -> modelMapper.map(transaction, TransactionDTOInner.class)).collect(Collectors.toList());

        return new ResponseEntity<List<TransactionDTOInner>>(transactionDTOS, HttpStatus.OK);

    }

    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('CUSTOMER')")
    public ResponseEntity<TransactionResponseBody> transferMoney(@Parameter(in = ParameterIn.DEFAULT, description = "", schema = @Schema()) @Valid @RequestBody NewTransactionDTO body) {

        // checking if fields are filled in
        if (body.getUserID() ==null || body.getFromIBAN() == "" || body.getToIBAN()=="" || body.getAmount() ==null){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Please fill out all the fields!");
        }

        Account fromAccount = accountService.getAccount(body.getFromIBAN());
        Account toAccount = accountService.getAccount(body.getToIBAN());
        Transaction transaction = new Transaction();

        User user = userService.getUser(UUID.fromString(body.getUserID()));

        // if the user is not an employee then they cannot access different account
        if (!user.getRoles().contains(Role.ROLE_EMPLOYEE)) {
            if (fromAccount.getUser().getUserId() != user.getUserId()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "This account does not belong to this user!");
            }
        }

        // prevent transferring from the bank account
        if (fromAccount.getIban().equals(this.bankIBAN)){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "You are not allowed to transfer from the bank's account!");
        }
        // check if from and to accounts are not the same
        if (fromAccount.getIban() != toAccount.getIban()) {
            // check if from and to users are the same
            if (fromAccount.getUser().getUserId().equals(toAccount.getUser().getUserId())) {
                // go to transactions service
                transaction = transactionService.transferMoney(body);

            } else {
                // prevent transfers from or to SAVINGS account of another customer
                if (fromAccount.getAccountType().equals(AccountType.SAVINGS)) {
                    throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Cannot transfer from saving account to a different user!");
                }
                else if (toAccount.getAccountType().equals(AccountType.SAVINGS)) {
                    throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Cannot transfer to saving account from a different user!");
                }
                else {
                    transaction = transactionService.transferMoney(body);
                }
            }
            TransactionResponseBody transactionResponseBody = new TransactionResponseBody();
            transactionResponseBody.setBalance(transaction.getAmount());
            transactionResponseBody.setIBAN(transaction.getFromIBAN());
            return new ResponseEntity<TransactionResponseBody>(transactionResponseBody, HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Cannot transfer money to the same account!");
        }

    }

    private User returnUserfromToken(){
        String jwtToken = jwtTokenProvider.resolveToken(request);
        if (!jwtTokenProvider.validateToken(jwtToken)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The token is invalid!");
        }
        return userService.getUserByEmail(jwtTokenProvider.getUsername(jwtToken));
    }

}
