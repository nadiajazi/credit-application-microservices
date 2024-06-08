package com.example.aibouauth.notification.notification;


import com.example.aibouauth.notification.kafka.payment.PaymentConfirmation;
import com.example.aibouauth.notification.kafka.payment.PaymentConfirmationConverter;
import com.example.aibouauth.notification.kafka.purchase.PurchaseConfirmation;
import com.example.aibouauth.notification.kafka.purchase.PurchaseConfirmationConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Notification
{
    @Id
    @GeneratedValue
    private Integer id;
    private NotificationType type;
    private LocalDateTime notificationDate;
    @Convert(converter = PurchaseConfirmationConverter.class)
    private PurchaseConfirmation purchaseConfirmation;
    @Convert(converter = PaymentConfirmationConverter.class)
    private PaymentConfirmation paymentConfirmation;
}
