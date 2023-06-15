package io.inholland.groep4.api.service;
import io.inholland.groep4.api.model.DTO.UserAccountDTO;
import io.inholland.groep4.api.model.User;
import io.inholland.groep4.api.model.UserAccount;
import io.inholland.groep4.api.repository.UserAccountRepository;
import io.inholland.groep4.api.repository.UserRepository;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class UserAccountService {

    @Autowired
    private UserAccountRepository userAccountRepository;

    public UserAccount setAccount(UserAccountDTO userAccount)
    {
        // Create a new account
        UserAccount account = new UserAccount();

        // Set account type
        UserAccount.AccountTypeEnum accountType = userAccount.getAccountType();
        if (accountType != UserAccount.AccountTypeEnum.CURRENT && accountType != UserAccount.AccountTypeEnum.SAVINGS) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect account type given");
        }
        account.setAccountType(accountType);

        // Set lower limit
        Double lowerLimit = userAccount.getLowerLimit();
        if (lowerLimit < 100.0 || lowerLimit > 10000.0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect lower limit given");
        }
        account.setLowerLimit(lowerLimit);

        // Set account status
        UserAccount.AccountStatusEnum accountStatus = userAccount.getAccountStatus();
        if (accountStatus != UserAccount.AccountStatusEnum.ACTIVE && accountStatus != UserAccount.AccountStatusEnum.INACTIVE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect account status given");
        }
        account.setAccountStatus(accountStatus);

        // Set account balance
        Double accountBalance = userAccount.getAccountBalance();
        if (accountBalance < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect account balance given");
        }
        account.setAccountBalance(accountBalance);

        return account;
    }

    public UserAccount add(UserAccount userAccount, boolean randomIBAN) {
        try{
            // Check if a random iban should be generated
            if (randomIBAN) {
                userAccount.setIBAN(getIBAN());
            }

            userAccountRepository.save(userAccount);
            return userAccount;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect iban given");
        }
    }

    public String getIBAN() {
        // Generate a new IBAN
        Iban iban = new Iban.Builder().countryCode(CountryCode.NL).bankCode("INHO").buildRandom();

        // Check if not already in use
        // If in use, get a new one until it's unique
        while (existByIBAN(iban.toString())) {
            iban = new Iban.Builder().countryCode(CountryCode.NL).bankCode("INHO").buildRandom();
        }
        return iban.toString();
    }

    public List<UserAccount> getAllAccounts(User user, Boolean role) {
        try {
            if (role.equals(true)) {
                // User is an employee, getting all accounts
                return userAccountRepository.findAll();
            }
            else {
                // Get the user accounts
                List<UserAccount> userAccounts = getAccountsByUser(user);
                if (userAccounts.isEmpty()) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User accounts not found");
                }
                return userAccounts;
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving user accounts", e);
        }
    }

    public UserAccount getAccountById(Long id, User user, Boolean role) {
        UserAccount account = userAccountRepository.getUserAccountById(id);
        if (account == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }

        // Check if the user is an employee or account owner
        if (role.equals(true) || checkIfAccountBelongsToOwner(user, id)) {
            return account;
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }
    }

    public boolean checkIfAccountBelongsToOwner(User user, Long id) {
        if (!userAccountRepository.existsByOwnerAndId(user, id)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Account does not belong to owner");
        }
        return userAccountRepository.existsByOwnerAndId(user, id);
    }

    public UserAccount save(UserAccount user) {
        if (userAccountRepository.getUserAccountById(user.getId()) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No account to save");
        }
        return userAccountRepository.save(user);
    }


    public List<UserAccount> getAccountsByUser(User user) {
        List<UserAccount> userAccounts = userAccountRepository.getUserAccountsByOwner(user);
        if (userAccounts.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }
        return userAccounts;
    }

    public boolean existByIBAN(String iban) {
        return userAccountRepository.existsByIBAN(iban);
    }
}