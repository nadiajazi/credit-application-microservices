package com.example.aibouauth.payment.payment;

import com.example.aibouauth.payment.client.UpdateMontantRequest;
import com.example.aibouauth.payment.client.UserClient;
import com.example.aibouauth.payment.notification.NotificationProducer;
import com.example.aibouauth.payment.notification.PaymentNotificationRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository repository;
    private final PaymentMapper mapper;
    private final NotificationProducer notificationProducer;
    private final UserClient userClient;

    @Transactional
    public Integer createPayment(PaymentRequest request, String userToken) {
        Integer userId = userClient.findUserIdByToken(userToken);
        BigDecimal userMontant = userClient.getUserMontant(userToken);

        if (request.amount().compareTo(userMontant) > 0) {
            throw new IllegalArgumentException("Payment amount exceeds the user's total amount.");
        }

        // Update user's montant
        BigDecimal newMontant = userMontant.subtract(request.amount());
        userClient.updateUserMontant(userToken,new UpdateMontantRequest(userId, newMontant));

        // Save the payment
        var payment = mapper.toPayment(request);
        payment.setCreatedDate(LocalDateTime.now());
        payment.setUserId(userId);



        // Send notification
        notificationProducer.sendNotification(
              new PaymentNotificationRequest(
                     request.amount(),
                     request.paymentMethod(),
                     request.customer().firstName(),
                     request.customer().lastName(),
                     request.customer().email()
        )
        );
        payment = repository.save(payment);

        return payment.getId();
    }
}
