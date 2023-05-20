package io.inholland.groep4.api.repository;

import io.inholland.groep4.api.model.UserAccount;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserAccountRepositoryTest {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Test
    public void should_get_useraccount_by_id() {
        // The given useraccount...
        UserAccount createdUserAccount = new UserAccount();
        userAccountRepository.save(createdUserAccount);

        // When
        UserAccount requestedUserAccount = userAccountRepository.getUserAccountById(createdUserAccount.getId());

        // Then
        assertThat(requestedUserAccount).isEqualTo(createdUserAccount);
    }
}
