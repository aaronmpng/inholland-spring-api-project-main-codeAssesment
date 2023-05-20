package io.inholland.groep4.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.inholland.groep4.api.model.DTO.LoginDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticateApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void authenticateWithCorrectCredentialsShouldGiveToken() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("test-employee1");
        loginDTO.setPassword("password");

        this.mockMvc.perform(post("/authenticate")
                        .content(asJsonString(loginDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    public void authenticateWithIncorrectCredentialsShouldGiveError() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("incorrect-username");
        loginDTO.setPassword("incorrect-password");

        this.mockMvc.perform(post("/authenticate")
                        .content(asJsonString(loginDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("422 UNPROCESSABLE_ENTITY \"Username/password invalid\""));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}