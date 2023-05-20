package io.inholland.groep4.configuration;

import io.inholland.groep4.api.model.Transaction;
import io.inholland.groep4.api.model.User;
import io.inholland.groep4.api.model.UserAccount;
import io.inholland.groep4.api.service.TransactionService;
import io.inholland.groep4.api.service.UserAccountService;
import io.inholland.groep4.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class MyApplicationRunner implements ApplicationRunner {

    @Autowired
    UserService userService;

    @Autowired
    UserAccountService userAccountService;

    @Autowired
    TransactionService transactionService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Create the default BANK account
        UserAccount account = new UserAccount();
        account.setIBAN("NL01INHO0000000001");
        account.setAccountType(UserAccount.AccountTypeEnum.CURRENT);
        account.setAccountBalance(500.00);
        account.setAccountStatus(UserAccount.AccountStatusEnum.ACTIVE);
        userAccountService.add(account, false);

        // Create a new test employee
        User testEmployee1 = new User();
        testEmployee1.setUsername("test-employee1");
        testEmployee1.setPassword("password");
        testEmployee1.setFirstName("test-employee1-firstname");
        testEmployee1.setLastName("test-employee1-lastname");
        testEmployee1.setEmail("testemployee1@example.com");
        testEmployee1.setBirthdate("01/01/1970");
        userService.add(testEmployee1, true);

        // Create a new test employee
        User testEmployee2 = new User();
        testEmployee2.setUsername("test-employee2");
        testEmployee2.setPassword("password");
        testEmployee2.setFirstName("test-employee2-firstname");
        testEmployee2.setLastName("test-employee2-lastname");
        testEmployee2.setEmail("testemployee2@example.com");
        testEmployee2.setBirthdate("01/01/1970");
        userService.add(testEmployee2, true);

        // Create a new test user
        User testUser1 = new User();
        testUser1.setUsername("test-user1");
        testUser1.setPassword("password");
        testUser1.setFirstName("test-user1-firstname");
        testUser1.setLastName("test-user1-lastname");
        testUser1.setEmail("testuser1@example.com");
        testUser1.setBirthdate("01/01/1970");
        userService.add(testUser1, false);

        // Create a new test user
        User testUser2 = new User();
        testUser2.setUsername("test-user2");
        testUser2.setPassword("password");
        testUser2.setFirstName("test-user2-firstname");
        testUser2.setLastName("test-user2-lastname");
        testUser2.setEmail("testuser2@example.com");
        testUser2.setBirthdate("01/01/1970");
        userService.add(testUser2, false);

        // Create a new account
        UserAccount testEmployeeAccount1 = new UserAccount();
        testEmployeeAccount1.setAccountType(UserAccount.AccountTypeEnum.CURRENT);
        testEmployeeAccount1.setOwner(testEmployee1);
        testEmployeeAccount1.setAccountBalance(500.00);
        testEmployeeAccount1.setAccountStatus(UserAccount.AccountStatusEnum.ACTIVE);
        testEmployeeAccount1.setLowerLimit(100.00);
        testEmployeeAccount1.setIBAN("USER_ACCOUNT_1_IBAN");
        userAccountService.add(testEmployeeAccount1, false);

        // Create a new account
        UserAccount testEmployeeAccount2 = new UserAccount();
        testEmployeeAccount2.setAccountType(UserAccount.AccountTypeEnum.CURRENT);
        testEmployeeAccount2.setOwner(testEmployee2);
        testEmployeeAccount2.setAccountBalance(500.00);
        testEmployeeAccount2.setAccountStatus(UserAccount.AccountStatusEnum.ACTIVE);
        testEmployeeAccount2.setLowerLimit(100.00);
        testEmployeeAccount2.setIBAN("USER_ACCOUNT_2_IBAN");
        userAccountService.add(testEmployeeAccount2, false);

        // Create a new account
        UserAccount testUserAccount1 = new UserAccount();
        testUserAccount1.setAccountType(UserAccount.AccountTypeEnum.CURRENT);
        testUserAccount1.setOwner(testUser1);
        testUserAccount1.setAccountBalance(500.00);
        testUserAccount1.setAccountStatus(UserAccount.AccountStatusEnum.ACTIVE);
        testUserAccount1.setLowerLimit(100.00);
        testUserAccount1.setIBAN("USER_ACCOUNT_3_IBAN");
        userAccountService.add(testUserAccount1, false);

        // Create a new account
        UserAccount testUserAccount2 = new UserAccount();
        testUserAccount2.setAccountType(UserAccount.AccountTypeEnum.CURRENT);
        testUserAccount2.setOwner(testUser2);
        testUserAccount2.setAccountBalance(500.00);
        testUserAccount2.setAccountStatus(UserAccount.AccountStatusEnum.ACTIVE);
        testUserAccount2.setLowerLimit(100.00);
        testUserAccount2.setIBAN("USER_ACCOUNT_4_IBAN");
        userAccountService.add(testUserAccount2, false);

        // Create a new account
        UserAccount testEmployeeAccount3 = new UserAccount();
        testEmployeeAccount3.setAccountType(UserAccount.AccountTypeEnum.CURRENT);
        testEmployeeAccount3.setOwner(testEmployee1);
        testEmployeeAccount3.setAccountBalance(500.00);
        testEmployeeAccount3.setAccountStatus(UserAccount.AccountStatusEnum.INACTIVE);
        testEmployeeAccount3.setLowerLimit(100.00);
        testEmployeeAccount3.setIBAN("USER_ACCOUNT_5_IBAN");
        userAccountService.add(testEmployeeAccount3, false);

        // Create a new account
        UserAccount testEmployeeAccount4 = new UserAccount();
        testEmployeeAccount4.setAccountType(UserAccount.AccountTypeEnum.CURRENT);
        testEmployeeAccount4.setOwner(testEmployee2);
        testEmployeeAccount4.setAccountBalance(500.00);
        testEmployeeAccount4.setAccountStatus(UserAccount.AccountStatusEnum.INACTIVE);
        testEmployeeAccount4.setLowerLimit(100.00);
        testEmployeeAccount4.setIBAN("USER_ACCOUNT_6_IBAN");
        userAccountService.add(testEmployeeAccount4, false);

        // Create a new account
        UserAccount testEmployeeAccount5 = new UserAccount();
        testEmployeeAccount5.setAccountType(UserAccount.AccountTypeEnum.SAVINGS);
        testEmployeeAccount5.setOwner(testEmployee1);
        testEmployeeAccount5.setAccountBalance(500.00);
        testEmployeeAccount5.setAccountStatus(UserAccount.AccountStatusEnum.ACTIVE);
        testEmployeeAccount5.setLowerLimit(100.00);
        testEmployeeAccount5.setIBAN("USER_ACCOUNT_7_IBAN");
        userAccountService.add(testEmployeeAccount5, false);

        // Create a new account
        UserAccount testEmployeeAccount6 = new UserAccount();
        testEmployeeAccount6.setAccountType(UserAccount.AccountTypeEnum.SAVINGS);
        testEmployeeAccount6.setOwner(testEmployee2);
        testEmployeeAccount6.setAccountBalance(500.00);
        testEmployeeAccount6.setAccountStatus(UserAccount.AccountStatusEnum.ACTIVE);
        testEmployeeAccount6.setLowerLimit(100.00);
        testEmployeeAccount6.setIBAN("USER_ACCOUNT_8_IBAN");
        userAccountService.add(testEmployeeAccount6, false);

        Transaction transaction = new Transaction();
        transaction.setSender(testEmployeeAccount1.getIBAN());
        transaction.setReceiver(testEmployeeAccount2.getIBAN());
        transaction.setAmount(9.95);
        transaction.setDescription("TEST-TRANSACTION");
        transactionService.add(transaction);

        Transaction transaction2 = new Transaction();
        transaction2.setSender(testUserAccount1.getIBAN());
        transaction2.setReceiver(testUserAccount2.getIBAN());
        transaction2.setAmount(9.95);
        transaction2.setDescription("TEST-TRANSACTION");
        transactionService.add(transaction2);
    }
}
