package com.example.aibouauth.notification.kafka;


import com.example.aibouauth.notification.email.EmailService;
import com.example.aibouauth.notification.kafka.payment.PaymentConfirmation;
import com.example.aibouauth.notification.kafka.payment.PaymentMethod;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, topics = {"payment-topic"})
public class PaymentConsumerIntegrationTest {

    @Autowired
    private NotificationConsumer notificationConsumer;


    @MockBean
    private EmailService emailService;

    @BeforeAll
    static void setup() {
        System.setProperty("spring.kafka.bootstrap-servers", "localhost:9092");
    }

    @Test
    public void testConsumePaymentConfirmationNotifications() throws MessagingException {
        PaymentMethod paymentMethod = PaymentMethod.valueOf("CREDIT_CARD");

        PaymentConfirmation confirmation = new PaymentConfirmation(
                new BigDecimal("50.00"), paymentMethod, "Nadia","Jazi", "jazinadia7@gmail.com"
        );
        doNothing().when(emailService).sendPaymentSuccessEmail(any(), any(), any());

        notificationConsumer.consumePaymentSuccessNotifications(confirmation);

        String fullName = confirmation.customerFirstname() + " " + confirmation.customerLastname();

        await().atMost(10, TimeUnit.SECONDS).untilAsserted(() ->
                verify(emailService, times(1)).sendPaymentSuccessEmail(
                        eq("jazinadia7@gmail.com"),
                        eq(fullName),
                        eq(new BigDecimal("50.00"))
                )
        );
    }
}
