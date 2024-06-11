package com.example.aibouauth.core.user;

public record CustomerResponse(
        Integer id,
        String firstname,
        String lastname,
        String email,
        String phone
) {
    public static CustomerResponse fromEntity(User user) {
        return new CustomerResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhone());
    }
}
