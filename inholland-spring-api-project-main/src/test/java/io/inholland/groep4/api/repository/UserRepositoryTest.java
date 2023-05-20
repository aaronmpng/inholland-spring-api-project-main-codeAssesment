package io.inholland.groep4.api.repository;

import io.inholland.groep4.api.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void should_find_user_by_username() {
        // The given user...
        User createdUser = new User();
        createdUser.setUsername("test user");
        userRepository.save(createdUser);

        // When
        User requestedUser = userRepository.findByUsername("test user");

        // Then...
        assertThat(requestedUser.getUsername()).isEqualTo(createdUser.getUsername());
    }

    @Test
    public void should_get_user_by_id() {
        // The given user...
        User createdUser = new User();
        createdUser.setId(20L);
        userRepository.save(createdUser);

        // When
        Optional<User> foundUser = userRepository.findById(20L);

        // Then...
        foundUser.ifPresent(user -> assertThat(user).isEqualTo(createdUser));
    }
}
