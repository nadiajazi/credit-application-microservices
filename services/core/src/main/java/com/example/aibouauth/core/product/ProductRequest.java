package com.example.aibouauth.core.product;

import java.math.BigDecimal;

public record ProductRequest(
         String name,
         BigDecimal price,
         String images,
         String ref,
         Integer quantity
) {
}
