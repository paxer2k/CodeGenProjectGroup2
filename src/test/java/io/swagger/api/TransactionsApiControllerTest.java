package io.swagger.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.*;
import io.swagger.model.entity.Transaction;
import io.swagger.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class TransactionsApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private  TransactionsApiController transactionsApiController;
    private final ObjectMapper mapper = new ObjectMapper();

    private final List<Transaction> transactionList = new ArrayList<>();

    private static final String VALID_TOKEN_ADMIN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtdXNhdmlyQGluaG9sbGFuZC5ubCIsImF1dGgiOlt7ImF1dGhvcml0eSI6IlJPTEVfRU1QTE9ZRUUifV0sImlhdCI6MTY1NTgwNDUzMywiZXhwIjoxNjU1ODA4MTMzfQ.urkiIGG8OrZep1sK9i9jpeVEE8ScQyd8xo1626TwaHY";
    private String token;

    @Test
   // @WithMockUser(username = "james@inholland.nl", roles = { "EMPLOYEE" })
    @WithMockUser(roles = {"EMPLOYEE"})
    void getTransactions() throws Exception{
        String iban = "NL30INHO0065055626";

       // assertEquals(token.equals(VALID_TOKEN_ADMIN));
        when(transactionService.getTransactions(iban)).thenReturn(transactionList);
        this.mockMvc.perform(get("/transactions/" + iban).header("Authorization", "Bearer" + VALID_TOKEN_ADMIN))
                .andExpect(status().isOk());
//            .andExpect(jsonPath("$",hasSize(2)))
//                .andExpect(jsonPath("$[0].iban").value("NL30INHO0065055626"));
    }

    @BeforeEach
    public void transactions(){
        Transaction transaction = new Transaction();
        transaction.setAmount(10.0);
        transaction.setDate(LocalDateTime.now());
        transaction.setFromIBAN("NL30INHO0065055626");
        transaction.setToIBAN("NL30INHO0065055627");
        transaction.setId(1);
        transaction.setUserID(UUID.randomUUID());
        Transaction transaction1 = new Transaction();
        transaction1.setAmount(20.0);
        transaction1.setDate(LocalDateTime.now());
        transaction1.setFromIBAN("NL30INHO0065055626");
        transaction1.setToIBAN("NL30INHO0065055627");
        transaction1.setId(2);
        transaction1.setUserID(UUID.randomUUID());
        transactionList.add(transaction);
        transactionList.add(transaction1);
    }


    @Test
    @WithMockUser(username = "james@inholland.nl", roles = { "EMPLOYEE" })
    void transferMoney() throws Exception{
        // Execution
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        NewTransactionDTO newTransactionDTO=new NewTransactionDTO();

        this.mockMvc.perform(post("/transactions")
                        .content(mapper.writeValueAsString(newTransactionDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }
}