package com.example.aibouauth.core.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {

    private ProductMapper mapper;

    @BeforeEach
    void setUp(){
        mapper = new ProductMapper();
    }


    @Test
    public void shouldMapProductRequestToProduct(){
       ProductRequest productRequest = new ProductRequest("sunglasses",
               new BigDecimal("4500"),
               "https://image4.cdnsbg.com/1/69/564658_1702952188230.jpg",
               "rfc",
               5000);

       Product product = mapper.toProduct(productRequest);
       assertEquals(productRequest.price(),product.getPrice());
       assertEquals(productRequest.images(),product.getImages());
       assertEquals(productRequest.ref(),product.getRef());
       assertEquals(productRequest.quantity(),product.getQuantity());
    }
    @Test
    public void should_throw_null_pointer_exception_when_productRequest_is_null() {
        var exp = assertThrows(NullPointerException.class, () -> mapper.toProduct(null));
        assertEquals("the product request should not be null", exp.getMessage());

    }

    @Test
    public void shouldMapProductToProductPurchaseResponse(){
        Product product = new Product(1,"watch",
                new BigDecimal("4500"),
                "https://images.unsplash.com/photo-1523275335684-37898b6baf30?q=80&w=1999&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                "abc",
                5000);

        ProductPurchaseResponse productPurchaseResponse = mapper.toProductPurchaseResponse(product);
        assertEquals(productPurchaseResponse.productId(),product.getId());
        assertEquals(productPurchaseResponse.name(),product.getName());
        assertEquals(productPurchaseResponse.price(),product.getPrice());


    }


}