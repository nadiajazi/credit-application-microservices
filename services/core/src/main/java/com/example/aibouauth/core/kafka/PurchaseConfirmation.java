package com.example.aibouauth.core.kafka;

import com.example.aibouauth.core.product.ProductPurchaseRequest;


import java.math.BigDecimal;
import java.util.List;

public record PurchaseConfirmation(
        BigDecimal totalAmount,
        String customerName,
        String email,
        List<ProductPurchaseRequest> products
) {

}
