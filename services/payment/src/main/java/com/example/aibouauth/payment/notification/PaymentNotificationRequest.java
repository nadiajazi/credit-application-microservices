package com.example.aibouauth.payment.notification;

import com.example.aibouauth.payment.payment.Customer;
import com.example.aibouauth.payment.payment.PaymentMethod;

import java.math.BigDecimal;

public record PaymentNotificationRequest(

        BigDecimal amount,
        PaymentMethod paymentMethod,
        String customerFirstname,
        String customerLastname,
        String customerEmail

) {
}
