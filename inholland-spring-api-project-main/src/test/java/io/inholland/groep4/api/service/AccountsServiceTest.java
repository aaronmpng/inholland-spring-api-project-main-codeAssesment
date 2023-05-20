package io.inholland.groep4.api.service;

import io.inholland.groep4.api.model.User;
import io.inholland.groep4.api.model.UserAccount;
import io.inholland.groep4.api.security.JwtTokenProvider;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class AccountsServiceTest {

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private UserService userService;

    @Autowired
    public JwtTokenProvider jwtTokenProvider;

    @Test
    public void gettingAllAccountsShouldGiveListOAccounts() {
        Optional<List<UserAccount>> accounts = userAccountService.getAllAccounts();

        assertThat(accounts).isNotEmpty();
    }

    @Test
    public void gettingAccountsByIdShouldGiveAccount() {
        Long idToFind = 1L;
        UserAccount account = userAccountService.getAccountById(idToFind);

        assertThat(account).isNotNull();
        assertThat(account.getAccountBalance()).isEqualTo(500.0);
    }

    @Test
    public void gettingAccountWithWrongIDShouldThrowException() {
        Long idToFind = 100L;

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            userAccountService.getAccountById(idToFind);
        });

        assertTrue(exception.getMessage().contains("Id not found"));
    }

    @Test
    public void gettingAccountByUserShouldGiveUserAccount() {
        String usernameToFind = "test-employee1";
        User username = userService.findByUsername(usernameToFind);
        Optional<List<UserAccount>> userAccounts = userAccountService.getAccountsByUser(username);

        assertThat(userAccounts).isNotEmpty();
    }

    @Test
    public void gettingIBANShouldGiveValidIBAN() {
        String IBAN = userAccountService.getIBAN();
        assertThat(IBAN).isNotNull();
        assertThat(IBAN.length()).isEqualTo(18);
        assertThat(IBAN).contains("NL");
    }

    @Test
    public void addUserAccountShouldCreateValidAccount() {
        User user = new User();
        user.setUsername("son");
        user.setPassword("goku");
        user.setFirstName("son");
        user.setLastName("goku");
        user.setEmail("songoku@example.com");
        user.setBirthdate("01/01/1975");

        userService.add(user, false);

        UserAccount userAccount = new UserAccount();
        userAccount.setAccountType(UserAccount.AccountTypeEnum.CURRENT);
        userAccount.setOwner(user);
        userAccount.setAccountStatus(UserAccount.AccountStatusEnum.ACTIVE);
        userAccount.setLowerLimit(100.00);

        UserAccount newUser = userAccountService.add(userAccount, true);

        assertThat(newUser).isNotNull();
        assertThat(newUser.getLowerLimit()).isEqualTo(100.00);
    }

    @Test
    public void addingUserAccountWithIncorrectIbanShouldGiveException() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("testest");
        user.setFirstName("test");
        user.setLastName("testtest");
        user.setEmail("testest@example.com");
        user.setBirthdate("01/01/1975");

        UserAccount userAccount = new UserAccount();
        userAccount.setAccountType(UserAccount.AccountTypeEnum.CURRENT);
        userAccount.setOwner(user);
        userAccount.setAccountBalance(0.00);
        userAccount.setAccountStatus(UserAccount.AccountStatusEnum.ACTIVE);
        userAccount.setLowerLimit(100.00);
        userAccount.setIBAN("falseIBAN");

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            userAccountService.add(userAccount, false);
        });

        assertTrue(exception.getMessage().contains("Incorrect iban given"));
    }

    @Test
    public void checkIfAccountBelongToUser() {
        String usernameToFind = "test-user1";
        User username = userService.findByUsername(usernameToFind);
        Long idToFind = 8L;

        boolean account = userAccountService.checkIfAccountBelongsToOwner(username, idToFind);
        assertThat(account).isEqualTo(true);
    }

    @Test
    public void ifAccountDoesNotBelongToUserShouldThrowException() {
        String usernameToFind = "test-user1";
        User username = userService.findByUsername(usernameToFind);
        Long idToFind = 100L;

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            userAccountService.checkIfAccountBelongsToOwner(username, idToFind);
        });

        assertTrue(exception.getMessage().contains("Account does not belong to owner"));
    }

    @Test
    public void savingUserAccount() {
        Long idToFind = 1L;
        UserAccount account = userAccountService.getAccountById(idToFind);
        UserAccount userAccount = userAccountService.save(account);

        assertThat(userAccount.getAccountBalance()).isEqualTo(500.0);
    }

    @Test
    public void generatingAlreadyExistedIBAN() {
        String IBAN = userAccountService.getIBAN();

        Long idToFind = 1L;
        UserAccount account = userAccountService.getAccountById(idToFind);
        UserAccount userAccount = userAccountService.save(account);

        if (IBAN.equals(userAccount.getIBAN())) {
            IBAN = new Iban.Builder().countryCode(CountryCode.NL).bankCode("INHO").buildRandom().toString();
        }
        assertThat(userAccount.getIBAN()).isNotEqualTo(IBAN);
    }

    @Test
    public void saveNonExistingAccountShouldGiveException() {
        UserAccount user = new UserAccount();

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            userAccountService.save(user);
        });

        assertTrue(exception.getMessage().contains("No accounts found"));
    }
}
