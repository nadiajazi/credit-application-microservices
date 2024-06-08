package com.example.aibouauth.notification.kafka;

import com.example.aibouauth.notification.email.EmailService;
import com.example.aibouauth.notification.kafka.purchase.PurchaseConfirmation;
import com.example.aibouauth.notification.kafka.payment.PaymentConfirmation;
import com.example.aibouauth.notification.notification.Notification;
import com.example.aibouauth.notification.notification.NotificationRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.example.aibouauth.notification.notification.NotificationType.*;
import static java.lang.String.format;
@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationRepository repository;
    private final EmailService emailService;
    @KafkaListener(topics = "payment-topic")
    public void consumePaymentSuccessNotifications(PaymentConfirmation paymentConfirmation) throws MessagingException {
        log.info(format("Consuming the message from payment-topic Topic:: %s", paymentConfirmation));
        repository.save(
                Notification.builder()
                        .type(PAYMENT_CONFIRMATION)
                        .notificationDate(LocalDateTime.now())
                        .paymentConfirmation(paymentConfirmation)
                        .build()
        );
        var customerName = paymentConfirmation.customerFirstname() + " " + paymentConfirmation.customerLastname();
        emailService.sendPaymentSuccessEmail(
                paymentConfirmation.customerEmail(),
                customerName,
                paymentConfirmation.amount()
        );
    }

    @KafkaListener(topics = "purchase-topic")
    public void consumePurchaseConfirmationNotifications(PurchaseConfirmation purchaseConfirmation) throws MessagingException {
        log.info(format("Consuming the message from purchase-topic Topic:: %s", purchaseConfirmation));
        repository.save(
                Notification.builder()
                        .type(PURCHASE_CONFIRMATION)
                        .notificationDate(LocalDateTime.now())
                        .purchaseConfirmation(purchaseConfirmation)
                        .build()
        );
        emailService.sendPurchaseConfirmationEmail(
                purchaseConfirmation.totalAmount(),
                purchaseConfirmation.customerName(),
                purchaseConfirmation.email(),
                purchaseConfirmation.products()
        );
    }
}
