package io.inholland.groep4.api.controller;

import io.inholland.groep4.api.AuthenticateApi;
import io.inholland.groep4.api.model.DTO.LoginDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.inholland.groep4.api.model.DTO.LoginResponseDTO;
import io.inholland.groep4.api.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-24T18:28:14.004Z[GMT]")
@RestController
public class AuthenticateApiController implements AuthenticateApi {

    private static final Logger log = LoggerFactory.getLogger(AuthenticateApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private UserService userService;

    @org.springframework.beans.factory.annotation.Autowired
    public AuthenticateApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<?> authenticate(@Parameter(in = ParameterIn.DEFAULT, description = "", schema = @Schema()) @Valid @RequestBody LoginDTO body) {
        try {
            String token = userService.login(body.getUsername(), body.getPassword());
            LoginResponseDTO loginResponse = new LoginResponseDTO();
            loginResponse.setToken(token);

            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
