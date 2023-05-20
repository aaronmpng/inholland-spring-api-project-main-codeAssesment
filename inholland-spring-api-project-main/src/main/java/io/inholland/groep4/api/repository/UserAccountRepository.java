package io.inholland.groep4.api.repository;

import io.inholland.groep4.api.model.User;
import io.inholland.groep4.api.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    UserAccount getUserAccountById(Long id);

    UserAccount findByIBAN(String IBAN);

    boolean existsByIBAN(String IBAN);

    Optional<List<UserAccount> > getUserAccountsByOwner(User user);

    boolean existsByOwnerAndId(User owner, Long id);
}
