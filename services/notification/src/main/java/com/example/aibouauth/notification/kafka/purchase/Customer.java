package com.example.aibouauth.notification.kafka.purchase;

public record Customer(

        Integer id,
        String firstname,
        String lastname,
        String email
) {
}
