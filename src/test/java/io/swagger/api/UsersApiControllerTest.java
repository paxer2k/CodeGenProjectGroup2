package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.Security.JwtTokenProvider;
import io.swagger.Swagger2SpringBoot;
import io.swagger.model.NewUserDTO;
import io.swagger.model.UpdateUserDTO;
import io.swagger.model.UserDTO;
import io.swagger.model.entity.*;
import io.swagger.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

//@ExtendWith(SpringExtension.class)
//@WebMvcTest(UsersApiController.class)
//@Import(UsersApiController.class)
//@ComponentScan({"com.delivery.request"})
////@ContextConfiguration(classes = TestConfig.class)
//@AutoConfigureMockMvc(addFilters = false)
//@SpringBootTest
//@AutoConfigureMockMvc()

@ActiveProfiles("unitTesting")
@SpringBootTest(classes = { Swagger2SpringBoot.class })
@AutoConfigureMockMvc
class UsersApiControllerTest {

   @Autowired
  private MockMvc mockMvc;

    private  User musavir;

   //@Autowired
   @MockBean
    private UserService userService;

    @Autowired
   private UsersApiController usersApiController;

    @Autowired
    private ObjectMapper mapper;

    private List<User> userList;



    @Test
    void deleteUser() {
    }

    public NewUserDTO newUserDTO(){
        NewUserDTO musavir = new NewUserDTO();
        musavir.setFirstName("james");
        musavir.setLastName("Ahmed");
        musavir.setEmail("musavir123@inholland.nl");
        musavir.setPassword("test12345");
        musavir.setAddress("1231JK sjkfa, haarlem");
        musavir.setPhoneNumber("1235266");
        List<Role> roles = new ArrayList<>();
        roles.add(Role.ROLE_EMPLOYEE);
        musavir.setRoles(roles);
        return  musavir;
    }




    //---Get all users---//
    @Test
    @WithMockUser(roles = {"EMPLOYEE"})
    void getAllUsers()throws Exception{

      when(userService.getUsers()).thenReturn(userList);
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName").value("Musavir"));

    }




    //---Create user---//
    @Test
    @WithMockUser(username = "james@inholland.nl", password = "test12345", roles = "EMPLOYEE")
    void createUser() throws Exception{
        this.mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(newUserDTO()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated());

    }


    //---get user by email---//
    @Test
    void getUserByEmail() {
        User user = userService.getUserByEmail("musavir@inholland.nl");
        assertEquals(user.getEmail(), musavir.getEmail());
    }


    @Test
    @WithMockUser(username = "musavir@inholland.nl", roles = { "EMPLOYEE" })
    public void deleteUserPerformedByEmployee() {

        ResponseEntity<Void> response = usersApiController.deleteUser("5132ed08-c286-4125-998e-2cc8ab84c626");

        // Assertions
        assertNotNull(response);
        assertNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @WithMockUser(username = "alex@inholland.nl", roles = { "CUSTOMER" })
    public void deleteUserPerformedByCustomer() {
        // Assertions
        assertThrows(AccessDeniedException.class, () -> usersApiController.deleteUser("5132ed08-c286-4125-998e-2cc8ab84c626"));
    }


    public UpdateUserDTO updateUserDTO(){
        UpdateUserDTO Jone = new UpdateUserDTO();
        Jone.setFirstName("Jone");
        Jone.setLastName("Ahmed");
        Jone.setEmail("musavir123@inholland.nl");
        Jone.setAddress("1231JK sjkfa, haarlem");
        Jone.setPhoneNumber("1235266");
        Jone.setDayLimit(400);
        Jone.setTransactionLimit(BigDecimal.valueOf(500));
        return  Jone;
    }


    //---Update user---//
    @Test
    @WithMockUser(username = "musavir@inholland.nl", password = "test12345", roles = "EMPLOYEE")
    void updateUser() throws Exception{
       // String uid = "thisIsID";

        when(userService.getUser(UUID.fromString("5132ed08-c286-4125-998e-2cc8ab84c626"))).thenReturn(musavir);
        this.mockMvc.perform(put("/users/5132ed08-c286-4125-998e-2cc8ab84c626")
                        .content(mapper.writeValueAsString(updateUserDTO()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

    }

    @Test
    @WithMockUser(username = "musavir@inholland.nl", roles = { "EMPLOYEE" })
    public void updateUserPerformedByEmployee() {
        // Setup
        given(userService.getUserByEmail("musavir@inholland.nl")).willReturn(userList.get(1));

        // Execution
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();

        // Assertions
        ResponseEntity<UserDTO> response = usersApiController.updateUser("5132ed08-c286-4125-998e-2cc8ab84c626", updateUserDTO);
        assertNotNull(response);
        assertNull(response.getBody());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }




    @BeforeEach
    public void users(){
        userList = new ArrayList<>();

       // String uid = "thisIsID";

        musavir = new User();
        musavir.setUserId(UUID.fromString("5132ed08-c286-4125-998e-2cc8ab84c626"));
        musavir.setFirstName("Musavir");
        musavir.setLastName("Ahmed");
        musavir.setEmail("musavir@inholland.nl");
        musavir.setPhoneNumber("1235266");
        musavir.setAddress("1231JK sjkfa, haarlem");

        musavir.setTransactionLimit(1000.0);
        musavir.setDayLimit(2000.0);

        Account account1 = new Account();
        account1.setIban("NL01INHO0000000002");
        account1.setAccountStatus(AccountStatus.ACTIVE);
        account1.setBalance(100.0);
        account1.setAbsoluteLimit(0.0);
        account1.setAccountType(AccountType.CURRENT);
        account1.setUser(musavir);


        List<Account> accounts = new ArrayList<>();
        accounts.add(account1);
        musavir.setAccounts(accounts);

        musavir.setCurrentDayLimit(500.0);

        List<Role> roles = new ArrayList<>();
        roles.add(Role.ROLE_EMPLOYEE);
        musavir.setRoles(roles);
        musavir.setPassword("test12345");


        User alex = new User();
        alex.setFirstName("alex");
        alex.setLastName("Ahmed");
        alex.setEmail("alex@inholland.nl");
        alex.setPassword("test12345");
        alex.setAddress("1231JK sjkfa, haarlem");
        alex.setPhoneNumber("1235266");
        alex.setTransactionLimit(1000.0);
        List<Role> alexroles = new ArrayList<>();
        roles.add(Role.ROLE_CUSTOMER);
        alex.setRoles(alexroles);
        alex.setDayLimit(2000.0);
        userList.add(musavir);
        userList.add(alex);
    }
}