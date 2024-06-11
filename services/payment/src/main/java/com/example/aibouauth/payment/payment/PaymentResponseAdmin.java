package com.example.aibouauth.payment.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponseAdmin(
        Integer id,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        LocalDateTime createdDate,
        CustomerResponse customer
) {
}
