package io.swagger.api;

import io.swagger.annotations.Api;
import io.swagger.model.LoginDTO;
import io.swagger.model.LoginResponseBody;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.entity.User;
import io.swagger.service.LoginService;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.threeten.bp.OffsetDateTime;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-15T15:18:25.887Z[GMT]")
@RestController
@Api(tags = {"Login"})
public class LoginApiController implements LoginApi {

    private static final Logger log = LoggerFactory.getLogger(LoginApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private LoginService loginService;
    @Autowired
    private UserService userService;

    @org.springframework.beans.factory.annotation.Autowired
    public LoginApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<LoginResponseBody> login(@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody LoginDTO body) {

        // make sure fields are field out

        if (body.getEmail() == "") {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Please fill out the email");
        }

        if (body.getPassword() == ""){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Please fill out the password");
        }
        String loginToken = loginService.login(body.getEmail(), body.getPassword());
        User user = userService.getUserByEmail(body.getEmail());

        LoginResponseBody loginResponseBody = new LoginResponseBody();
        loginResponseBody.setJwTtoken(loginToken);
        Date now = new Date();
        Date validity = new Date(now.getTime() + 36000);
        loginResponseBody.setExpiresAt(validity);
        loginResponseBody.setTokenType("Bearer");
        loginResponseBody.setUserID(user.getUserId().toString());
        loginResponseBody.setRoles(user.getRoles());

        return new ResponseEntity<LoginResponseBody>(loginResponseBody, HttpStatus.OK);
    }

}
