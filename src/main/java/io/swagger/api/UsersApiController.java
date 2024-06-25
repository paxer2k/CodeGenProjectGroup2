package io.swagger.api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import io.swagger.Security.JwtTokenProvider;
import io.swagger.annotations.Api;
import io.swagger.model.AccountDTO;
import io.swagger.model.NewUserDTO;
import io.swagger.model.UpdateUserDTO;
import io.swagger.model.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.entity.Account;
import io.swagger.model.entity.Role;
import io.swagger.model.entity.User;
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
import java.util.*;
import java.util.stream.Collectors;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-15T15:18:25.887Z[GMT]")
@RestController
@Api(tags = {"Users"})
public class UsersApiController implements UsersApi {

    private static final Logger log = LoggerFactory.getLogger(UsersApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @org.springframework.beans.factory.annotation.Autowired
    public UsersApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;

        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    }


   // @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<UserDTO> createUser(@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody NewUserDTO body) {
        ModelMapper modelMapper = new ModelMapper();

        //checking if fields are filled in
        if (body.getFirstName() == "" || body.getLastName() == "" || body.getEmail() == "" || body.getPassword() == ""
                || body.getAddress() == "" || body.getPhoneNumber() == ""){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Fields cannot be empty!");
        }
        User user = modelMapper.map(body, User.class);


        User loggedUser = returnUserfromToken();

        // if user has no token OR user role is not Employee then setting the role to Customer
        if (loggedUser == null || !loggedUser.getRoles().contains(Role.ROLE_EMPLOYEE)) {
            List<Role> roles = new ArrayList<>();
            roles.add(Role.ROLE_CUSTOMER);
            user.setRoles(roles);
        }
        String userCreated = userService.createUser(user);

        UserDTO userResponse = modelMapper.map(user, UserDTO.class);

        return new ResponseEntity<UserDTO>(userResponse, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Void> deleteUser(@Size(min = 1)@Parameter(in = ParameterIn.PATH, description = "The User ID", required=true, schema=@Schema()) @PathVariable("UID") String UID) {

        userService.deleteUser(UUID.fromString(UID));

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('CUSTOMER')")
    public ResponseEntity<UserDTO> getUser(@Size(min = 1)@Parameter(in = ParameterIn.PATH, description = "The User ID", required=true, schema=@Schema()) @PathVariable("UID") String UID) {


        User loggedUser = returnUserfromToken();

        // if the logged in user is not an employee and if the provided user is not the same as logged in user, throw exception
        if (!loggedUser.getRoles().contains(Role.ROLE_EMPLOYEE)){
            if (!loggedUser.getUserId().equals(UUID.fromString(UID))){
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not allowed to access other customer's details!");
            }
        }
        User user = userService.getUser(UUID.fromString(UID));

        ModelMapper modelMapper = new ModelMapper();

        UserDTO userResponse = modelMapper.map(user, UserDTO.class);

        return new ResponseEntity<UserDTO>(userResponse, HttpStatus.OK);

    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<List<UserDTO>> getUsers(@Parameter(in = ParameterIn.QUERY, description = "search for this substring" ,schema=@Schema()) @Valid @RequestParam(value = "name", required = false) String name,@Min(0)@Parameter(in = ParameterIn.QUERY, description = "number of records to skip for pagination" ,schema=@Schema(allowableValues={  }
)) @Valid @RequestParam(value = "skip", required = false) Integer skip,@Min(0) @Max(50) @Parameter(in = ParameterIn.QUERY, description = "maximum number of records to return" ,schema=@Schema(allowableValues={  }, maximum="50"
)) @Valid @RequestParam(value = "limit", required = false) Integer limit,@Min(0) @Max(50) @Parameter(in = ParameterIn.QUERY, description = "return users with number of accounts" ,schema=@Schema(allowableValues={  }, maximum="50"
    )) @Valid @RequestParam(value = "accounts", required = false) Integer accounts) {

        List<User> users = userService.getUsers();

        List<UserDTO> userDTOS = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();

        // filtering by account size, name, and pagination
        if (accounts != null){
            userDTOS = users.stream()
                    .map(user -> modelMapper.map(user, UserDTO.class)).filter(user ->user.getAccounts().size()==accounts).collect(Collectors.toList());
        }
        else if (skip!=null && limit !=null && name !=null){
            userDTOS = users.stream()
                    .map(user -> modelMapper.map(user, UserDTO.class)).skip(skip).limit(limit).filter(user ->user.getFirstName().toLowerCase().contains(name.toLowerCase()) || user.getLastName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toList());
        }
        else if (skip!=null && limit !=null){
            userDTOS = users.stream()
                    .map(user -> modelMapper.map(user, UserDTO.class)).skip(skip).limit(limit).collect(Collectors.toList());
        }
        else {
            userDTOS = users.stream()
                    .map(user -> modelMapper.map(user, UserDTO.class)).collect(Collectors.toList());
        }
        return new ResponseEntity<List<UserDTO>>(userDTOS, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<UserDTO> updateUser(@Size(min = 1)@Parameter(in = ParameterIn.PATH, description = "The User ID", required=true, schema=@Schema()) @PathVariable("UID") String UID,@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody UpdateUserDTO body) {

        ModelMapper modelMapper = new ModelMapper();
        User user = modelMapper.map(body, User.class);

        User loggedUser = returnUserfromToken();

        if (!loggedUser.getRoles().contains(Role.ROLE_EMPLOYEE)){
            if (!loggedUser.getUserId().equals(UUID.fromString(UID))){
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not allowed to access other customer's details!");
            }
        }

        // make sure fields are field out
        if (UID == null || body.getFirstName() == ""|| body.getLastName() == "" || body.getEmail() ==""
                || body.getPhoneNumber() == "" || body.getAddress()=="" || body.getTransactionLimit() == null|| body.getDayLimit() == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Please fill out all the fields!");
        }

        User oldUser = userService.getUser(UUID.fromString(UID));
        user.setUserId(UUID.fromString(UID));
        user.setAccounts(oldUser.getAccounts());
        user.setCurrentDayLimit(oldUser.getCurrentDayLimit());
        user.setRoles(oldUser.getRoles());
        user.setPassword(oldUser.getPassword());
        user = userService.updateUser(user);

        UserDTO userResponse = modelMapper.map(user, UserDTO.class);

        return new ResponseEntity<UserDTO>(userResponse, HttpStatus.CREATED);
    }
    // checking if user has (valid)token or not
    private User returnUserfromToken(){
        String jwtToken = jwtTokenProvider.resolveToken(request);


        // if user doesn't have token then return null
        if (jwtToken == null) {
            return null;
        }

        // if user has invalid token then throw UNAUTHORIZED
        if (!jwtTokenProvider.validateToken(jwtToken)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }

        //else return the user
        return userService.getUserByEmail(jwtTokenProvider.getUsername(jwtToken));
    }
}
