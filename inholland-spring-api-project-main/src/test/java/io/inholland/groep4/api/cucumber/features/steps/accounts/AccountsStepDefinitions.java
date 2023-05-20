package io.inholland.groep4.api.cucumber.features.steps.accounts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.java8.En;
import io.inholland.groep4.api.cucumber.features.steps.BaseStepDefinitions;
import io.inholland.groep4.api.model.DTO.UserAccountDTO;
import io.inholland.groep4.api.model.DTO.UserDTO;
import io.inholland.groep4.api.model.UserAccount;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountsStepDefinitions extends BaseStepDefinitions implements En {

    private static final String VALID_TOKEN_USER = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0LXVzZXIxIiwiYXV0aCI6W3siYXV0aG9yaXR5IjoiUk9MRV9VU0VSIn1dLCJpYXQiOjE2NTM0MTk1NDEsImV4cCI6MTY4NDk3NjQ5M30.EwhnHxzCVH-mDeoaKgnhsgx4RQ2BgvzCnRFqPoIKV4o";
    private static final String VALID_TOKEN_EMPLOYEE = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0LWVtcGxveWVlMSIsImF1dGgiOlt7ImF1dGhvcml0eSI6IlJPTEVfRU1QTE9ZRUUifSx7ImF1dGhvcml0eSI6IlJPTEVfVVNFUiJ9XSwiaWF0IjoxNjUzNDE5NTE2LCJleHAiOjE2ODQ5NzY0Njh9.wz_6cKBM9mmAYmv2FVGGj8UvsL1mXXNMWnwtAMXp-fE";
    private static final String EXPIRED_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0LWVtcGxveWVlMSIsImF1dGgiOlt7ImF1dGhvcml0eSI6IlJPTEVfVVNFUiJ9XSwiaWF0IjoxNjUzMjMxMzYzLCJleHAiOjE2NTMyMzQ5NjN9.eDIOKqTxayyyVP1QLOCC2QwJXrMg1M8EM0gMaVP1P64";
    private static final String INVALID_TOKEN = "invalid";

    private final HttpHeaders httpHeaders = new HttpHeaders();
    private final TestRestTemplate restTemplate = new TestRestTemplate();

    private final ObjectMapper mapper = new ObjectMapper();

    private ResponseEntity<String> response;
    private HttpEntity<String> request;

    private Integer status;
    private UserAccountDTO userAccountDTO;

    private String token;

    public AccountsStepDefinitions() {
        When("^I call the accounts endpoint$", () -> {
            httpHeaders.clear();
            httpHeaders.add("Authorization", "Bearer " + token);
            request = new HttpEntity<>(null, httpHeaders);
            response = restTemplate.exchange(getBaseUrl() + "/accounts", HttpMethod.GET, new HttpEntity<>(null, httpHeaders), String.class);
            status = response.getStatusCodeValue();
        });

        When("^I call the accounts endpoint with accountid (\\d+)$", (Integer accountID) -> {
            httpHeaders.clear();
            httpHeaders.add("Authorization", "Bearer " + token);
            request = new HttpEntity<>(null, httpHeaders);
            response = restTemplate.exchange(getBaseUrl() + "/accounts/" + accountID, HttpMethod.GET, new HttpEntity<>(null, httpHeaders), String.class);
            status = response.getStatusCodeValue();
        });

        When("^I make a post request to the accounts endpoint$", () -> {
            httpHeaders.clear();
            httpHeaders.add("Authorization", "Bearer " + token);
            httpHeaders.add("Content-Type", "application/json");
            request = new HttpEntity<>(mapper.writeValueAsString(userAccountDTO), httpHeaders);
            response = restTemplate.postForEntity(getBaseUrl() + "/accounts", request, String.class);
            status = response.getStatusCodeValue();
        });

        Given("^I have a valid \"([^\"]*)\" token$", (String role) -> {
            switch (role) {
                case "user":
                    token = VALID_TOKEN_USER;
                    break;
                case "employee":
                    token = VALID_TOKEN_EMPLOYEE;
                    break;
                default:
                    throw new IllegalArgumentException("No such role");
            }
        });

        Given("^I have an \"([^\"]*)\" bearertoken$", (String type) -> {
            switch (type) {
                case "invalid":
                    token = INVALID_TOKEN;
                    break;
                case "expired":
                    token = EXPIRED_TOKEN;
                    break;
                default:
                    throw new IllegalArgumentException("No such type");
            }
        });

        And("^I have a valid account object with \"([^\"]*)\" as type$", (String type) -> {
            userAccountDTO = new UserAccountDTO();
            switch (type) {
                case "savings":
                    userAccountDTO.setAccountType(UserAccount.AccountTypeEnum.CURRENT);
                    break;
                case "current":
                    userAccountDTO.setAccountType(UserAccount.AccountTypeEnum.SAVINGS);
                    break;
                default:
                    throw new IllegalArgumentException("No such type");
            }
        });

        And("^I have an invalid account object$", () -> {
            userAccountDTO = new UserAccountDTO();
        });

        Then("^the response is a (\\d+) statuscode$", (Integer statusCode) -> {
            assertEquals(status, response.getStatusCodeValue());
        });

        Then("^the result is a list of (\\d+) accounts$", (Integer size) -> {
            int actual = JsonPath.read(response.getBody(), "$.size()");
            assertEquals(size, actual);
        });
    }
}
