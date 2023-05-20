package io.inholland.groep4.api.service;
import io.inholland.groep4.api.model.DTO.UserAccountDTO;
import io.inholland.groep4.api.model.User;
import io.inholland.groep4.api.model.UserAccount;
import io.inholland.groep4.api.repository.UserAccountRepository;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class UserAccountService {
    private final HttpServletRequest request;

    @Autowired
    private UserAccountRepository userAccountRepository;
    @Autowired
    private UserService userService;

    public UserAccountService(HttpServletRequest request) {
        this.request = request;
    }
    public UserAccount setAccount(UserAccountDTO userAccount)
    {
        // Create a new account
        UserAccount account = new UserAccount();

        // Set the properties
        account.setOwner(userAccount.getOwner());

        if(userAccount.getAccountType().equals(UserAccount.AccountTypeEnum.CURRENT) || userAccount.getAccountType().equals(UserAccount.AccountTypeEnum.SAVINGS))
        {
            account.setAccountType(userAccount.getAccountType());
        }
        else
        {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Incorrect account type given");
        }

        if(userAccount.getLowerLimit() < 100.0 || userAccount.getLowerLimit() > 10000.0)
        {
            System.out.println("limit");
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Incorrect lower limit given");
        }
        else {
            account.setLowerLimit(userAccount.getLowerLimit());
        }

        if(userAccount.getAccountStatus().equals(UserAccount.AccountStatusEnum.ACTIVE) || userAccount.getAccountStatus().equals(UserAccount.AccountStatusEnum.INACTIVE))
        {
            account.setAccountStatus(userAccount.getAccountStatus());
        }
        else {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Incorrect account status given");
        }
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
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Incorrect iban given");
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

    public Optional<List<UserAccount>> getAllAccounts() {
        try {
            // Create an empty list for accounts
            Optional<List<UserAccount>> accounts;

            // Check the role of the user
            if (request.isUserInRole("ROLE_EMPLOYEE")){
                // User is an employee, getting all accounts
                accounts = Optional.of(userAccountRepository.findAll());
            } else {
                // Get the user accounts
                Principal principal = request.getUserPrincipal();

                // Get the current user
                User user = userService.findByUsername(principal.getName());

                // Get the user accounts
                accounts = getAccountsByUser(user);
            }
            return accounts;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Account not found");
        }
    }

    public UserAccount getAccountById(Long id) {
        if (userAccountRepository.getUserAccountById(id) == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Account not found");
        }
        UserAccount account = new UserAccount();
        Principal principal = request.getUserPrincipal();
        User user = userService.findByUsername(principal.getName());
        // Check if the user is an employee or account owner
        if (request.isUserInRole("ROLE_EMPLOYEE") || checkIfAccountBelongsToOwner(user, id)) {
            account = userAccountRepository.getUserAccountById(id);
        }
        return account;
    }

    public boolean checkIfAccountBelongsToOwner(User user, Long id) {
        if (!userAccountRepository.existsByOwnerAndId(user, id)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Account does not belong to owner");
        }
        return userAccountRepository.existsByOwnerAndId(user, id);
    }

    public UserAccount save(UserAccount user) {
        if (userAccountRepository.getUserAccountById(user.getId()) == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "No account to save");
        }
        return userAccountRepository.save(user);
    }

    public Optional<List<UserAccount>> getAccountsByUser(User user) {
        if (!userAccountRepository.getUserAccountsByOwner(user).isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "User account found");
        }
        return userAccountRepository.getUserAccountsByOwner(user);
    }

    public boolean existByIBAN(String iban) {
        return userAccountRepository.existsByIBAN(iban);
    }
}