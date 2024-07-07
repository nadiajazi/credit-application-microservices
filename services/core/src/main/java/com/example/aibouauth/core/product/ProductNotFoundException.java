package com.example.aibouauth.core.product;

public class ProductNotFoundException extends RuntimeException {


    public ProductNotFoundException(Integer productId) {
        super("Product not found with ID: " + productId);
    }}
