package io.inholland.groep4.api;

import io.inholland.groep4.api.model.DTO.UserDTO;
import io.inholland.groep4.api.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-31T22:24:07.069Z[GMT]")
@Validated
public interface UsersApi {

    @Operation(summary = "Get specific user", description = "If admin, able to show other users. If user, only able to show your details.", security = {
            @SecurityRequirement(name = "bearerAuth")}, tags = {"Employees", "Customers"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Getting specific user successful", content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Bad input parameter(s)"),
            @ApiResponse(responseCode = "401", description = "JWT Bearer Token is missing or invalid"),
            @ApiResponse(responseCode = "403", description = "You are forbidden to view this content"),
            @ApiResponse(responseCode = "404", description = "Item not found")})
    @RequestMapping(value = "/users/{id}",
            produces = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<?> getSpecificUser(@Parameter(in = ParameterIn.PATH, description = "The user ID", required = true, schema = @Schema()) @PathVariable("id") Long id);

    @Operation(summary = "Get users", description = "If admin, show all users. If user, only show your own details.", security = {
            @SecurityRequirement(name = "bearerAuth")}, tags = {"Employees", "Customers"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Getting users successful", content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Bad input parameter(s)"),
            @ApiResponse(responseCode = "401", description = "JWT Bearer Token is missing or invalid"),
            @ApiResponse(responseCode = "403", description = "You are forbidden to view this content"),
            @ApiResponse(responseCode = "404", description = "Item not found")})
    @RequestMapping(value = "/users",
            produces = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<?> getUsers();

    @Operation(summary = "Create user", description = "Create a new user", security = {
            @SecurityRequirement(name = "bearerAuth")}, tags = {"Employees"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad input parameter(s)"),
            @ApiResponse(responseCode = "401", description = "JWT Bearer Token is missing or invalid"),
            @ApiResponse(responseCode = "403", description = "You are forbidden to view this content"),
            @ApiResponse(responseCode = "404", description = "Item not found")})
    @RequestMapping(value = "/users",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.POST)
    ResponseEntity<?> postUser(@Parameter(in = ParameterIn.DEFAULT, description = "", schema = @Schema()) @Valid @RequestBody UserDTO body);

    @Operation(summary = "Update user", description = "Update a user by using a userID", security = {
            @SecurityRequirement(name = "bearerAuth")}, tags = {"Employees"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Edit user successful", content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "JWT Bearer Token is missing or invalid"),
            @ApiResponse(responseCode = "404", description = "Item not found")})
    @RequestMapping(value = "/users/{id}",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.PUT)
    ResponseEntity<?> updateUser(@Parameter(in = ParameterIn.PATH, description = "The user ID", required = true, schema = @Schema()) @PathVariable("id") Long id, @Parameter(in = ParameterIn.DEFAULT, description = "", schema = @Schema()) @Valid @RequestBody User body);
}

