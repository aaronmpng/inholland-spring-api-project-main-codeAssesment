package io.inholland.groep4.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.inholland.groep4.api.TransactionsApi;
import io.inholland.groep4.api.model.DTO.TransactionDTO;
import io.inholland.groep4.api.model.Role;
import io.inholland.groep4.api.model.Transaction;
import io.inholland.groep4.api.model.User;
import io.inholland.groep4.api.model.UserAccount;
import io.inholland.groep4.api.service.TransactionService;
import io.inholland.groep4.api.service.UserAccountService;
import io.inholland.groep4.api.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.threeten.bp.OffsetDateTime;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-31T22:24:07.069Z[GMT]")
@RestController
public class TransactionsApiController implements TransactionsApi {

    private static final Logger log = LoggerFactory.getLogger(TransactionsApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    public TransactionsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE','USER')")
    public ResponseEntity<?> getTransactions() {
        try {
            // Create a empty list for transactions
            List<Transaction> transactions = new ArrayList<>();

            // Check the role of the user
            if (request.isUserInRole("ROLE_EMPLOYEE")) {
                // Get the security information
                Principal principal = request.getUserPrincipal();

                // Get the current user
                User user = userService.findByUsername(principal.getName());

                // User is an employee, getting all transactions
                transactions = transactionService.getAllTransactions();
            } else {
                // Get the security information
                Principal principal = request.getUserPrincipal();

                // Get the current user
                User user = userService.findByUsername(principal.getName());

                // Get the user transactions
                transactions = transactionService.getAllUserTransactions(user);
            }

            return ResponseEntity.status(HttpStatus.OK).body(transactions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE','USER')")
    public ResponseEntity<?> getSpecificTransaction(@Parameter(in = ParameterIn.PATH, description = "The transaction ID", required = true, schema = @Schema()) @PathVariable("id") Long id) {
        try {
            Principal principal = request.getUserPrincipal();
            User user = userService.findByUsername(principal.getName());

            // Check if the user is an employee or transaction owner
            if (request.isUserInRole("ROLE_EMPLOYEE")) {
                return ResponseEntity.status(HttpStatus.OK).body(transactionService.getTransactionById(id));
            } else {
                if (transactionService.checkIfTransactionBelongsToOwner(user, id)){
                    return ResponseEntity.status(HttpStatus.OK).body(transactionService.getTransactionById(id));
                }
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE', 'USER')")
    public ResponseEntity<?> postTransactions(@Parameter(in = ParameterIn.DEFAULT, description = "", schema = @Schema()) @Valid @RequestBody TransactionDTO body) {
        try {
            Principal principal = request.getUserPrincipal();
            User user = userService.findByUsername(principal.getName());
            //System.out.println(user.toString());

            Transaction transaction = new Transaction();

            //check if the user owns an account by the given IBAN, if not, check if the user is an employee, if not set a flag stating the reason for rejecting this transaction
            for (UserAccount account : user.getAccounts()) {
                if ((account.getIBAN().equals(body.getSender())) || (user.getRoles().contains(Role.ROLE_EMPLOYEE))) {
                    transaction.setDescription(body.getDescription());
                    transaction.setAmount(body.getAmount());
                    transaction.setSender(body.getSender());
                    transaction.setReceiver(body.getReceiver());
                    transaction.setDateTime(OffsetDateTime.now());
                    transaction = transactionService.add(transaction);
                    break;
                } else transaction.setRejectionFlag("Error: Sender IBAN does not belong to the given user!");
            }

            if (transaction.getRejectionFlag() != "") {
                System.out.println(transaction.getRejectionFlag());
                return new ResponseEntity<Transaction>(HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<Transaction>(HttpStatus.OK).ok().body(transaction);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
