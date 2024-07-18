package com.example.aibouauth.payment.payment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PaymentMapperTest {

    private PaymentMapper paymentMapper;

    @BeforeEach
    void setUp() {
        paymentMapper = new PaymentMapper();
    }

    @Test
    void testToPayment() {

        PaymentRequest request = new PaymentRequest(
                new BigDecimal("100.00"),
                PaymentMethod.CREDIT_CARD,
                new Customer(1, "Nadia", "Jazi", "jazinadia7@gmail.com")
        );


        Payment payment = paymentMapper.toPayment(request);


        assertEquals(request.amount(), payment.getAmount());
        assertEquals(request.paymentMethod(), payment.getPaymentMethod());
        assertNull(payment.getId());
    }

    @Test
    void testFromPayment() {

        Payment payment = new Payment();
        payment.setId(1);
        payment.setAmount(new BigDecimal("100.00"));
        payment.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        payment.setCreatedDate(LocalDateTime.now());
        payment.setUserId(12345);

        PaymentResponse paymentResponse = paymentMapper.fromPayment(payment);


        assertEquals(payment.getId(), paymentResponse.id());
        assertEquals(payment.getAmount(), paymentResponse.amount());
        assertEquals(payment.getPaymentMethod(), paymentResponse.paymentMethod());
        assertEquals(payment.getCreatedDate(), paymentResponse.createdDate());
        assertEquals(payment.getUserId(), paymentResponse.userId());
    }

    @Test
    void testFromPaymentToAdmin() {

        Payment payment = new Payment();
        payment.setId(1);
        payment.setAmount(new BigDecimal("100.00"));
        payment.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        payment.setCreatedDate(LocalDateTime.now());
        payment.setUserId(12345);


        PaymentResponseAdmin paymentResponseAdmin = paymentMapper.fromPaymentToAdmin(payment);


        assertEquals(payment.getId(), paymentResponseAdmin.id());
        assertEquals(payment.getAmount(), paymentResponseAdmin.amount());
        assertEquals(payment.getPaymentMethod(), paymentResponseAdmin.paymentMethod());
        assertEquals(payment.getCreatedDate(), paymentResponseAdmin.createdDate());
        assertNull(paymentResponseAdmin.customer());
    }
}
