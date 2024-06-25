package io.swagger.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Entity
public class Transaction {

    @Id
    @GeneratedValue
    private long id;

    private String fromIBAN;

    private String toIBAN;

    private Double amount;

    private LocalDateTime date;

    private UUID userID;

    public Transaction() {
    }

    public Transaction(String fromIBAN, String toIBAN, Double amount, LocalDateTime date, UUID userID) {
        this.fromIBAN = fromIBAN;
        this.toIBAN = toIBAN;
        this.amount = amount;
        this.date = date;
        this.userID = userID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFromIBAN() {
        return fromIBAN;
    }

    public void setFromIBAN(String fromIBAN) {
        this.fromIBAN = fromIBAN;
    }

    public String getToIBAN() {
        return toIBAN;
    }

    public void setToIBAN(String toIBAN) {
        this.toIBAN = toIBAN;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public UUID getUserID() {
        return userID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }
}
