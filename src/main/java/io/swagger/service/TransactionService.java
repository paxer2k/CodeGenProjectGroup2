package io.swagger.service;

import io.swagger.model.NewTransactionDTO;
import io.swagger.model.entity.Account;
import io.swagger.model.entity.Transaction;
import io.swagger.model.entity.User;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.TransactionRepository;
import io.swagger.repository.TransactionRepositoryImpl;
import io.swagger.repository.UserRepository;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionRepositoryImpl transactionRepositoryImpl;

    public List<Transaction> getTransactions(String IBAN) {
        return transactionRepository.getTransactionByFromIBANOrToIBAN(IBAN,IBAN);
    }
    public List<Transaction> getTransactionsFiltered(String IBAN, String startDate, String endDate, BigDecimal moreBalance,
                                                     BigDecimal lessBalance, BigDecimal equalBalance, String toIBAN) {
        return transactionRepositoryImpl.getTransactionFiltered(IBAN,startDate,endDate,moreBalance,lessBalance,equalBalance,toIBAN);
    }

    public Transaction transfer(Transaction transaction){
        return transactionRepository.save(transaction);
    }

    public Transaction transferMoney(NewTransactionDTO body) {
        String fromIBAN = body.getFromIBAN();
        String toIBAN = body.getToIBAN();
        String pinCode = body.getPincode();
        Double amount = body.getAmount();
        UUID userID = UUID.fromString(body.getUserID());

        Account fromAccount = accountService.getAccount(fromIBAN);
        Account toAccount = accountService.getAccount(toIBAN);
        // Transaction transaction;
        // check if from or to accounts are filled in
        if (fromAccount != null && toAccount != null) {
            // if amount is 0 or negative or above transaction limit, throw exception
            if (amount > 0) {
                if (amount<=fromAccount.getUser().getTransactionLimit()) {
                    Account account = accountService.withdraw(amount, fromAccount.getIban(), pinCode,"transfer");
                    // if withdraw did not work, no depositing allowed
                    if (account !=null){
                        accountService.deposit(amount, toAccount.getIban(), pinCode);

                        Transaction transaction = new Transaction(fromIBAN, toIBAN, amount, LocalDateTime.now(), userID);
                        transactionRepository.save(transaction);
                        return transaction;
                    }
                    else {
                        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Could not make a transaction!");
                    }
                }
                else {
                    throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Could not transfer above transaction limit");
                }

            } else {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Cannot transfer a negative amount or 0");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "FROM or TO accounts are not filled in!");
        }
    }
}
