package io.inholland.groep4.api.repository;

import io.inholland.groep4.api.model.Transaction;
import io.inholland.groep4.api.model.User;
import io.inholland.groep4.api.model.UserAccount;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Test
    public void should_get_transaction_by_id() {
        // The given transaction...
        Transaction createdTransaction = new Transaction();
        transactionRepository.save(createdTransaction);

        // When
        Transaction requestedTransaction = transactionRepository.getTransactionById(createdTransaction.getId());

        // Then...
        assertThat(requestedTransaction).isEqualTo(createdTransaction);
    }

    @Test
    public void should_find_transaction_by_owner() {
        // Create a sender
        User sender = new User();
        userRepository.save(sender);

        // Create a receiver
        User receiver = new User();
        userRepository.save(receiver);

        // Create an account for the sender
        UserAccount senderAccount = new UserAccount();
        senderAccount.setOwner(sender);
        senderAccount.setIBAN("test-iban-1");
        senderAccount.setAccountBalance(500.00);
        userAccountRepository.save(senderAccount);

        // Create an account for the receiver
        UserAccount receiverAccount = new UserAccount();
        receiverAccount.setOwner(receiver);
        receiverAccount.setIBAN("test-iban-2");
        userAccountRepository.save(receiverAccount);

        // Create the transaction
        Transaction createdTransaction = new Transaction();
        createdTransaction.setOwner(sender);
        createdTransaction.setReceiver(receiverAccount.getIBAN());
        transactionRepository.save(createdTransaction);

        // When
        List<Transaction> transactionList = transactionRepository.getTransactionsByOwner(sender);
        Optional<Transaction> requestedTransaction = transactionList.stream().findFirst();

        // Then...
        requestedTransaction.ifPresent(transaction -> assertThat(transaction).isEqualTo(createdTransaction));
    }

    @Test
    public void should_give_true_when_transactions_exists_by_owner_and_id() {
        // Create a sender
        User sender = new User();
        userRepository.save(sender);

        // Create a receiver
        User receiver = new User();
        userRepository.save(receiver);

        // Create an account for the sender
        UserAccount senderAccount = new UserAccount();
        senderAccount.setOwner(sender);
        senderAccount.setIBAN("test-iban-1");
        senderAccount.setAccountBalance(500.00);
        userAccountRepository.save(senderAccount);

        // Create an account for the receiver
        UserAccount receiverAccount = new UserAccount();
        receiverAccount.setOwner(receiver);
        receiverAccount.setIBAN("test-iban-2");
        userAccountRepository.save(receiverAccount);

        // Create the transaction
        Transaction createdTransaction = new Transaction();
        createdTransaction.setOwner(sender);
        createdTransaction.setReceiver(receiverAccount.getIBAN());
        transactionRepository.save(createdTransaction);

        assertThat(transactionRepository.existsByOwnerAndId(sender, createdTransaction.getId())).isEqualTo(true);
    }
}
