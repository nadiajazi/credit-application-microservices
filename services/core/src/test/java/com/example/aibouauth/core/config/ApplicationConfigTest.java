package com.example.aibouauth.core.config;

import com.example.aibouauth.core.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class ApplicationConfigTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationConfiguration authenticationConfiguration;

    @InjectMocks
    private ApplicationConfig applicationConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        applicationConfig = new ApplicationConfig(userRepository);
    }

    @Test
    void userDetailsService_shouldReturnUserDetailsService() {

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());


        UserDetailsService userDetailsService = applicationConfig.userDetailsService();


        assertNotNull(userDetailsService);
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("test@example.com"));
        verify(userRepository).findUserByEmail("test@example.com");
    }

    @Test
    void authenticationProvider_shouldReturnDaoAuthenticationProvider() {

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());


        AuthenticationProvider authenticationProvider = applicationConfig.authenticationProvider();


        assertNotNull(authenticationProvider);
        assertEquals(DaoAuthenticationProvider.class, authenticationProvider.getClass());
    }

    @Test
    void authenticationManager_shouldReturnAuthenticationManager() throws Exception {

        AuthenticationManager mockAuthenticationManager = mock(AuthenticationManager.class);
        when(authenticationConfiguration.getAuthenticationManager()).thenReturn(mockAuthenticationManager);


        AuthenticationManager authenticationManager = applicationConfig.authenticationManager(authenticationConfiguration);


        assertNotNull(authenticationManager);
        assertEquals(mockAuthenticationManager, authenticationManager);
        verify(authenticationConfiguration).getAuthenticationManager();
    }

    @Test
    void passwordEncoder_shouldReturnBCryptPasswordEncoder() {

        PasswordEncoder passwordEncoder = applicationConfig.passwordEncoder();


        assertNotNull(passwordEncoder);
        assertEquals(BCryptPasswordEncoder.class, passwordEncoder.getClass());
    }
}
