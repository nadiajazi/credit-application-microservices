package com.example.aibouauth.core.payment;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod paymentMethod
) {
}
