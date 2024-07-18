package com.example.aibouauth.core.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Test findUserByEmail returns user when email exists")
    public void testFindUserByEmail_UserExists() {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        userRepository.save(user);

        // Act
        Optional<User> foundUser = userRepository.findUserByEmail("test@example.com");

        // Assert
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("test@example.com");
        assertThat(foundUser.get().getFirstName()).isEqualTo("John");
        assertThat(foundUser.get().getLastName()).isEqualTo("Doe");
    }

    @Test
    @DisplayName("Test findUserByEmail returns empty when email does not exist")
    public void testFindUserByEmail_UserDoesNotExist() {
        // Act
        Optional<User> foundUser = userRepository.findUserByEmail("nonexistent@example.com");

        // Assert
        assertThat(foundUser).isNotPresent();
    }

    @Test
    @DisplayName("Test findUserById returns user when id exists")
    public void testFindUserById_UserExists() {
        // Arrange
        User user = new User();
        user.setEmail("test2@example.com");
        user.setFirstName("Jane");
        user.setLastName("Doe");
        User savedUser = userRepository.save(user);

        // Act
        Optional<User> foundUser = userRepository.findUserById(savedUser.getId());

        // Assert
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getId()).isEqualTo(savedUser.getId());
        assertThat(foundUser.get().getEmail()).isEqualTo("test2@example.com");
        assertThat(foundUser.get().getFirstName()).isEqualTo("Jane");
        assertThat(foundUser.get().getLastName()).isEqualTo("Doe");
    }

    @Test
    @DisplayName("Test findUserById returns empty when id does not exist")
    public void testFindUserById_UserDoesNotExist() {
        // Act
        Optional<User> foundUser = userRepository.findUserById(999);

        // Assert
        assertThat(foundUser).isNotPresent();
    }
}
