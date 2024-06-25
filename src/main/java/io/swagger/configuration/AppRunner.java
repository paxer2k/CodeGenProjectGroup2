package io.swagger.configuration;

import io.swagger.model.entity.*;
import io.swagger.service.AccountService;
import io.swagger.service.TransactionService;
import io.swagger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class AppRunner implements ApplicationRunner {
    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Override
    public void run(ApplicationArguments args) throws Exception{
        User musavir = new User();
        musavir.setFirstName("Musavir");
        musavir.setLastName("Ahmed");
        musavir.setEmail("musavir@inholland.nl");
        musavir.setPassword("test12345");
        musavir.setAddress("1231JK sjkfa, haarlem");
        musavir.setPhoneNumber("1235266");
        musavir.setTransactionLimit(1000.0);
        List<Role> roles = new ArrayList<>();
        roles.add(Role.ROLE_EMPLOYEE);
        musavir.setRoles(roles);
        musavir.setDayLimit(2000.0);

        User alex = new User();
        alex.setFirstName("alex");
        alex.setLastName("Ahmed");
        alex.setEmail("alex@inholland.nl");
        alex.setPassword("test12345");
        alex.setAddress("1231JK sjkfa, haarlem");
        alex.setPhoneNumber("1235266");
        alex.setTransactionLimit(1000.0);
        List<Role> alexroles = new ArrayList<>();
        alexroles.add(Role.ROLE_CUSTOMER);
        alex.setRoles(alexroles);
        alex.setDayLimit(2000.0);

        accountService.createBankIban();

        Account account1 = new Account();
        account1.setIban("NL01INHO0000000002");
        account1.setAccountStatus(AccountStatus.ACTIVE);
        account1.setBalance(100.0);
        account1.setAbsoluteLimit(0.0);
        account1.setAccountType(AccountType.CURRENT);
        account1.setUser(musavir);

        Account account2 = new Account();
        account2.setIban("NL01INHO0000000003");
        account2.setAccountStatus(AccountStatus.ACTIVE);
        account2.setBalance(200.0);
        account2.setAbsoluteLimit(0.0);
        account2.setAccountType(AccountType.CURRENT);
        account2.setUser(alex);

        Account account3 = new Account();
        account3.setIban("NL01INHO0000000004");
        account3.setAccountStatus(AccountStatus.ACTIVE);
        account3.setBalance(200.0);
        account3.setAbsoluteLimit(0.0);
        account3.setAccountType(AccountType.SAVINGS);
        account3.setUser(alex);

        Transaction transaction1= new Transaction();
        transaction1.setFromIBAN("NL01INHO0000000002");
        transaction1.setToIBAN("NL01INHO0000000003");
        transaction1.setDate(LocalDateTime.now());
        transaction1.setAmount(10.0);

        Transaction transaction2= new Transaction();
        transaction1.setFromIBAN("NL01INHO0000000003");
        transaction1.setToIBAN("NL01INHO0000000002");
        transaction1.setDate(LocalDateTime.now());
        transaction1.setAmount(20.0);

        userService.createUser(alex);
        userService.createUser(musavir);
        accountService.createAccountStartApp(account1);
        accountService.createAccountStartApp(account2);
        accountService.createAccountStartApp(account3);
        transactionService.transfer(transaction1);
        transactionService.transfer(transaction2);
    }
}
