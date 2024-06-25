package io.swagger.api.steps.user;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.cucumber.java8.En;
import io.swagger.model.NewTransactionDTO;
import io.swagger.model.NewUserDTO;
import io.swagger.model.UserDTO;
import lombok.extern.slf4j.Slf4j;
import io.swagger.api.steps.BaseStepDefinitions;
import io.swagger.model.LoginDTO;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.lang.annotation.Annotation;

@Slf4j

public class UserStepDefinitions extends BaseStepDefinitions implements En {

    private final HttpHeaders httpHeaders = new HttpHeaders();
    private final TestRestTemplate restTemplate = new TestRestTemplate();

    private final ObjectMapper mapper = new ObjectMapper();

    private ResponseEntity<String> response;
    private HttpEntity<String> request;

    private Integer status;
    private NewUserDTO dto;
    private UserDTO userDTO;
    private String token;

    // Token is valid for one year
    // private static final String VALID_TOKEN_USER = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbGV4QGluaG9sbGFuZC5ubCIsImF1dGgiOlt7ImF1dGhvcml0eSI6IlJPTEVfQ1VTVE9NRVIifV0sImlhdCI6MTY1NTU3Nzg3OSwiZXhwIjoxNjU1NTgxNDc5fQ.QaPRE3Djcbhy1LlTSB9I0-Yj-JTAJKkw3VSe8Pb6akc";
    private static final String VALID_TOKEN_USER = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbGV4QGluaG9sbGFuZC5ubCIsImF1dGgiOlt7ImF1dGhvcml0eSI6IlJPTEVfQ1VTVE9NRVIifV0sImlhdCI6MTY1NTc3NDQ4MywiZXhwIjoxNjU1Nzc4MDgzfQ.zowpj9xP0_ywlWI6VQr6aSVGe2BlfhbqLRWbjBLbGZ8";
    //private static final String VALID_TOKEN_ADMIN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtdXNhdmlyQGluaG9sbGFuZC5ubCIsImF1dGgiOlt7ImF1dGhvcml0eSI6IlJPTEVfRU1QTE9ZRUUifV0sImlhdCI6MTY1NTU3NzgwNywiZXhwIjoxNjU1NTgxNDA3fQ.CM2wMt6vvWPDqlnz-qXaeDQud9l0Q1RNgRJwkCK7vVc";
    private static final String VALID_TOKEN_ADMIN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtdXNhdmlyQGluaG9sbGFuZC5ubCIsImF1dGgiOlt7ImF1dGhvcml0eSI6IlJPTEVfRU1QTE9ZRUUifV0sImlhdCI6MTY1NTg0MDQxMywiZXhwIjoxNjU1ODQ0MDEzfQ.vRJdeerpUJTY-BZ__EJ5Rzszy-Yjg9FHJNQ0mjei564";

    private static final String EXPIRED_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtdXNhdmlyQGluaG9sbGFuZC5ubCIsImF1dGgiOlt7ImF1dGhvcml0eSI6IlJPTEVfRU1QTE9ZRUUifV0sImlhdCI6MTY1NTU1NDA5OSwiZXhwIjoxNjU1NTU3Njk5fQ.JXnhcAhZtUS9s2zv3J_fAJQAAiL7gwbxz8zNKJdLHlQ";
    private static final String INVALID_TOKEN = "invalid";




    //eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtdXNhdmlyQGluaG9sbGFuZC5ubCIsImF1dGgiOlt7ImF1dGhvcml0eSI6IlJPTEVfRU1QTE9ZRUUifV0sImlhdCI6MTY1NTU1NDA5OSwiZXhwIjoxNjU1NTU3Njk5fQ.JXnhcAhZtUS9s2zv3J_fAJQAAiL7gwbxz8zNKJdLHlQ


    public UserStepDefinitions() {

        Given("^I am not logged in$", () -> {
            httpHeaders.clear();
            //httpHeaders.add("Authorization", "Bearer " + INVALID_TOKEN);
        });

        When("^I call /users endpoint$", () -> {

            request = new HttpEntity<>(null, httpHeaders);
            response = restTemplate.exchange(getBaseUrl() + "/api/users", HttpMethod.GET, new HttpEntity<>(null, httpHeaders), String.class);
            status = response.getStatusCodeValue();
        });


        Given("^I have invalid token$", () -> {
            httpHeaders.clear();
            httpHeaders.add("Authorization", "Bearer " + INVALID_TOKEN);
        });
        Given("^I have expired token$", () -> {
            httpHeaders.clear();
            httpHeaders.add("Authorization", "Bearer " + EXPIRED_TOKEN);
        });


        Given("^I have valid token for role \"([^\"]*)\"$", (String role) -> {
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
        Then("^I receive the status of (\\d+)$", (Integer code) -> {
            Assertions.assertEquals(code, status);
        });
        And("^I get all the users$", () -> {
            JSONArray jsonArray = new JSONArray(response.getBody());
            Assertions.assertNotNull(jsonArray);
        });
        When("^I make a post request to the users endpoint$", () -> {

            httpHeaders.add("Authorization", "Bearer " + token);
            httpHeaders.add("Content-Type", "application/json");
            request = new HttpEntity<>(mapper.writeValueAsString(dto), httpHeaders);
            response = restTemplate.postForEntity(getBaseUrl() + "/api/users", request, String.class);
            status = response.getStatusCodeValue();



        });

        And("^I have a valid user object with firstname \"([^\"]*)\" and lastname \"([^\"]*)\" and email \"([^\"]*)\" and password \"([^\"]*)\" and address \"([^\"]*)\" " +
                "and phonenumber \"([^\"]*)\"$", (String firstname, String lastname, String email, String password, String address, String phonenumber) -> {

            dto = new NewUserDTO (firstname,lastname,email,password,address,phonenumber);

        });


    }
}

