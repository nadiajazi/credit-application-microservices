package com.example.aibouauth.core.product;

import com.example.aibouauth.core.product.ProductRequest;
import com.example.aibouauth.core.product.Product;
import org.springframework.stereotype.Service;


@Service
public class ProductMapper {


    public Product toProduct(ProductRequest request) {
        if (request == null) {
           throw new NullPointerException("the product request should not be null");
        }


        return Product.builder()
                .name(request.name())
                .price(request.price())
                .images(request.images())
                .ref(request.ref())
                .quantity(request.quantity())
                .build();

    }


    public ProductPurchaseResponse toProductPurchaseResponse(Product product) {
        return new ProductPurchaseResponse(product.getId(), product.getName(),product.getPrice());
    }
}
