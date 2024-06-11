package com.example.aibouauth.payment.payment;

public record CustomerResponse(
        Integer id,
        String firstname,
        String lastname,
        String email,
        String phone
) {
}
