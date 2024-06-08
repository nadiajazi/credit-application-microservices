package com.example.aibouauth.notification.kafka.payment;

import java.math.BigDecimal;

public record PaymentConfirmation(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        String customerFirstname,
        String customerLastname,
        String customerEmail
) {
}
