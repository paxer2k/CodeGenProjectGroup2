package io.swagger.api.steps.transactions;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.java8.En;
import io.swagger.api.steps.BaseStepDefinitions;
import io.swagger.model.NewTransactionDTO;
import io.swagger.model.TransactionDTO;
import lombok.extern.slf4j.Slf4j;
import org.iban4j.Iban;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@Slf4j
public class TransactionStepsDefinitions extends BaseStepDefinitions implements En {
    private final TestRestTemplate restTemplate = new TestRestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();
    private ResponseEntity<String> response;
    private HttpEntity<String> request;
    private Integer status;
    private NewTransactionDTO dto;
    private final HttpHeaders httpHeaders = new HttpHeaders();

    private String token;
    // Token is valid for one year
   // private static final String VALID_TOKEN_USER = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbGV4QGluaG9sbGFuZC5ubCIsImF1dGgiOlt7ImF1dGhvcml0eSI6IlJPTEVfQ1VTVE9NRVIifV0sImlhdCI6MTY1NTU3Nzg3OSwiZXhwIjoxNjU1NTgxNDc5fQ.QaPRE3Djcbhy1LlTSB9I0-Yj-JTAJKkw3VSe8Pb6akc";
    private static final String VALID_TOKEN_USER = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbGV4QGluaG9sbGFuZC5ubCIsImF1dGgiOlt7ImF1dGhvcml0eSI6IlJPTEVfQ1VTVE9NRVIifV0sImlhdCI6MTY1NTgwNDQ2OSwiZXhwIjoxNjU1ODA4MDY5fQ.rJHKBQx1FSVjK_KYsAfTsR-v4B0esi9lXbofPdjKKkQ";
    //private static final String VALID_TOKEN_ADMIN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtdXNhdmlyQGluaG9sbGFuZC5ubCIsImF1dGgiOlt7ImF1dGhvcml0eSI6IlJPTEVfRU1QTE9ZRUUifV0sImlhdCI6MTY1NTU3NzgwNywiZXhwIjoxNjU1NTgxNDA3fQ.CM2wMt6vvWPDqlnz-qXaeDQud9l0Q1RNgRJwkCK7vVc";
    private static final String VALID_TOKEN_ADMIN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtdXNhdmlyQGluaG9sbGFuZC5ubCIsImF1dGgiOlt7ImF1dGhvcml0eSI6IlJPTEVfRU1QTE9ZRUUifV0sImlhdCI6MTY1NTgwNDUzMywiZXhwIjoxNjU1ODA4MTMzfQ.urkiIGG8OrZep1sK9i9jpeVEE8ScQyd8xo1626TwaHY";

    private static final String EXPIRED_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtdXNhdmlyQGluaG9sbGFuZC5ubCIsImF1dGgiOlt7ImF1dGhvcml0eSI6IlJPTEVfRU1QTE9ZRUUifV0sImlhdCI6MTY1NTU1NDA5OSwiZXhwIjoxNjU1NTU3Njk5fQ.JXnhcAhZtUS9s2zv3J_fAJQAAiL7gwbxz8zNKJdLHlQ";
    private static final String INVALID_TOKEN = "invalid";

    public TransactionStepsDefinitions() {
        When("^I call the transactions endpoint$", () -> {

            request = new HttpEntity<>(null, httpHeaders);
            response = restTemplate.exchange(getBaseUrl() + "api/transactions/NL61INHO0737003362", HttpMethod.GET, new HttpEntity<>(null, httpHeaders), String.class);
            status = response.getStatusCodeValue();

        });
        When("^I make a post request to the transactions endpoint$", () -> {
            httpHeaders.clear();
            httpHeaders.add("Authorization", "Bearer " + token);
            httpHeaders.add("Content-Type", "application/json");
            request = new HttpEntity<>(mapper.writeValueAsString(dto), httpHeaders);
            response = restTemplate.postForEntity(getBaseUrl() + "/api/transactions", request, String.class);
            status = response.getStatusCodeValue();
        });
        Then("^the result is a status of (\\d+)$", (Integer code) -> {
            Assertions.assertEquals(code, status);
        });
        And("^I get all the transactions$", () -> {

            JSONArray jsonArray = new JSONArray(response.getBody());
            Assertions.assertNotNull(jsonArray);
        });
        Given("^I have a valid token for role \"([^\"]*)\"$", (String role) -> {
            httpHeaders.clear();
            if (role.equals("customer")){
                token = VALID_TOKEN_USER;
            }
            else if (role.equals("employee")){
                token = VALID_TOKEN_ADMIN;
            }
            else {
                throw new IllegalArgumentException("No such role");
            }
            httpHeaders.add("Authorization", "Bearer " + token);
        });

        Given("^I have an invalid token$", () -> {
            httpHeaders.clear();
            httpHeaders.add("Authorization", "Bearer " + INVALID_TOKEN);
        });

        Given("^I have an expired token$", () -> {
            httpHeaders.clear();
            httpHeaders.add("Authorization", "Bearer " + EXPIRED_TOKEN);
        });

        And("^I have a valid transaction object with fromIBAN \"([^\"]*)\" and toIBAN \"([^\"]*)\" and amount (\\d+) and userID \"([^\"]*)\" and pincode \"([^\"]*)\"$", (String fromIBAN, String toIBAN, Double amount, String userID, String pincode) -> {
            dto = new NewTransactionDTO(fromIBAN,toIBAN,amount,userID,pincode);
        });

        Given("^I have no token$", () -> {
            httpHeaders.clear();
        });



    }
}
