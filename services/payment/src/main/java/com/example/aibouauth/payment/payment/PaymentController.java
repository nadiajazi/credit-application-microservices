package com.example.aibouauth.payment.payment;

import com.example.aibouauth.payment.client.UserClient;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService service;
    private final UserClient userClient;
    @PostMapping("/makepayment")
    public ResponseEntity<Integer> createPayment(
            @RequestHeader("Authorization") String userToken,
            @RequestBody PaymentRequest request) {
        Integer paymentId = service.createPayment(request, userToken);
        return new ResponseEntity<>(paymentId, HttpStatus.CREATED);
    }

    @GetMapping("/user")
    public ResponseEntity<List<PaymentResponse>> getUserPayments(@RequestHeader("Authorization") String userToken) {
        Integer userId = userClient.findUserIdByToken(userToken);
        List<PaymentResponse> payments = service.getUserPayments(userId);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/admin")
    public ResponseEntity<List<PaymentResponseAdmin>> getAllPayments(@RequestHeader("Authorization") String authHeader) {
        List<PaymentResponseAdmin> payments = service.getAllPayments(authHeader);
        return ResponseEntity.ok(payments);
    }
}
