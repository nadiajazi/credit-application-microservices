package com.example.aibouauth.payment.payment;



import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class PaymentMapper {


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

    public PaymentResponseAdmin fromPaymentToAdmin(Payment payment, CustomerResponse customer) {
        if (payment == null) {
            return null;
        }

        return new PaymentResponseAdmin(
                payment.getId(),
                payment.getAmount(),
                payment.getPaymentMethod(),
                payment.getCreatedDate(),
                customer
        );
    }

}
