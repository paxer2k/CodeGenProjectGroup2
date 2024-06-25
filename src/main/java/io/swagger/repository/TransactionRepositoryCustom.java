package io.swagger.repository;

import io.swagger.model.entity.Transaction;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepositoryCustom {
     List<Transaction> getTransactionFiltered(String IBAN, String startDate, String endDate, BigDecimal moreBalance, BigDecimal lessBalance,
                                              BigDecimal equalBalance, String toIBAN);
}
