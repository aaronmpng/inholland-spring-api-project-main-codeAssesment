package io.inholland.groep4.api.cucumber.features.steps.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.java8.En;
import io.inholland.groep4.api.cucumber.features.steps.BaseStepDefinitions;
import io.inholland.groep4.api.model.DTO.UserDTO;
import org.json.JSONObject;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UsersStepDefinitions extends BaseStepDefinitions implements En {

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
    private UserDTO userDTO;

    private String token;

    public UsersStepDefinitions() {
        When("^I call the user endpoint$", () -> {
            httpHeaders.clear();
            httpHeaders.add("Authorization", "Bearer " + token);
            request = new HttpEntity<>(null, httpHeaders);
            response = restTemplate.exchange(getBaseUrl() + "/users", HttpMethod.GET, new HttpEntity<>(null, httpHeaders), String.class);
            status = response.getStatusCodeValue();
        });

        When("^I call the user endpoint with userid (\\d+)$", (Integer userId) -> {
            httpHeaders.clear();
            httpHeaders.add("Authorization", "Bearer " + token);
            request = new HttpEntity<>(null, httpHeaders);
            response = restTemplate.exchange(getBaseUrl() + "/users/" + userId, HttpMethod.GET, new HttpEntity<>(null, httpHeaders), String.class);
            status = response.getStatusCodeValue();
        });

        When("^I make a post request to the user endpoint$", () -> {
            httpHeaders.clear();
            httpHeaders.add("Authorization", "Bearer " + token);
            httpHeaders.add("Content-Type", "application/json");
            request = new HttpEntity<>(mapper.writeValueAsString(userDTO), httpHeaders);
            response = restTemplate.postForEntity(getBaseUrl() + "/users", request, String.class);
            status = response.getStatusCodeValue();
        });

        Then("^the result is a status of (\\d+)$", (Integer code) -> {
            assertEquals(code, status);
        });

        Given("^I have a valid token for role \"([^\"]*)\"$", (String role) -> {
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

        Given("^I have an \"([^\"]*)\" token", (String type) -> {
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

        And("^I have a valid user object with username \"([^\"]*)\" and password \"([^\"]*)\"", (String username, String password) -> {
            userDTO = new UserDTO();
            userDTO.setUsername(username);
            userDTO.setPassword(password);
        });

        And("^I have an invalid user object with username \"([^\"]*)\"", (String username) -> {
            userDTO = new UserDTO();
            userDTO.setUsername(username);
        });

        Then("^the result is a list of users of size (\\d+)$", (Integer size) -> {
            int actual = JsonPath.read(response.getBody(), "$.size()");
            assertEquals(size, actual);
        });

        And("^I validate the user object has an id greater than (\\d+)$", (Integer expected) -> {
            JSONObject jsonObject = new JSONObject(response.getBody());
            int actual = jsonObject.getInt("id");
            assertTrue(actual > expected);
        });
    }
}
