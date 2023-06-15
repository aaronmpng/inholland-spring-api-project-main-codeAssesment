package io.inholland.groep4.api.controller;
import io.inholland.groep4.api.AccountsApi;
import io.inholland.groep4.api.model.DTO.UserAccountDTO;
import io.inholland.groep4.api.model.User;
import io.inholland.groep4.api.model.UserAccount;
import io.inholland.groep4.api.service.UserAccountService;
import io.inholland.groep4.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-31T22:24:07.069Z[GMT]")
@RestController
@RequestMapping(value = "/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountsApiController implements AccountsApi {

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private UserService userService;

    @Autowired
    private final HttpServletRequest request;

    @Autowired
    public AccountsApiController(HttpServletRequest request) {
        this.request = request;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('EMPLOYEE','USER')")
    public ResponseEntity getAccounts() {
        User user = userService.findByUsername(request.getUserPrincipal().getName());
        boolean isEmployee = request.isUserInRole("ROLE_EMPLOYEE");
        List<UserAccount> account = userAccountService.getAllAccounts(user, isEmployee);
        return new ResponseEntity<List<UserAccount>>(account, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE','USER')")
    @GetMapping("/accounts/{id}")
    public ResponseEntity<UserAccount> getSpecificAccount(@PathVariable("id") Long id) {
        User user = userService.findByUsername(request.getUserPrincipal().getName());
        boolean isEmployee = request.isUserInRole("ROLE_EMPLOYEE");
        UserAccount account = userAccountService.getAccountById(id, user, isEmployee);
        return new ResponseEntity<UserAccount>(account, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @PostMapping("/accounts")
    public ResponseEntity<UserAccountDTO> postAccount(@RequestBody UserAccountDTO body) {
        UserAccount account = userAccountService.setAccount(body);
        userAccountService.add(account, true);
        return new ResponseEntity<UserAccountDTO>(body, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @PutMapping("/accounts/{id}")
    public ResponseEntity<UserAccount> updateAccount(@PathVariable("id") Long id, @Valid @RequestBody UserAccount body) {
        body.setId(id);
        UserAccount result = userAccountService.save(body);
        return new ResponseEntity<UserAccount>(body, HttpStatus.OK);
    }
}
