package io.inholland.groep4.api.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserTest {

    private User createdUser;

    @BeforeEach
    public void setup() {
        createdUser = new User();
    }

    @Test
    public void createUserShouldNotBeNull() {
        createdUser.setRoles(Arrays.asList(Role.ROLE_EMPLOYEE, Role.ROLE_USER));

        assertNotNull(createdUser);
    }

    @Test
    public void setIdShouldSetThatId() {
        Long id = 15L;
        createdUser.setId(id);
        assertEquals(id, createdUser.getId());
    }

    @Test
    public void setFirstNameShouldSetThatFirstName() {
        String firstName = "John";
        createdUser.setFirstName(firstName);
        assertEquals(firstName, createdUser.getFirstName());
    }

    @Test
    public void setLastNameShouldSetThatLastName() {
        String lastName = "Doe";
        createdUser.setLastName(lastName);
        assertEquals(lastName, createdUser.getLastName());
    }

    @Test
    public void setEmailShouldSetThatEmail() {
        String email = "johndoe@example.com";
        createdUser.setEmail(email);
        assertEquals(email, createdUser.getEmail());
    }

    @Test
    public void setUsernameShouldSetThatUsername() {
        String username = "test-employee1";
        createdUser.setUsername(username);
        assertEquals(username, createdUser.getUsername());
    }

    @Test
    public void setPasswordShouldSetThatPassword() {
        String password = "password";
        createdUser.setPassword(password);
        assertEquals(password, createdUser.getPassword());
    }

    @Test
    public void setBirthdateShouldSetThatBirthdate() {
        String birthdate = "01/01/1970";
        createdUser.setBirthdate(birthdate);
        assertEquals(birthdate, createdUser.getBirthdate());
    }

    @Test
    public void setStatusShouldSetThatStatus() {
        User.StatusEnum status = User.StatusEnum.ACTIVE;
        createdUser.setStatus(Collections.singletonList(status));
        assertEquals(Collections.singletonList(status), createdUser.getStatus());
    }

    @Test
    public void setRolesShouldSetThoseRoles() {
        Role[] roles = { Role.ROLE_USER, Role.ROLE_EMPLOYEE };
        createdUser.setRoles(Arrays.asList(roles));
        assertEquals(Arrays.asList(roles), createdUser.getRoles());
    }
}
