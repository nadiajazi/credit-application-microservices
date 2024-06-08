package com.example.aibouauth.core.user;

public record CustomerResponse(
        String id,
        String firstname,
        String lastname,
        String email
) {
}
