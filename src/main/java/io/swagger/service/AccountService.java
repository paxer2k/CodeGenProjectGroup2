package io.swagger.service;

import io.swagger.model.DepositDTO;
import io.swagger.model.NewAccountDTO;
import io.swagger.model.WithdrawDTO;
import io.swagger.model.entity.*;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.TransactionRepository;
import io.swagger.repository.UserRepository;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    private final String bankIBAN = "NL01INHO0000000001";
    private final double defaultBalance = 0.00;
    private final double defaultAbsoluteLimit = 0.00;

    public List<Account> getAccounts() {

        return accountRepository.findAll();
    }

    public Account getAccount(String IBAN) {
        return accountRepository.getOne(IBAN);
    }

    public Account createAccount(NewAccountDTO body) {
        Account account = new Account();

        User user=userRepository.getOne(UUID.fromString(body.getUserID()));

        account.setIban(ibanGenerate());

        account.setUser(user);
        account.setAccountType(AccountType.valueOf(body.getAccountType().toString().toUpperCase()));
        account.setBalance(this.defaultBalance);
        account.setAccountStatus(AccountStatus.ACTIVE);
        account.setAbsoluteLimit(this.defaultAbsoluteLimit);
        Account createdAccount =accountRepository.save(account);
        List<Account> accountList = user.getAccounts();
        accountList.add(createdAccount);
        return createdAccount;
    }

    public Account createAccountStartApp(Account account){
        account.setIban(ibanGenerate());
        return accountRepository.save(account);
    }
    public void deleteAccount(String IBAN) {
        accountRepository.deleteById(IBAN);
    }

    public Account updateAccount(Account account) {
        return accountRepository.save(account);
    }

    public Account withdraw(Double amount, String IBAN, String pincode, String withdrawType) {

        //String pincode = body.getPincode(); // pincode will be checked if it  is correct...

        Account account = getAccount(IBAN);


        if (account.getBalance()>account.getAbsoluteLimit()){
            Double currentBalance = account.getBalance() - amount;

            LocalDate date = LocalDate.now();
            List<Transaction> withdrawals = transactionRepository.getTransactionByFromIBANAndDateBetween(IBAN, date.atStartOfDay(), LocalTime.MAX.atDate(date));
            double totalWithdrawals = 0.0;
            if (withdrawals.size()>0){
                totalWithdrawals = withdrawals.stream().mapToDouble(Transaction::getAmount).sum();
//                for (Transaction transaction:withdrawals) {
//                    totalWithdrawals += transaction.getAmount();
//                }
                if (amount+totalWithdrawals>account.getUser().getDayLimit()){
                    throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "The amount per day cannot exceed the daily limit!");
                }
            }
            if (currentBalance>=account.getAbsoluteLimit()){
               // Double currentDayLimit = account.getUser().getCurrentDayLimit() - amount;
                account.setBalance(currentBalance);
               // account.getUser().setCurrentDayLimit(currentDayLimit);
                if (withdrawType.equals("withdraw")){
                    Transaction transaction = new Transaction(account.getIban(), this.bankIBAN, amount, LocalDateTime.now(), account.getUser().getUserId());
                    transactionRepository.save(transaction);
                }
                return accountRepository.save(account);
            }
            else {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "The amount cannot exceed the absolute limit!");
            }
        }
        else {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "The balance cannot be lower than the absolute limit!");
        }
    }
    public Account deposit(Double amount, String IBAN, String pincode) {

        //String pincode = pincode; // pincode will be checked if it  is correct...
        Account account = getAccount(IBAN);

        Double balance = account.getBalance() + amount;
        account.setBalance(balance);

        return accountRepository.save(account);
    }
    public String ibanGenerate(){

        Iban iban = ibanBuilder();
        while (accountRepository.existsById(iban.toString())){
            iban = ibanBuilder();
        }
        return iban.toString();
    }
    public Iban ibanBuilder(){
        return new Iban.Builder()
                .countryCode(CountryCode.NL)
                .bankCode("INHO").accountNumber(getRandomNumberString()).build();
    }
    public static String getRandomNumberString() {
        // It will generate 11 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999999);

        // this will convert any number sequence into 11 character.
        return String.format("%010d", number);
    }

    public void createBankIban(){
        Account account1 = new Account();
        account1.setIban(this.bankIBAN);
        account1.setAccountStatus(AccountStatus.ACTIVE);
        account1.setBalance(1000000000000000000000.0);
        account1.setAbsoluteLimit(0.0);
        account1.setAccountType(AccountType.CURRENT);
        accountRepository.save(account1);
    }

}
