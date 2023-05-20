package io.inholland.groep4.api.repository;

import io.inholland.groep4.api.model.Transaction;
import io.inholland.groep4.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction getTransactionById(Long id);

    List<Transaction> getTransactionsByOwner(User user);

    boolean existsByOwnerAndId(User owner, Long id);
}
