package io.inholland.groep4.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.inholland.groep4.api.model.User;
import io.inholland.groep4.api.model.UserAccount;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountsApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "test-employee1", password = "password", roles = "EMPLOYEE")
    public void getAccountSuccessfullyAsEmployeeShouldGiveMultipleAccounts() throws Exception {
        this.mockMvc.perform(get("/accounts"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(5))))
                .andExpect(jsonPath("$[0].IBAN", is("NL01INHO0000000001")))
                .andExpect(jsonPath("$[1].IBAN", is("USER_ACCOUNT_1_IBAN")))
                .andExpect(jsonPath("$[2].IBAN", is("USER_ACCOUNT_2_IBAN")));
    }

    @Test
    @WithMockUser(username = "test-user1", password = "password", roles = "USER")
    public void getAccountSuccessfullyAsUserShouldGiveObject() throws Exception {
        this.mockMvc.perform(get("/accounts"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].IBAN", is("USER_ACCOUNT_3_IBAN")));

    }

    @Test
    @WithMockUser(username = "test-user1", password = "password", roles = "USER")
    public void getSpecificAccountAsUserWithoutPrivilegeShouldGiveBadrequest() throws Exception {
        this.mockMvc.perform(get("/accounts/9"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string("422 UNPROCESSABLE_ENTITY \"Account does not belong to owner\""));
    }

    @Test
    @WithMockUser(username = "test-employee1", password = "password", roles = "EMPLOYEE")
    public void gettingNotExistingAccountShouldGiveBadrequest() throws Exception {
        mockMvc.perform(get("/accounts/999"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("422 UNPROCESSABLE_ENTITY \"Id not found\""));
    }

    @Test
    @WithMockUser(username = "test-employee1", password = "password", roles = "EMPLOYEE")
    public void getSpecificAccountShouldGiveOk() throws Exception {
        this.mockMvc.perform(get("/accounts/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.IBAN", is("NL01INHO0000000001")))
                .andExpect(jsonPath("$.accountBalance", is(500.00)));
    }

    @Test
    @WithMockUser(username = "test-employee1", password = "password", roles = "EMPLOYEE")
    public void createAccountSuccessfullyShouldGiveObject() throws Exception {
        User user = new User();
        user.setUsername("peter");
        user.setPassword("test");
        user.setFirstName("Peter");
        user.setLastName("Griffin");
        user.setEmail("peter@example.com");
        user.setBirthdate("01/01/1970");

        UserAccount userAccount = new UserAccount();
        userAccount.setAccountType(UserAccount.AccountTypeEnum.CURRENT);
        userAccount.setOwner(user);
        userAccount.setAccountBalance(0.00);
        userAccount.setAccountStatus(UserAccount.AccountStatusEnum.ACTIVE);
        userAccount.setLowerLimit(100.00);

        this.mockMvc.perform(post("/accounts").content(asJsonString(userAccount)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountStatus", is("active")))
                .andExpect(jsonPath("$.accountBalance", is(0.00)))
                .andExpect(jsonPath("$.lowerLimit", is(100.00)));
    }

    @Test
    @WithMockUser(username = "test-employee1", password = "password", roles = "EMPLOYEE")
    public void updateAccountSuccesfullyShouldGiveObject() throws Exception {
        UserAccount account = new UserAccount();
        account.setLowerLimit(100.0);
        account.setIBAN("NL01INHO0000000001");
        account.setAccountBalance(500.00);

        this.mockMvc.perform(put("/accounts/1").content(asJsonString(account)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.lowerLimit", is(100.00)));
    }

    @Test
    @WithMockUser(username = "test-employee1", password = "password", roles = "EMPLOYEE")
    public void updateAccountThatDoesntExistShouldGiveError() throws Exception {
        UserAccount account = new UserAccount();
        account.setAccountType(UserAccount.AccountTypeEnum.CURRENT);
        account.setIBAN("NL01INHO0000001");
        User user = new User();
        account.setOwner(user);
        account.setAccountBalance(2.0);
        account.setLowerLimit(1.0);
        account.setAccountStatus(UserAccount.AccountStatusEnum.ACTIVE);

        this.mockMvc.perform(put("/accounts/99").content(asJsonString(account)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}