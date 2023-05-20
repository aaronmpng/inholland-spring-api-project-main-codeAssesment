package io.inholland.groep4.api.controller;

import io.inholland.groep4.api.UsersApi;
import io.inholland.groep4.api.model.DTO.UserDTO;
import io.inholland.groep4.api.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-31T22:24:07.069Z[GMT]")
@RestController
public class UsersApiController implements UsersApi {

    private static final Logger log = LoggerFactory.getLogger(UsersApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private UserService userService;

    @Autowired
    public UsersApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE','USER')")
    public ResponseEntity<?> getUsers() {
        try {
            // Create a empty list for users
            List<User> users = new ArrayList<>();

            // Check the role of the user
            if (request.isUserInRole("ROLE_EMPLOYEE")) {
                // User is an employee, getting all users
                users = userService.getAllUsers();
            } else {
                // Get the security information
                Principal principal = request.getUserPrincipal();

                // Get the current user
                User user = userService.findByUsername(principal.getName());

                // Add the user to the list
                users.add(user);
            }

            return ResponseEntity.status(HttpStatus.OK).body(users);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE','USER')")
    public ResponseEntity<?> getSpecificUser(@Parameter(in = ParameterIn.PATH, description = "The user ID", required = true, schema = @Schema()) @PathVariable("id") Long id) {
        try {
            Principal principal = request.getUserPrincipal();
            User user = userService.findByUsername(principal.getName());

            // Check if the user is requesting their own information
            if (user.getId() == id) {
                return ResponseEntity.status(HttpStatus.OK).body(user);
            } else {
                if (request.isUserInRole("ROLE_EMPLOYEE")) {
                    User userQuery = userService.getSpecificUser(id);

                    return ResponseEntity.status(HttpStatus.OK).body(userQuery);
                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<?> postUser(@Parameter(in = ParameterIn.DEFAULT, description = "", schema = @Schema()) @Valid @RequestBody UserDTO body) {
        try {
            User user = new User();
            user.setUsername(body.getUsername());
            user.setPassword(body.getPassword());
            user.setFirstName(body.getFirstName());
            user.setLastName(body.getLastName());
            user.setEmail(body.getEmail());
            user.setBirthdate(body.getBirthdate());

            User result = userService.add(user, false);
            return ResponseEntity.status(201).body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<?> updateUser(@Parameter(in = ParameterIn.PATH, description = "The user ID", required = true, schema = @Schema()) @PathVariable("id") Long id, @Parameter(in = ParameterIn.DEFAULT, description = "", schema = @Schema()) @Valid @RequestBody User body) {
        try {
            body.setId(id);
            User result = userService.save(body);
            return ResponseEntity.status(200).body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
