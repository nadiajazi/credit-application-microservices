package com.example.aibouauth.payment.payment;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService service;
    @PostMapping("/makepayment")
    public ResponseEntity<Integer> createPayment(
            @RequestHeader("Authorization") String userToken,
            @RequestBody PaymentRequest request) {
        Integer paymentId = service.createPayment(request, userToken);
        return new ResponseEntity<>(paymentId, HttpStatus.CREATED);
    }
}
