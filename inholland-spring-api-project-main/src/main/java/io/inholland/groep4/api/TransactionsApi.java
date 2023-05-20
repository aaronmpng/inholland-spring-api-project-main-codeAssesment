package io.inholland.groep4.api;

import io.inholland.groep4.api.model.DTO.TransactionDTO;
import io.inholland.groep4.api.model.Transaction;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
public interface TransactionsApi {

    @Operation(summary = "Get specific transaction", description = "If admin, able to show other transactions than yours. If user, only able to show your own transactions.", security = {
            @SecurityRequirement(name = "bearerAuth")}, tags = {"Customers", "Employees"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Getting transaction successful", content = @Content(schema = @Schema(implementation = Transaction.class))),
            @ApiResponse(responseCode = "400", description = "Bad input parameter(s)"),
            @ApiResponse(responseCode = "401", description = "JWT Bearer Token is missing or invalid"),
            @ApiResponse(responseCode = "403", description = "You are forbidden to view this content"),
            @ApiResponse(responseCode = "404", description = "Item not found")})
    @RequestMapping(value = "/transactions/{id}",
            produces = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<?> getSpecificTransaction(@Parameter(in = ParameterIn.PATH, description = "The transaction ID", required = true, schema = @Schema()) @PathVariable("id") Long id);

    @Operation(summary = "Get transactions", description = "If admin, show all transactions. If user, show only your own transactions.", security = {
            @SecurityRequirement(name = "bearerAuth")}, tags = {"Customers", "Employees"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Getting transactions successful", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Transaction.class)))),
            @ApiResponse(responseCode = "400", description = "Bad input parameter(s)"),
            @ApiResponse(responseCode = "401", description = "JWT Bearer Token is missing or invalid"),
            @ApiResponse(responseCode = "403", description = "You are forbidden to view this content"),
            @ApiResponse(responseCode = "404", description = "Item not found")})
    @RequestMapping(value = "/transactions",
            produces = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<?> getTransactions();

    @Operation(summary = "Create transaction", description = "Create a new transaction", security = {
            @SecurityRequirement(name = "bearerAuth")}, tags = {"Customers", "Employees"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Creating transactions successful", content = @Content(schema = @Schema(implementation = Transaction.class))),
            @ApiResponse(responseCode = "400", description = "Bad input parameter(s)"),
            @ApiResponse(responseCode = "401", description = "JWT Bearer Token is missing or invalid"),
            @ApiResponse(responseCode = "403", description = "You are forbidden to view this content"),
            @ApiResponse(responseCode = "404", description = "Item not found")})
    @RequestMapping(value = "/transactions",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.POST)
    ResponseEntity<?> postTransactions(@Parameter(in = ParameterIn.DEFAULT, description = "", schema = @Schema()) @Valid @RequestBody TransactionDTO body);
}

