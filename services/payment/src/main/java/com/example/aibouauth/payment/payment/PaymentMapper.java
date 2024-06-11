package com.example.aibouauth.payment.payment;


import com.example.aibouauth.payment.client.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentMapper {

    private final UserClient userClient;
    public Payment toPayment(PaymentRequest request) {
        if (request == null) {
            return null;
        }
        return Payment.builder()
                .paymentMethod(request.paymentMethod())
                .amount(request.amount())
                .build();
    }

    public PaymentResponse fromPayment(Payment  payment) {
        if (payment == null) {
            return null;
        }


        return new PaymentResponse(
                payment.getId(),
                payment.getAmount(),
                payment.getPaymentMethod(),
                payment.getCreatedDate(),
                payment.getUserId()
        );
    }

    public PaymentResponseAdmin fromPaymentToAdmin(Payment payment) {
        if (payment == null) {
            return null;
        }
        return new PaymentResponseAdmin(
                payment.getId(),
                payment.getAmount(),
                payment.getPaymentMethod(),
                payment.getCreatedDate(),
                null
        );
    }

}
