package com.example.aibouauth.payment.payment;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Customer customer
) {
}
