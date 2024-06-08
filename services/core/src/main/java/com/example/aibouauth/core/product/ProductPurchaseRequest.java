package com.example.aibouauth.core.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductPurchaseRequest(
        @NotNull(message = "Product name is mandatory")
        String productName,
        @Positive(message = "Quantity is mandatory")
        Integer quantity
) {
}
