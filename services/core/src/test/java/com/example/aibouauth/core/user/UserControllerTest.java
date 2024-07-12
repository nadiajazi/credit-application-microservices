package com.example.aibouauth.core.user;



import com.example.aibouauth.core.product.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;

import static org.mockito.Mockito.doNothing;
import org.springframework.http.MediaType;


import java.math.BigDecimal;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

  @Mock
  private UsersService service;



  @InjectMocks
  private UserController userController;

  private MockMvc mockMvc;

  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    this.mockMvc = MockMvcBuilders.standaloneSetup(userController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();
    this.objectMapper = new ObjectMapper();
    User user = new User();
    user.setId(1);
    user.setEmail("user@example.com");
    user.setPassword("oldPassword");

    Authentication authentication = new UsernamePasswordAuthenticationToken(user, null);

    SecurityContext securityContext = SecurityContextHolder.getContext();
    securityContext.setAuthentication(authentication);
  }


  @Test
  void testChangePassword() throws Exception {
    changePasswordRequest request = new changePasswordRequest("oldPassword", "newPassword", "newPassword");

    doNothing().when(service).changePassword(any(changePasswordRequest.class), any(Principal.class));

    mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(request))
                    .principal(SecurityContextHolder.getContext().getAuthentication()))
            .andExpect(status().isOk());

    verify(service, times(1)).changePassword(any(changePasswordRequest.class), any(Principal.class));
  }

  private String asJsonString(Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  @Test
  void testUpdateMaxAmount() throws Exception {
    UserDto updatedUserData = new UserDto();
    updatedUserData.setMaxAmount(1000.0);

    User user = new User();
    user.setMaxAmount(1000.0);

    when(service.getUserById(anyInt())).thenReturn(user);
    doNothing().when(service).updateMaxAmount(any(UserDto.class), anyInt());

    mockMvc.perform(put("/api/v1/user/maxAmount/{id}", 1)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updatedUserData)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.maxAmount").value(1000.0));

    verify(service, times(1)).getUserById(anyInt());
    verify(service, times(1)).updateMaxAmount(any(UserDto.class), anyInt());
  }

  @Test
  void testFindUserIdByToken() throws Exception {
    when(service.getUserIdByToken(anyString())).thenReturn(1);

    mockMvc.perform(get("/api/v1/user/byToken")
                    .header("Authorization", "Bearer token"))
            .andExpect(status().isOk())
            .andExpect(content().string("1"));

    verify(service, times(1)).getUserIdByToken(anyString());
  }

  @Test
  void testGetUserMontant() throws Exception {
    when(service.getUserMontant(anyString())).thenReturn(BigDecimal.TEN);

    mockMvc.perform(get("/api/v1/user/montant")
                    .header("Authorization", "Bearer token"))
            .andExpect(status().isOk())
            .andExpect(content().string("10"));

    verify(service, times(1)).getUserMontant(anyString());
  }

  @Test
  void testGetMaxAmount() throws Exception {
    User user = new User();
    user.setMaxAmount(1000.0);

    when(service.getUserById(anyInt())).thenReturn(user);

    mockMvc.perform(get("/api/v1/user/maxAmount/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(content().string("1000.0"));

    verify(service, times(1)).getUserById(anyInt());
  }

  @Test
  void testGetMontant() throws Exception {
    User user = new User();
    user.setMontant(BigDecimal.TEN);

    when(service.getUserById(anyInt())).thenReturn(user);

    mockMvc.perform(get("/api/v1/user/montant/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(content().string("10"));

    verify(service, times(1)).getUserById(anyInt());
  }

  @Test
  void testUpdateUserMontant() throws Exception {
    BigDecimal newMontant = new BigDecimal("122.00");
    UpdateMontantRequest request = new UpdateMontantRequest(newMontant);


    when(service.getUserIdByToken(anyString())).thenReturn(1);
    doNothing().when(service).updateUserMontant(anyInt(), any(BigDecimal.class));

    mockMvc.perform(put("/api/v1/user/updateMontant")
                    .header("Authorization", "Bearer token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk());

    verify(service, times(1)).getUserIdByToken(anyString());
    verify(service, times(1)).updateUserMontant(anyInt(), any(BigDecimal.class));
  }

  @Test
  void testGetUserById_UserFound() throws Exception {
    User user = new User();
    user.setId(1);
    user.setFirstName("John");
    user.setLastName("Doe");
    user.setEmail("john.doe@example.com");
    user.setPhone("1234567890");

    when(service.getUserById(1)).thenReturn(user);

    mockMvc.perform(get("/api/v1/user/{id}", 1)
                    .header("Authorization", "Bearer token"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.firstname").value("John"))
            .andExpect(jsonPath("$.lastname").value("Doe"))
            .andExpect(jsonPath("$.email").value("john.doe@example.com"))
            .andExpect(jsonPath("$.phone").value("1234567890"));

    verify(service, times(1)).getUserById(1);
  }

  @Test
  void testGetUserById_UserNotFound() throws Exception {
    when(service.getUserById(1)).thenReturn(null);

    mockMvc.perform(get("/api/v1/user/{id}", 1)
                    .header("Authorization", "Bearer token"))
            .andExpect(status().isNotFound());

    verify(service, times(1)).getUserById(1);
  }

}
