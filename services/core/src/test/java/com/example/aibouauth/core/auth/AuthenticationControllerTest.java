package com.example.aibouauth.core.auth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.aibouauth.core.product.GlobalExceptionHandler;
import com.example.aibouauth.core.user.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;


class AuthenticationControllerTest {



    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(authenticationController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        this.objectMapper = new ObjectMapper();
    }

    @Test
    void registerTest() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .firstName("test")
                .lastName("test")
                .email("test@example.com")
                .password("password")
                .phone("123456789")
                .role(Role.USER)
                .build();

        AuthenticationResponse response = AuthenticationResponse.builder()
                .accessToken("jwtToken")
                .refreshToken("refreshToken")
                .firstName("test")
                .role(Role.USER)
                .id(1)
                .montant(BigDecimal.ZERO)
                .build();

        when(authenticationService.register(any(RegisterRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("jwtToken"))
                .andExpect(jsonPath("$.refreshToken").value("refreshToken"))
                .andExpect(jsonPath("$.firstName").value("test"))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.montant").value(0));
    }

    @Test
    void authenticateTest() throws Exception {
        AuthenticationRequest request = AuthenticationRequest.builder()
                .email("test@example.com")
                .password("password")
                .build();

        AuthenticationResponse response = AuthenticationResponse.builder()
                .accessToken("jwtToken")
                .refreshToken("refreshToken")
                .firstName("test")
                .role(Role.USER)
                .id(1)
                .montant(BigDecimal.ZERO)
                .build();

        when(authenticationService.authenticate(any(AuthenticationRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("jwtToken"))
                .andExpect(jsonPath("$.refreshToken").value("refreshToken"))
                .andExpect(jsonPath("$.firstName").value("test"))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.montant").value(0));
    }

    @Test
    void refreshTokenTest() throws Exception {
        mockMvc.perform(post("/api/v1/auth/refresh-token")
                        .header("Authorization", "Bearer refreshToken"))
                .andExpect(status().isOk());
    }
}
