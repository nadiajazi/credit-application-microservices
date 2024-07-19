package com.example.aibouauth.payment.payment;

import com.example.aibouauth.payment.client.UserClient;
import com.example.aibouauth.payment.notification.NotificationProducer;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentMapper paymentMapper;

    @Mock
    private NotificationProducer notificationProducer;

    @Mock
    private UserClient userClient;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Transactional
    void testCreatePayment_Successful() {
        // Given
        String userToken = "valid_token";
        PaymentRequest request = new PaymentRequest(
                new BigDecimal("50.00"),
                PaymentMethod.CREDIT_CARD,
                new Customer(1, "John", "Doe", "john.doe@example.com")
        );
        Integer userId = 1;
        BigDecimal userMontant = new BigDecimal("100.00");

        when(userClient.findUserIdByToken(userToken)).thenReturn(userId);
        when(userClient.getUserMontant(userToken)).thenReturn(userMontant);
        when(paymentMapper.toPayment(request)).thenReturn(new Payment());
        when(paymentRepository.save(any())).thenAnswer(invocation -> {
            Payment savedPayment = invocation.getArgument(0);
            savedPayment.setId(1);
            return savedPayment;
        });

        // When
        Integer paymentId = paymentService.createPayment(request, userToken);

        // Then
        assertNotNull(paymentId);
        verify(userClient, times(1)).findUserIdByToken(userToken);
        verify(userClient, times(1)).getUserMontant(userToken);
        verify(paymentMapper, times(1)).toPayment(request);
        verify(paymentRepository, times(1)).save(any());
        verify(notificationProducer, times(1)).sendNotification(any());
    }

    @Test
    void testCreatePayment_InsufficientMontant() {
        // Given
        String userToken = "valid_token";
        PaymentRequest request = new PaymentRequest(
                new BigDecimal("150.00"),
                PaymentMethod.CREDIT_CARD,
                new Customer(1, "John", "Doe", "john.doe@example.com")
        );
        BigDecimal userMontant = new BigDecimal("100.00");

        when(userClient.getUserMontant(userToken)).thenReturn(userMontant);

        // When, Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> paymentService.createPayment(request, userToken));
        assertEquals("Payment amount exceeds the user's total amount.", exception.getMessage());

        verify(userClient, times(1)).getUserMontant(userToken);
        verify(paymentMapper, never()).toPayment(any());
        verify(paymentRepository, never()).save(any());
        verify(notificationProducer, never()).sendNotification(any());
    }

    @Test
    void testGetAllPayments() {
        // Given
        String authHeader = "Bearer test-token";

        Payment payment1 = new Payment();
        payment1.setId(1);
        payment1.setAmount(BigDecimal.valueOf(100));
        payment1.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        payment1.setCreatedDate(LocalDateTime.now());
        payment1.setUserId(1);

        Payment payment2 = new Payment();
        payment2.setId(2);
        payment2.setAmount(BigDecimal.valueOf(200));
        payment2.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        payment2.setCreatedDate(LocalDateTime.now());
        payment2.setUserId(2);

        List<Payment> payments = List.of(payment1, payment2);

        CustomerResponse customerResponse1 = new CustomerResponse(1, "John", "Doe", "john.doe@example.com", "1234567890");
        CustomerResponse customerResponse2 = new CustomerResponse(2, "Jane", "Doe", "jane.doe@example.com", "0987654321");

        PaymentResponseAdmin responseAdmin1 = new PaymentResponseAdmin(
                payment1.getId(),
                payment1.getAmount(),
                payment1.getPaymentMethod(),
                payment1.getCreatedDate(),
                customerResponse1
        );

        PaymentResponseAdmin responseAdmin2 = new PaymentResponseAdmin(
                payment2.getId(),
                payment2.getAmount(),
                payment2.getPaymentMethod(),
                payment2.getCreatedDate(),
                customerResponse2
        );

        when(paymentRepository.findAll()).thenReturn(payments);
        when(userClient.getUserById(authHeader, 1)).thenReturn(customerResponse1);
        when(userClient.getUserById(authHeader, 2)).thenReturn(customerResponse2);
        when(paymentMapper.fromPaymentToAdmin(payment1, customerResponse1)).thenReturn(responseAdmin1);
        when(paymentMapper.fromPaymentToAdmin(payment2, customerResponse2)).thenReturn(responseAdmin2);

        // When
        List<PaymentResponseAdmin> result = paymentService.getAllPayments(authHeader);

        // Then
        assertEquals(2, result.size());
        assertEquals(responseAdmin1, result.get(0));
        assertEquals(responseAdmin2, result.get(1));

        verify(paymentRepository).findAll();
        verify(userClient).getUserById(authHeader, 1);
        verify(userClient).getUserById(authHeader, 2);
        verify(paymentMapper).fromPaymentToAdmin(payment1, customerResponse1);
        verify(paymentMapper).fromPaymentToAdmin(payment2, customerResponse2);
    }



    @Test
    void testGetUserPayments() {
        // Given
        Integer userId = 1;
        Payment payment1 = new Payment(1, new BigDecimal("100.00"), PaymentMethod.CREDIT_CARD, LocalDateTime.now(), userId);
        Payment payment2 = new Payment(2, new BigDecimal("150.00"), PaymentMethod.PAYPAL, LocalDateTime.now().minusDays(1), userId);
        List<Payment> payments = List.of(payment1, payment2);


        PaymentResponse paymentResponse1 = new PaymentResponse(payment1.getId(), payment1.getAmount(), payment1.getPaymentMethod(), payment1.getCreatedDate(), userId);
        PaymentResponse paymentResponse2 = new PaymentResponse(payment2.getId(), payment2.getAmount(), payment2.getPaymentMethod(), payment2.getCreatedDate(), userId);

        when(paymentRepository.findByUserId(userId)).thenReturn(payments);
        when(paymentMapper.fromPayment(payment1)).thenReturn(paymentResponse1);
        when(paymentMapper.fromPayment(payment2)).thenReturn(paymentResponse2);

        // When
        List<PaymentResponse> result = paymentService.getUserPayments(userId);

        // Then
        assertEquals(2, result.size());
        assertEquals(paymentResponse1, result.get(0));
        assertEquals(paymentResponse2, result.get(1));

        verify(paymentRepository).findByUserId(userId);
        verify(paymentMapper).fromPayment(payment1);
        verify(paymentMapper).fromPayment(payment2);
    }





}
