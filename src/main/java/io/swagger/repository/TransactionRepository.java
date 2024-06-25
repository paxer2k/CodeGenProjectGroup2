package io.swagger.repository;

import io.swagger.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String>, TransactionRepositoryCustom{
    List<Transaction> getTransactionByFromIBAN(String IBAN);
    List<Transaction> getTransactionByFromIBANAndDateBetween(String IBAN, LocalDateTime startDate, LocalDateTime endDate);
    List<Transaction> getTransactionByFromIBANAndAmountBetween(String IBAN, Double moreBalance, Double lessBalance);
    List<Transaction> getTransactionByFromIBANAndAmountEquals(String IBAN, Double equalBalance);
    List<Transaction> getTransactionByFromIBANOrToIBAN(String fromIBAN, String toIBAN);

    List<Transaction> getTransactionByFromIBANOrFromIBANAndDateBetweenOrFromIBANAndAmountBetweenOrFromIBANAndAmountEqualsOrFromIBANAndToIBAN(String IBAN,
    String IBAN1, LocalDateTime startDate, LocalDateTime endDate, String IBAN2, Double moreBalance, Double lessBalance, String IBAN3, Double equalBalance,
                                                                                                                                             String fromIBAN, String toIBAN);

    //List<Transaction> getAllById(UUID IBAN);

}
