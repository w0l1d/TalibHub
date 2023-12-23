package org.ilisi.backend.repository;

import lombok.extern.slf4j.Slf4j;
import org.ilisi.backend.model.Manager;
import org.ilisi.backend.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Slf4j
class UserRepositoryTests {

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findByEmailReturnsUserWhenUserExists() {
        User user = new Manager();
        user.setEmail("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        Optional<User> returnedUser = userRepository.findByEmail("test@example.com");

        assertTrue(returnedUser.isPresent());
        assertEquals("test@example.com", returnedUser.get().getEmail());
    }

    @Test
    void findByEmailReturnsEmptyWhenUserDoesNotExist() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        Optional<User> returnedUser = userRepository.findByEmail("test@example.com");

        assertFalse(returnedUser.isPresent());
    }

    @Test
    void loadUserByUsernameReturnsUserDetailsWhenUserExists() {
        User user = new Manager();
        user.setEmail("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(userRepository.loadUserByUsername("test@example.com")).thenCallRealMethod();

        UserDetails returnedUserDetails = userRepository.loadUserByUsername("test@example.com");

        assertEquals("test@example.com", returnedUserDetails.getUsername());
    }

    @Test
    void loadUserByUsernameThrowsExceptionWhenUserDoesNotExist() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(userRepository.loadUserByUsername("test@example.com")).thenCallRealMethod();

        assertThrows(UsernameNotFoundException.class, () -> userRepository.loadUserByUsername("test@example.com"));
    }
}