package com.example.aibouauth.core.product;

import org.springframework.stereotype.Service;

@Service
public class ProductMapper {

    public ProductPurchaseResponse toProductPurchaseResponse(Product product) {
        return new ProductPurchaseResponse(product.getId(), product.getName(),product.getPrice());
    }
}
