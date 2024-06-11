package com.example.aibouauth.payment.client;

import com.example.aibouauth.payment.payment.Customer;
import com.example.aibouauth.payment.payment.CustomerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@FeignClient(
        name = "user-service",
        url = "${spring.application.configuration.user-url}"
)
public interface UserClient {
    @GetMapping("/user/byToken")
    Integer findUserIdByToken(
            @RequestHeader("Authorization") String authHeader
    );

    @GetMapping("/user/{id}")
    CustomerResponse getUserById(@RequestHeader("Authorization") String authHeader, @PathVariable("id") Integer id);

    @GetMapping("/user/montant")
    BigDecimal getUserMontant(
            @RequestHeader("Authorization") String authHeader
    );

    @PutMapping("/user/updateMontant")
    void updateUserMontant(@RequestHeader("Authorization") String token, @RequestBody UpdateMontantRequest request);
}

