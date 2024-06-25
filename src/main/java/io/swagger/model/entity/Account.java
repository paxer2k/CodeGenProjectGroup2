package io.swagger.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.UUID;

@Entity
public class Account {

    @Id
    private String iban ; // should auto generate

    @ManyToOne (cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ID")
    @JsonIgnore
    private User user;
    private  Double balance;
    private  Double absoluteLimit;

    private AccountType accountType;

    private AccountStatus accountStatus;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getAbsoluteLimit() {
        return absoluteLimit;
    }

    public void setAbsoluteLimit(Double absoluteLimit) {
        this.absoluteLimit = absoluteLimit;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public Account() {
    }
}
