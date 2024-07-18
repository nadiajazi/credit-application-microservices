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
public class NotificationConsumerIntegrationTest {

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
                new BigDecimal("100.00"), "John Doe", "johndoe@example.com", List.of(new Product("product1", 2))
        );

        doNothing().when(emailService).sendPurchaseConfirmationEmail(any(), any(), any(), any());

        notificationConsumer.consumePurchaseConfirmationNotifications(purchaseConfirmation);

        await().atMost(10, TimeUnit.SECONDS).untilAsserted(() ->
                verify(emailService, times(1)).sendPurchaseConfirmationEmail(
                        eq(new BigDecimal("100.00")),
                        eq("John Doe"),
                        eq("johndoe@example.com"),
                        anyList()
                )
        );
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
