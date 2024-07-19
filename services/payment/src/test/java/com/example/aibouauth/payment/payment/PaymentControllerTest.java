package com.example.aibouauth.payment.payment;

import com.example.aibouauth.payment.client.UserClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @Mock
    private PaymentMapper paymentMapper;

    @Mock
    private UserClient userClient;

    @InjectMocks
    private PaymentController paymentController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();
    }


    @Test
    void testCreatePayment() throws Exception {

        PaymentRequest request = new PaymentRequest(
                new BigDecimal("100.00"),
                PaymentMethod.CREDIT_CARD,
                new Customer(1, "Nadia", "Jazi", "jazinadia7@gmail.com")
        );
        String userToken = "Bearer valid_token";
        when(paymentService.createPayment(any(PaymentRequest.class), any(String.class))).thenReturn(1);


        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/payment/makepayment")
                        .header(HttpHeaders.AUTHORIZATION, userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("1"));

        verify(paymentService, times(1)).createPayment(any(PaymentRequest.class), eq(userToken));
    }

    @Test
    void testGetUserPayments() throws Exception {

        String userToken = "valid_token";
        Integer userId = 1;
        PaymentResponse paymentResponse1 = new PaymentResponse(
                1, new BigDecimal("100.00"), PaymentMethod.CREDIT_CARD, null, userId);
        PaymentResponse paymentResponse2 = new PaymentResponse(
                2, new BigDecimal("150.00"), PaymentMethod.PAYPAL, null, userId);
        List<PaymentResponse> payments = Arrays.asList(paymentResponse1, paymentResponse2);


        when(userClient.findUserIdByToken(userToken)).thenReturn(userId);
        when(paymentService.getUserPayments(userId)).thenReturn(payments);


        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/payment/user")
                        .header(HttpHeaders.AUTHORIZATION, userToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(asJsonString(payments)));

        verify(userClient, times(1)).findUserIdByToken(userToken);
        verify(paymentService, times(1)).getUserPayments(userId);
    }

    @Test
    void testGetAdminAllPayments() throws Exception {

        String authHeader = "Bearer test-token";

        CustomerResponse customerResponse1 = new CustomerResponse(1, "John", "Doe", "john.doe@example.com", "1234567890");
        CustomerResponse customerResponse2 = new CustomerResponse(2, "Jane", "Doe", "jane.doe@example.com", "0987654321");
        PaymentResponseAdmin paymentResponse1 = new PaymentResponseAdmin(
                1, new BigDecimal("100.00"), PaymentMethod.CREDIT_CARD, null, customerResponse1);
        PaymentResponseAdmin paymentResponse2 = new PaymentResponseAdmin(
                2, new BigDecimal("150.00"), PaymentMethod.PAYPAL, null, customerResponse2);
        List<PaymentResponseAdmin> payments = Arrays.asList(paymentResponse1, paymentResponse2);



        when(paymentService.getAllPayments(authHeader)).thenReturn(payments);


        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/payment/admin")
                        .header(HttpHeaders.AUTHORIZATION, authHeader)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(asJsonString(payments)));


        verify(paymentService, times(1)).getAllPayments(authHeader);
    }

    private String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}