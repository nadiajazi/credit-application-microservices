package com.example.aibouauth.core.payment;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;



@FeignClient(
        name = "payment-service",
        url = "${spring.application.configuration.payment-url}"
)
public class PaymentClient {

    @PostMapping("/makepaymet")
    void makePayment(@RequestHeader("Authorization") String token, @RequestBody PaymentRequest paymentRequest) {

    }

}
