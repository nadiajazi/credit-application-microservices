package com.example.aibouauth.notification.kafka;

import static org.mockito.Mockito.*;


import com.example.aibouauth.notification.email.EmailService;
import com.example.aibouauth.notification.kafka.payment.PaymentConfirmation;
import com.example.aibouauth.notification.kafka.payment.PaymentMethod;
import com.example.aibouauth.notification.notification.NotificationRepository;
import com.example.aibouauth.notification.kafka.purchase.PurchaseConfirmation;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
@TestPropertySource(properties = "spring.kafka.bootstrap-servers=kafka:9092")
public class NotificationConsumerTest {

    @Mock
    private NotificationRepository repository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private NotificationConsumer notificationConsumer;

    @Test
    public void testConsumePurchaseConfirmationNotifications() throws Exception {

        PurchaseConfirmation confirmation = new PurchaseConfirmation(
                new BigDecimal("100.00"), "nadiajazi", "jazinadia@gmail.com", List.of()
        );


        notificationConsumer.consumePurchaseConfirmationNotifications(confirmation);


        verify(repository, times(1)).save(any());
        verify(emailService, times(1)).sendPurchaseConfirmationEmail(
                confirmation.totalAmount(), confirmation.customerName(), confirmation.email(), confirmation.products()
        );
    }

    @Test
    public void testConsumePaymentConfirmationNotifications() throws Exception {

        PaymentMethod paymentMethod = PaymentMethod.valueOf("CREDIT_CARD");

        PaymentConfirmation confirmation = new PaymentConfirmation(
                new BigDecimal("50.00"), paymentMethod, "Nadia","Jazi", "jazinadia7@gmail.com"
        );



        notificationConsumer.consumePaymentSuccessNotifications(confirmation);

        String fullName = confirmation.customerFirstname() + " " + confirmation.customerLastname();

        verify(repository, times(1)).save(any());
        verify(emailService, times(1)).sendPaymentSuccessEmail(confirmation.customerEmail(),
               fullName, confirmation.amount()
        );
    }
}
