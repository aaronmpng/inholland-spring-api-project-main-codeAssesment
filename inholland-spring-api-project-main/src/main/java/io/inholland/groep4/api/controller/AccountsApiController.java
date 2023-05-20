package io.inholland.groep4.api.controller;
import io.inholland.groep4.api.AccountsApi;
import io.inholland.groep4.api.model.DTO.UserAccountDTO;
import io.inholland.groep4.api.model.UserAccount;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;


@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-31T22:24:07.069Z[GMT]")
@RestController
public class AccountsApiController implements AccountsApi {

    private static final Logger log = LoggerFactory.getLogger(AccountsApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private UserService userService;

    @Autowired
    public AccountsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE','USER')")
    public ResponseEntity<?> getAccounts() {
        Optional<List<UserAccount>> account = userAccountService.getAllAccounts();

        return new ResponseEntity<Optional<List<UserAccount>>>(account, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE','USER')")
    public ResponseEntity<?> getSpecificAccount(@Parameter(in = ParameterIn.PATH, description = "The account ID", required = true, schema = @Schema()) @PathVariable("id") Long id) {
        UserAccount account = userAccountService.getAccountById(id);

        return new ResponseEntity<UserAccount>(account, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<?> postAccount(@Parameter(in = ParameterIn.DEFAULT, description = "", schema = @Schema()) @Valid @RequestBody UserAccountDTO body) {
        UserAccount account = userAccountService.setAccount(body);
        userAccountService.add(account, true);
        return new ResponseEntity<UserAccountDTO>(body, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<UserAccount> updateAccount(@Parameter(in = ParameterIn.PATH, description = "The account ID", required = true, schema = @Schema()) @PathVariable("id") Long id, @Parameter(in = ParameterIn.DEFAULT, description = "", schema = @Schema()) @Valid @RequestBody UserAccount body) {
        body.setId(id);
        UserAccount result = userAccountService.save(body);
        return new ResponseEntity<UserAccount>(body, HttpStatus.OK);
    }
}
