package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.Security.JwtTokenProvider;
import io.swagger.model.*;
import io.swagger.model.entity.*;

import io.swagger.service.AccountService;
import io.swagger.service.UserService;
import lombok.With;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import javax.print.attribute.standard.Media;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AccountsApiControllerTest {


    @MockBean
    UserService userService;

    @MockBean
    AccountService accountService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    private static final String VALID_TOKEN_ADMIN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtdXNhdmlyQGluaG9sbGFuZC5ubCIsImF1dGgiOlt7ImF1dGhvcml0eSI6IlJPTEVfRU1QTE9ZRUUifV0sImlhdCI6MTY1NTg0NTQ1MSwiZXhwIjoxNjU1ODQ5MDUxfQ.1GgSwFOmrdC6jk47EZcJsE8KwCsUDnAXoxYQtAfDP_8";
    private String token;



    List<Account> expectedAccounts;

    @BeforeEach
    public void setupData(){
//        List<Account> accounts = new ArrayList<>();
//
//        Account account1 = new Account();
//        account1.setAbsoluteLimit(0.0);
//        account1.setBalance(2000.0);
//        account1.setAccountStatus(AccountStatus.ACTIVE);
//        account1.setAccountType(AccountType.CURRENT);
//        account1.setIban("NL46INHO0962909260");
//        account1.setUser(null);
//
//        accounts.add(account1);
//
//        Account account2 = new Account();
//        account2.setAbsoluteLimit(0.0);
//        account2.setBalance(2000.0);
//        account2.setAccountType(AccountType.SAVINGS);
//        account2.setIban("NL19INHO1259637692");
//        account2.setUser(null);
//
//        accounts.add(account2);
//
//        User bob = new User();
//        List<Role> rolesForBob = new ArrayList<>();
//        rolesForBob.add(Role.ROLE_CUSTOMER);
//        bob.setUserId(UUID.fromString("ed466f56-5d32-4afe-8935-45948e51f498"));
//        bob.setFirstName("Bob");
//        bob.setLastName("Bobbly");
//        bob.setEmail("bob@example.com");
//        bob.setPhoneNumber("+31 6 12345678");
//        bob.setTransactionLimit(500.0);
//        bob.setDayLimit(1000.0);
//        bob.setPassword("test1235");
//        bob.setRoles(rolesForBob);
//
        User john = new User();
        List<Role> rolesForJohn = new ArrayList<>();
        rolesForJohn.add(Role.ROLE_EMPLOYEE);
        john.setUserId(UUID.fromString("0cf35021-5d4f-444c-a278-601e85afde25"));
        john.setFirstName("John");
        john.setLastName("Johnny");
        john.setEmail("john@example.com");
        john.setPhoneNumber("+31 6 12345321");
        john.setTransactionLimit(500.0);
        john.setDayLimit(1000.0);
        john.setPassword("test1235");
        john.setRoles(rolesForJohn);
//
//        expectedAccounts = accounts;
//
//        given(userService.getUserByEmail("bob@example.com")).willReturn(bob);
//        given(userService.getUserByEmail("john@example.com")).willReturn(john);
    }





    @Test
    @WithMockUser(username = "musavir@inholland.nl", password = "test12345", roles = {"EMPLOYEE"})
    public void getAllAccounts() throws Exception {
        this.mockMvc.perform(get("/accounts"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "musavir@inholland.nl", password = "test12345", roles = {"EMPLOYEE"})
    public void getAccountByIBAN() throws Exception {
        this.mockMvc.perform(get("/accounts/NL78INHO0404565238").header("Authorization", "Bearer " + VALID_TOKEN_ADMIN))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "musavir@inholland.nl", password = "test12345", roles = {"EMPLOYEE"})
    public void makeWithdrawalWithEmployee() throws Exception {
        WithdrawDTO withdrawDTO = new WithdrawDTO(20.0, "xxxx");

        this.mockMvc.perform(post("/accounts/NL78INHO0404565238/withdraw").header("Authorization", "Bearer " + VALID_TOKEN_ADMIN)
                .content(mapper.writeValueAsString(withdrawDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "musavir@inholland.nl", roles = { "EMPLOYEE" })
    void createAccount() throws Exception{

        this.mockMvc.perform(post("/accounts")
                        .content(mapper.writeValueAsString(newAccountDTO()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }








//    @Test
//    @WithMockUser(username = "user1", password = "test12345", roles = {"EMPLOYEE"})
//    public void createAccount() throws Exception {
//        Account account = new Account();
//        User john = new User();
//        List<Role> rolesForJohn = new ArrayList<>();
//        rolesForJohn.add(Role.ROLE_EMPLOYEE);
//        john.setUserId(UUID.fromString("0cf35021-5d4f-444c-a278-601e85afde25"));
//        john.setFirstName("John");
//        john.setLastName("Johnny");
//        john.setEmail("john@example.com");
//        john.setPhoneNumber("+31 6 12345321");
//        john.setTransactionLimit(500.0);
//        john.setDayLimit(1000.0);
//        john.setPassword("test1235");
//        john.setRoles(rolesForJohn);
//
//        account.setUser(john);
//        account.setIban("NL17INHO0945830410");
//        account.setAccountType(AccountType.CURRENT);
//        account.setAccountStatus(AccountStatus.ACTIVE);
//        account.setBalance(2000.0);
//        account.setAbsoluteLimit(0.0);
//
//        when(accountService.createAccount(newAccountDTO())).thenReturn(account);
//        this.mockMvc.perform(post("/accounts")
//                .content(mapper.writeValueAsString(account))
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated());
//    }





    public NewAccountDTO newAccountDTO(){
        NewAccountDTO account = new NewAccountDTO();
        account.setUserID("0cf35021-5d4f-444c-a278-601e85afde25");
        account.setAccountType(NewAccountDTO.AccountTypeEnum.CURRENT);

        return  account;
    }

    public DepositDTO depositDTO() {
        DepositDTO deposit = new DepositDTO(20.0, "xxxx");
        return deposit;
    }

    public WithdrawDTO withdrawDTO() {
        WithdrawDTO withdraw = new WithdrawDTO(10.0, "xxxx");
        return withdraw;
    }

    public UpdateAccountDTO updateAccountDTO() {
        UpdateAccountDTO account = new UpdateAccountDTO();
        account.setAccountStatus(UpdateAccountDTO.AccountStatusEnum.INACTIVE);
        account.setAccountType(UpdateAccountDTO.AccountTypeEnum.SAVINGS);
        account.setAbsoluteLimit(BigDecimal.valueOf(10));

        return account;
    }

//    @Test
//    @WithMockUser(roles = {"EMPLOYEE"})
//    void getAllAccounts() throws Exception {
//        when(accountService.getAccounts()).thenReturn(accountList);
//        mockMvc.perform(get("/accounts"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$",hasSize(2)))
//                .andExpect(jsonPath("$[0].userID").value(createdUser.getUserId()));
//    }
//
//    void getOneAccount() throws Exception {
//        when(accountService.getAccount(createdAccount.getIban())).thenReturn(createdAccount);
//        mockMvc.perform(get("/accounts/" + createdAccount.getIban()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$",hasSize(1)))
//                .andExpect(jsonPath("$[0].userID").value(createdUser.getUserId()));
//    }
//
//    @Test
//    void createAccount() throws Exception {
//        mockMvc.perform(post("/accounts")
//                        .content(mapper.writeValueAsString(newAccountDTO()))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                        .andExpect(status().isCreated());
//    }
//
//    @Test
//    void depositMoney() throws Exception {
//        mockMvc.perform(post("/accounts/" + createdAccount.getIban() + "/deposit")
//                .content(mapper.writeValueAsString(depositDTO()))
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated());
//    }
//
//    @Test
//    void withdrawMoney() throws Exception {
//        mockMvc.perform(post("/accounts/" + createdAccount.getIban() + "/withdraw")
//                        .content(mapper.writeValueAsString(withdrawDTO()))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                        .andExpect(status().isCreated());
//    }
//
//    @Test
//    void updateAccounts() throws Exception {
//        this.mockMvc.perform(put("/accounts/" + createdAccount.getIban())
//                        .content(mapper.writeValueAsString(updateAccountDTO()))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                        .andExpect(status().isCreated());
//    }





}