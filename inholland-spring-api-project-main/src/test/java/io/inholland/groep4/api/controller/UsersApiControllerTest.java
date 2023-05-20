package io.inholland.groep4.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.inholland.groep4.api.model.User;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UsersApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "test-employee1", password = "password", roles = "EMPLOYEE")
    public void getUsersAsEmployeeShouldReturnMultipleUsers() throws Exception {
        mockMvc.perform(get("/users").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(3))))
                .andExpect(jsonPath("$[0].firstName", is("test-employee1-firstname")))
                .andExpect(jsonPath("$[1].firstName", is("test-employee2-firstname")))
                .andExpect(jsonPath("$[2].firstName", is("test-user1-firstname")));
    }

    @Test
    @WithMockUser(username = "test-user1", password = "password", roles = "USER")
    public void getUsersAsUserShouldReturnOneUser() throws Exception {
        mockMvc.perform(get("/users").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].firstName", is("test-user1-firstname")));
    }

    @Test
    @WithMockUser(username = "test-employee1", password = "password", roles = "EMPLOYEE")
    public void getSpecificUserAsEmployeeShouldReturnUser() throws Exception {
        mockMvc.perform(get("/users/5").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName", is("test-user2-firstname")));
    }

    @Test
    @WithMockUser(username = "test-user1", password = "password", roles = "USER")
    public void gettingYourOwnUserShouldReturnUser() throws Exception {
        mockMvc.perform(get("/users/4").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName", is("test-user1-firstname")));
    }

    @Test
    @WithMockUser(username = "test-user1", password = "password", roles = "USER")
    public void gettingOtherUserWithoutAccessShouldGiveForbidden() throws Exception {
        mockMvc.perform(get("/users/2"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "test-employee1", password = "password", roles = "EMPLOYEE")
    public void gettingAnUnexistingUserShouldGiveNotFound() throws Exception {
        mockMvc.perform(get("/users/999"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("422 UNPROCESSABLE_ENTITY \"Id not found\""));
    }

    @Test
    @WithMockUser(username = "test-user1", password = "password", roles = "USER")
    public void createUserWithoutPermissionShouldGiveForbidden() throws Exception {
        User user = new User();
        user.setUsername("peter");
        user.setPassword("test");
        user.setFirstName("Peter");
        user.setLastName("Griffin");
        user.setEmail("peter@example.com");
        user.setBirthdate("01/01/1970");

        this.mockMvc.perform(post("/users").content(asJsonString(user)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "test-employee1", password = "password", roles = "EMPLOYEE")
    public void createUserWithMissingDataShouldGiveError() throws Exception {
        User user = new User();
        user.setUsername("peter");
        // PROCEED WITH MISSING PASSWORD
        user.setFirstName("Peter");
        user.setLastName("Griffin");
        user.setEmail("peter@example.com");
        user.setBirthdate("01/01/1970");

        this.mockMvc.perform(post("/users").content(asJsonString(user)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("rawPassword cannot be null"));
    }

    @Test
    @WithMockUser(username = "test-employee1", password = "password", roles = "EMPLOYEE")
    public void createUserSuccesfullyShouldGiveIsCreated() throws Exception {
        User user = new User();
        user.setUsername("peter");
        user.setPassword("test");
        user.setFirstName("Peter");
        user.setLastName("Griffin");
        user.setEmail("peter@example.com");
        user.setBirthdate("01/01/1970");

        this.mockMvc.perform(post("/users").content(asJsonString(user)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName", is("Peter")))
                .andExpect(jsonPath("$.lastName", is("Griffin")));
    }

    @Test
    @WithMockUser(username = "test-user1", password = "password", roles = "USER")
    public void updateUserWithoutPermissionShouldGiveForbidden() throws Exception {
        User user = new User();
        user.setUsername("peter");
        user.setPassword("test");
        user.setFirstName("Peter");
        user.setLastName("Griffin");
        user.setEmail("peter@example.com");
        user.setBirthdate("01/01/1970");

        this.mockMvc.perform(put("/users/2").content(asJsonString(user)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "test-employee2", password = "password", roles = "EMPLOYEE")
    public void updateUserSuccesfullyShouldGiveOk() throws Exception {
        User user = new User();
        user.setUsername("test-user2");
        user.setPassword("password");
        user.setFirstName("test-user2-firstname");
        user.setLastName("test-user2-lastname");
        user.setEmail("testuser2@email.com");
        user.setBirthdate("01/01/1970");

        this.mockMvc.perform(put("/users/5").content(asJsonString(user)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName", is("test-user2-firstname")))
                .andExpect(jsonPath("$.lastName", is("test-user2-lastname")))
                .andExpect(jsonPath("$.email", is("testuser2@email.com")));
    }

    @Test
    @WithMockUser(username = "test-employee1", password = "password", roles = "EMPLOYEE")
    public void updateUserThatDoesntExistShouldGiveError() throws Exception {
        User user = new User();
        user.setUsername("not-existing-user");
        user.setPassword("test");
        user.setFirstName("Not");
        user.setLastName("Existing");
        user.setEmail("notexisting@email.com");
        user.setBirthdate("01/01/1970");

        this.mockMvc.perform(put("/users/2").content(asJsonString(user)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("422 UNPROCESSABLE_ENTITY \"Username not found\""));
    }


    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
