package com.example.aibouauth.notification.kafka;

import com.example.aibouauth.notification.email.EmailService;
import com.example.aibouauth.notification.kafka.payment.PaymentConfirmation;
import com.example.aibouauth.notification.kafka.payment.PaymentMethod;
import com.example.aibouauth.notification.kafka.purchase.Product;
import com.example.aibouauth.notification.kafka.purchase.PurchaseConfirmation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import jakarta.mail.MessagingException;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.*;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, topics = {"purchase-topic"})
public class PurchaseConsumerIntegrationTest {

    @Autowired
    private NotificationConsumer notificationConsumer;


    @MockBean
    private EmailService emailService;

    @BeforeAll
    static void setup() {
        System.setProperty("spring.kafka.bootstrap-servers", "localhost:9092");
    }

    @Test
    public void testConsumePurchaseConfirmationNotifications() throws MessagingException {
        PurchaseConfirmation purchaseConfirmation = new PurchaseConfirmation(
                new BigDecimal("100.00"), "test test", "test@example.com", List.of(new Product("product1", 2))
        );

        doNothing().when(emailService).sendPurchaseConfirmationEmail(any(), any(), any(), any());

        notificationConsumer.consumePurchaseConfirmationNotifications(purchaseConfirmation);

        await().atMost(10, TimeUnit.SECONDS).untilAsserted(() ->
                verify(emailService, times(1)).sendPurchaseConfirmationEmail(
                        eq(new BigDecimal("100.00")),
                        eq("test test"),
                        eq("test@example.com"),
                        anyList()
                )
        );
    }

}
