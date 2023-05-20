package io.inholland.groep4.api.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.threeten.bp.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TransactionTest {

    private Transaction createdTransaction;
    private User createdUser;

    @BeforeEach
    public void setup() {
        createdUser = new User();
        createdUser.setUsername("test-employee1");
        createdUser.setPassword("password");
        createdUser.setFirstName("John");
        createdUser.setLastName("Doe");
        createdUser.setEmail("johndoe@example.com");
        createdUser.setBirthdate("01/01/1970");

        createdTransaction = new Transaction();
    }

    @Test
    public void createTransactionShouldNotBeNull() {
        assertNotNull(createdTransaction);
    }

    @Test
    public void setIdShouldSetThatId() {
        Long id = 15L;
        createdTransaction.setId(id);
        assertEquals(id, createdTransaction.getId());
    }

    @Test
    public void setOwnerShouldSetThatOwner() {
        createdTransaction.setOwner(createdUser);
        assertEquals(createdUser, createdTransaction.getOwner());
    }

    @Test
    public void setReceiverShouldSetThatReceiver() {
        String IBAN = "NL01INHO0000000001";
        createdTransaction.setReceiver(IBAN);
        assertEquals(IBAN, createdTransaction.getReceiver());
    }

    @Test
    public void setAmountShouldSetThatAmount() {
        Double amount = 100.00;
        createdTransaction.setAmount(amount);
        assertEquals(amount, createdTransaction.getAmount());
    }

    @Test
    public void setDescriptionShouldSetThatDescription() {
        String description = "Description";
        createdTransaction.setDescription(description);
        assertEquals(description, createdTransaction.getDescription());
    }

    @Test
    public void setRejectionFlagShouldSetThatRejectionFlag() {
        String rejectionFlag = "Rejected for reason";
        createdTransaction.setRejectionFlag(rejectionFlag);
        assertEquals(rejectionFlag, createdTransaction.getRejectionFlag());
    }

    @Test
    public void setDateTimeShouldSetThatDateTime() {
        OffsetDateTime dateTime = OffsetDateTime.now();
        createdTransaction.setDateTime(dateTime);
        assertEquals(dateTime, createdTransaction.getDateTime());
    }
}
