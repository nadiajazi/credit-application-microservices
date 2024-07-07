package com.example.aibouauth.core.product;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository repository;

    @Mock
    private ProductMapper mapper;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void should_successfully_save_a_product() {
        ProductRequest productRequest = new ProductRequest("sunglasses",
                new BigDecimal("4500"),
                "https://image4.cdnsbg.com/1/69/564658_1702952188230.jpg",
                "rfc",
                500);

        Product product = new Product(1,
                "sunglasses",
                new BigDecimal("4500"),
                "https://image4.cdnsbg.com/1/69/564658_1702952188230.jpg",
                "rfc",
                500
                );

        Product savedProduct = new Product(
                "sunglasses",
                new BigDecimal("4500"),
                "https://image4.cdnsbg.com/1/69/564658_1702952188230.jpg",
                "rfc",
                500
        );

        savedProduct.setId(1);

        when(mapper.toProduct(productRequest)).thenReturn(product);
        when(repository.save(product)).thenReturn(savedProduct);
        when(mapper.toProductPurchaseResponse(savedProduct))
                .thenReturn(new ProductPurchaseResponse(
                        1,
                        "sunglasses",
                        new BigDecimal(4500)
                ));

        ProductPurchaseResponse response = productService.createProduct(productRequest);
        assertEquals(productRequest.name(),response.name());
        assertEquals(productRequest.price(),response.price());

        verify(mapper, times(1)).toProduct(productRequest);
        verify(repository, times(1)).save(product);
        verify(mapper, times(1)).toProductPurchaseResponse(savedProduct);
    }





};