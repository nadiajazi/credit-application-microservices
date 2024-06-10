package com.example.aibouauth.payment.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
        Integer id,

        BigDecimal amount,
        PaymentMethod paymentMethod,
        LocalDateTime createdDate,
        Integer userId) {
}
