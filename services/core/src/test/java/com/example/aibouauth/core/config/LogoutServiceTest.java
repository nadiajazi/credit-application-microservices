package com.example.aibouauth.core.config;

import com.example.aibouauth.core.token.Token;
import com.example.aibouauth.core.token.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class LogoutServiceTest {

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    @Mock
    private Token storedToken;

    @InjectMocks
    private LogoutService logoutService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLogout_NoAuthorizationHeader() {
        // Given
        when(request.getHeader("Authorization")).thenReturn(null);

        // When
        logoutService.logout(request, response, authentication);

        // Then
        verify(tokenRepository, never()).findByToken(anyString());
    }

    @Test
    public void testLogout_InvalidAuthorizationHeader() {
        // Given
        when(request.getHeader("Authorization")).thenReturn("InvalidHeader");

        // When
        logoutService.logout(request, response, authentication);

        // Then
        verify(tokenRepository, never()).findByToken(anyString());
    }

    @Test
    public void testLogout_ValidToken() {

        String token = "valid.jwt.token";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(storedToken));


        logoutService.logout(request, response, authentication);


        verify(tokenRepository).findByToken(token);
        verify(storedToken).setExpired(true);
        verify(storedToken).setRevoked(true);
        verify(tokenRepository).save(storedToken);
    }

    @Test
    public void testLogout_TokenNotFound() {
        // Given
        String token = "nonexistent.jwt.token";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(tokenRepository.findByToken(token)).thenReturn(Optional.empty());

        // When
        logoutService.logout(request, response, authentication);

        // Then
        verify(tokenRepository).findByToken(token);
        verify(tokenRepository, never()).save(any(Token.class));
    }
}
