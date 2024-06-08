package com.example.aibouauth.core.product;

import java.math.BigDecimal;

public record ProductPurchaseResponse(
        Integer productId,
        String name,
        BigDecimal price,
        Integer quantity
) {
}
