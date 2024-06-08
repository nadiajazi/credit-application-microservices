package com.example.aibouauth.notification.kafka.purchase;

import java.math.BigDecimal;
import java.util.List;

public record PurchaseConfirmation(
        BigDecimal totalAmount,
        String customerName,
        String email,
        List<Product> products

) {
}
