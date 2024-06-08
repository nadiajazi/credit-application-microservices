package com.example.aibouauth.core.purchase;

import com.example.aibouauth.core.product.ProductPurchaseResponse;
import com.example.aibouauth.core.user.UserResponse;

import java.math.BigDecimal;
import java.util.List;

public record PurchaseResponse(
        Integer id,
        BigDecimal amount,
        UserResponse userResponse,
        List<ProductPurchaseResponse> products
) {
}
