package io.swagger.api.steps.accounts;

import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java8.De;
import io.cucumber.java8.En;
import io.swagger.api.steps.BaseStepDefinitions;
import io.swagger.model.DepositDTO;
import io.swagger.model.NewAccountDTO;
import io.swagger.model.NewTransactionDTO;
import io.swagger.model.WithdrawDTO;
import io.swagger.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@Slf4j
public class AccountStepsDefinitions extends BaseStepDefinitions implements En {

   // private static final String VALID_TOKEN_USER = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbXJpc2giLCJhdXRoIjpbeyJhdXRob3JpdHkiOiJST0xFX1VTRVIifV0sImlhdCI6MTY1NTY2NjM4NCwiZXhwIjoxNjU1NjY5OTg0fQ.7C7I2xMVVxDvixMJY0s8b3UqyXCAT-WYgDZ1kDkJOUM";
    private static final String VALID_TOKEN_USER = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbGV4QGluaG9sbGFuZC5ubCIsImF1dGgiOlt7ImF1dGhvcml0eSI6IlJPTEVfQ1VTVE9NRVIifV0sImlhdCI6MTY1NTg0ODAwNCwiZXhwIjoxNjU1ODUxNjA0fQ.n79rWbmIOLn8qF9S8FVpNylj_CRGKSCe9YrcNpNGgOQ";

    private static final String VALID_TOKEN_ADMIN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtdXNhdmlyQGluaG9sbGFuZC5ubCIsImF1dGgiOlt7ImF1dGhvcml0eSI6IlJPTEVfRU1QTE9ZRUUifV0sImlhdCI6MTY1NTg5MDc5MywiZXhwIjoxNjU1ODk0MzkzfQ.r3JCil_eOXXZX2-TU_1_65EbZ_v9bmI2t-gSXgXvo48";

   //private static final String VALID_TOKEN_ADMIN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtdXNhdmlyQGluaG9sbGFuZC5ubCIsImF1dGgiOlt7ImF1dGhvcml0eSI6IlJPTEVfRU1QTE9ZRUUifV0sImlhdCI6MTY1NTc1NzQ0NywiZXhwIjoxNjU1NzYxMDQ3fQ.emc9KqIviHpTpn2Y94srnjEqyNpazq3T3_aU5zZPloc";

    private static final String EXPIRED_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtdXNhdmlyQGluaG9sbGFuZC5ubCIsImF1dGgiOlt7ImF1dGhvcml0eSI6IlJPTEVfRU1QTE9ZRUUifV0sImlhdCI6MTY1NTU1NDA5OSwiZXhwIjoxNjU1NTU3Njk5fQ.JXnhcAhZtUS9s2zv3J_fAJQAAiL7gwbxz8zNKJdLHlQ";
    private static final String INVALID_TOKEN = "invalid";

    private final HttpHeaders httpHeaders = new HttpHeaders();
    private final TestRestTemplate restTemplate = new TestRestTemplate();

    private final ObjectMapper mapper = new ObjectMapper();

    private ResponseEntity<String> response;
    private HttpEntity<String> request;
    private Integer status;
    private WithdrawDTO withdrawDTO;
    private DepositDTO depositDTO;
    private String token = null;


    public AccountStepsDefinitions() {
        Given("^I have a valid token for the role \"([^\"]*)\"$", (String role) -> {
            httpHeaders.clear();
            if (role.equals("employee")) {
                token = VALID_TOKEN_ADMIN;
            }
            else if (role.equals("customer")) {
                token = VALID_TOKEN_USER;
            }
            else {
                throw new IllegalArgumentException("No such role exists");
            }
        });

        When("^I call the account endpoint by IBAN \"([^\"]*)\"$", (String iban) -> {
            httpHeaders.clear();
            httpHeaders.add("Authorization", "Bearer " + token);
            request = new HttpEntity<>(null, httpHeaders);
            response = restTemplate.exchange(getBaseUrl() + "/api/accounts/" + iban, HttpMethod.GET, new HttpEntity<>(null,httpHeaders), String.class);
            status = response.getStatusCodeValue();
        });

        Then("^I get status code (\\d+)$", (Integer code) -> {
            Assertions.assertEquals(code, status);
        });

        And("^I receive account information related to the IBAN$", () -> {
            JSONObject jsonObject = new JSONObject(response.getBody());
            Assertions.assertNotNull(jsonObject);
        });

        And("^I receive all accounts information$", () -> {
            JSONArray jsonArray = new JSONArray(response.getBody());
            Assertions.assertNotNull(jsonArray);
        });

        Given("^I am provided with an invalid token$", () -> {
            httpHeaders.clear();
            httpHeaders.add("Authorization", "Bearer " + INVALID_TOKEN);
        });

        Given("^I am provided with an expired token$", () -> {
            httpHeaders.clear();
            httpHeaders.add("Authorization", "Bearer " + EXPIRED_TOKEN);
        });

        When("^I make a deposit to IBAN \"([^\"]*)\"$", (String iban) -> {
            httpHeaders.clear();
            httpHeaders.add("Authorization",  "Bearer " + token);
            request = new HttpEntity<>(null, httpHeaders);
            response = restTemplate.exchange(getBaseUrl() + "/api/accounts/" + iban + "/deposit", HttpMethod.POST, new HttpEntity<>(null,httpHeaders), String.class);
            status = response.getStatusCodeValue();
        });
        When("^I try to withdraw with the IBAN \"([^\"]*)\"$", (String iban) -> {
            httpHeaders.clear();
            httpHeaders.add("Authorization",  "Bearer " + token);
            request = new HttpEntity<>(null, httpHeaders);
            response = restTemplate.exchange(getBaseUrl() + "/api/accounts/" + iban + "/withdraw", HttpMethod.POST, new HttpEntity<>(null,httpHeaders), String.class);
            status = response.getStatusCodeValue();
        });

        When("^I call the accounts endpoint$", () -> {
            httpHeaders.clear();
            httpHeaders.add("Authorization",  "Bearer " + token);
            request = new HttpEntity<>(null, httpHeaders);
            response = restTemplate.exchange(getBaseUrl() + "/api/accounts", HttpMethod.GET, new HttpEntity<>(null, httpHeaders), String.class);
            status = response.getStatusCodeValue();
        });
        And("^I provide the amount of \"([^\"]*)\" and the pincode of \"([^\"]*)\" for a withdrawal$", (String amount, String pincode) -> {
            withdrawDTO.setAmount(Double.parseDouble(amount));
            withdrawDTO.setPincode("xxxx");
        });
        And("^I provide the amount of \"([^\"]*)\" and the pincode of \"([^\"]*)\" for a deposit$", (String amount, String pincode) -> {
            depositDTO.setAmount(Double.parseDouble(amount));
            depositDTO.setPincode("xxxx");
        });

    }
}

