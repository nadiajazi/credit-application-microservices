package com.example.aibouauth.payment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

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

    @GetMapping("/user/montant")
    BigDecimal getUserMontant(
            @RequestHeader("Authorization") String authHeader
    );

    @PutMapping("/user/updateMontant")
    void updateUserMontant(@RequestHeader("Authorization") String token, @RequestBody UpdateMontantRequest request);
}

