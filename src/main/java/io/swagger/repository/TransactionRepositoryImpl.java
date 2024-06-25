package io.swagger.repository;

import io.swagger.model.entity.Transaction;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TransactionRepositoryImpl implements TransactionRepositoryCustom {

    @PersistenceContext
    EntityManager em;

    @Override
    public List<Transaction> getTransactionFiltered(String IBAN, String startDate, String endDate, BigDecimal moreBalance,
                                                    BigDecimal lessBalance, BigDecimal equalBalance, String toIBAN) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Transaction> cq = cb.createQuery(Transaction.class);

        Root<Transaction> transaction = cq.from(Transaction.class);
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(transaction.get("fromIBAN"), IBAN));
        if (toIBAN != null) {
            predicates.add(cb.equal(transaction.get("toIBAN"), toIBAN));
        }
        if (startDate != null && endDate != null) {
            LocalDateTime startDateTime = LocalDateTime.of(LocalDate.parse(startDate), LocalTime.parse("00:00:00"));
            LocalDateTime endDateTime = LocalDateTime.of(LocalDate.parse(endDate), LocalTime.parse("23:59:59"));
            predicates.add(cb.between(transaction.get("date"), startDateTime, endDateTime));
        } else if (startDate != null) {
            LocalDateTime startDateTime = LocalDateTime.of(LocalDate.parse(startDate), LocalTime.parse("00:00:00"));
            predicates.add(cb.greaterThanOrEqualTo(transaction.get("date"), startDateTime));
        } else if (endDate != null) {
            LocalDateTime endDateTime = LocalDateTime.of(LocalDate.parse(endDate), LocalTime.parse("23:59:59"));
            predicates.add(cb.lessThanOrEqualTo(transaction.get("date"), endDateTime));
        }
        if (moreBalance != null && lessBalance != null) {
            predicates.add(cb.between(transaction.get("amount"), moreBalance.doubleValue(), lessBalance.doubleValue()));
        }
        if (equalBalance != null) {
            predicates.add(cb.equal(transaction.get("amount"), equalBalance.doubleValue()));
        }
        cq.where(predicates.toArray(new Predicate[0]));

        return em.createQuery(cq).getResultList();
    }

}
