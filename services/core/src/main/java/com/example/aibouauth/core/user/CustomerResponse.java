package com.example.aibouauth.core.user;

import java.math.BigDecimal;

public record CustomerResponse(
        Integer id,
        String firstname,
        String lastname,
        String email,
        String phone,
        BigDecimal montant
) {
    public static CustomerResponse fromEntity(User user) {
        return new CustomerResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhone(), user.getMontant());
    }
}
