package io.inholland.groep4.api.service;

import io.inholland.groep4.api.model.Role;
import io.inholland.groep4.api.model.User;
import io.inholland.groep4.api.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    public JwtTokenProvider jwtTokenProvider;

    @Test
    public void whenLoggingInCorrectCredentialsShouldGiveToken() {
        String username = "test-employee1";
        String password = "password";

        String token = userService.login(username, password);

        assertThat(jwtTokenProvider.validateToken(token)).isEqualTo(true);
    }

    @Test
    public void whenLoggingInIncorrectCredentialsShouldThrowException() {
        String incorrectUsername = "wrong-username";
        String incorrectPassword = "wrong-password";

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            userService.login(incorrectUsername, incorrectPassword);
        });

        assertTrue(exception.getMessage().contains("Username/password invalid"));
    }

    @Test
    public void creatingANewUserSuccessfullyShouldGiveObject() {
        // Create a new test user
        User createdUser = new User();
        createdUser.setUsername("new-test-user");
        createdUser.setPassword("password");
        createdUser.setFirstName("John");
        createdUser.setLastName("Doe");
        createdUser.setEmail("johndoe@example.com");
        createdUser.setBirthdate("01/01/1970");

        // Add the user
        User response = userService.add(createdUser, false);

        // Assert that the user has been created
        assertEquals(response.getUsername(), createdUser.getUsername());
        assertThat(response.getRoles()).contains(Role.ROLE_USER);
        assertThat(response.getStatus()).contains(User.StatusEnum.ACTIVE);
        assertThat(response.getAccessLevel()).contains(User.AccessLevelEnum.USER);
    }

    @Test
    public void creatingANewEmployeeSuccessfullyShouldGiveUser() {
        // Create a new test employee
        User createdEmployee = new User();
        createdEmployee.setUsername("new-test-employee");
        createdEmployee.setPassword("password");
        createdEmployee.setFirstName("Jane");
        createdEmployee.setLastName("Roe");
        createdEmployee.setEmail("janeroe@example.com");
        createdEmployee.setBirthdate("01/01/1970");

        // Add the employee
        User response = userService.add(createdEmployee, true);

        // Assert that the user has been created
        assertEquals(response.getUsername(), createdEmployee.getUsername());
        assertThat(response.getRoles()).contains(Role.ROLE_USER, Role.ROLE_EMPLOYEE);
        assertThat(response.getStatus()).contains(User.StatusEnum.ACTIVE);
        assertThat(response.getAccessLevel()).contains(User.AccessLevelEnum.USER, User.AccessLevelEnum.EMPLOYEE);
    }

    @Test
    public void creatingAUserWithAnUsernameAlreadyInUseShouldGiveAnException() {
        // Create a new test user
        User createdUser = new User();
        createdUser.setUsername("test-user1");
        createdUser.setPassword("password");
        createdUser.setFirstName("test-user1-firstname");
        createdUser.setLastName("test-user1-lastname");
        createdUser.setEmail("testuser1@example.com");
        createdUser.setBirthdate("01/01/1970");

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            userService.add(createdUser, false);
        });

        assertTrue(exception.getMessage().contains("Username already in use"));
    }

    @Test
    public void updatingAUserSuccessfullyShouldGiveUser() {
        User user = new User();
        user.setUsername("test-user2");
        user.setPassword("password");
        user.setFirstName("test-user2-firstname");
        user.setLastName("test-user2-lastname");
        user.setEmail("testuser2@example.com");
        // Update the birthdate
        user.setBirthdate("01/01/1999");

        // Add the employee
        User response = userService.save(user);

        // Assert that the birthdate has been updated
        assertEquals(response.getUsername(), user.getUsername());
        assertEquals(response.getBirthdate(), user.getBirthdate());
    }

    @Test
    public void updatingAUserThatDoesNotExistShouldGiveAnException() {
        // Create a new test user
        User user = new User();
        user.setUsername("not-existing-user");
        user.setPassword("password");
        user.setFirstName("test-user1-firstname");
        user.setLastName("test-user1-lastname");
        user.setEmail("testuser1@example.com");
        user.setBirthdate("01/01/1970");

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            userService.save(user);
        });

        assertTrue(exception.getMessage().contains("Username not found"));
    }

    @Test
    public void gettingAllUsersShouldGiveListOfUsers() {
        List<User> users = userService.getAllUsers();

        assertThat(users).isNotEmpty();
        assertThat(users.get(0).getUsername()).isEqualTo("test-employee1");
    }

    @Test
    public void findingAUserSuccessfullyByUsernameShouldGiveUser() {
        String usernameToFind = "test-user1";

        User response = userService.findByUsername(usernameToFind);

        assertNotNull(response);
        assertThat(response.getUsername()).isEqualTo("test-user1");
    }

    @Test
    public void findingAUserWithAnInvalidUsernameShouldGiveException() {
        String notExistingUsername = "test-user10";

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            userService.findByUsername(notExistingUsername);
        });

        assertTrue(exception.getMessage().contains("Username not found"));
    }

    @Test
    public void findingAUserSuccessfullyByIdShouldGiveUser() {
        Long idToFind = 5L;

        User response = userService.getSpecificUser(idToFind);

        assertNotNull(response);
        assertThat(response.getUsername()).isEqualTo("test-user2");
    }

    @Test
    public void findingAUserWithInvalidIdShouldGiveException() {
        Long notExistingId = 100L;

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            userService.getSpecificUser(notExistingId);
        });

        assertTrue(exception.getMessage().contains("Id not found"));
    }
}
