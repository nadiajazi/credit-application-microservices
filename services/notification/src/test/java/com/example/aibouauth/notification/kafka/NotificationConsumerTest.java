package com.example.aibouauth.notification.kafka;

import static org.mockito.Mockito.*;


import com.example.aibouauth.notification.email.EmailService;
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
        // Arrange
        PurchaseConfirmation confirmation = new PurchaseConfirmation(
                new BigDecimal("100.00"), "nadiajazi", "jazinadia@gmail.com", List.of()
        );

        // Act
        notificationConsumer.consumePurchaseConfirmationNotifications(confirmation);

        // Assert
        verify(repository, times(1)).save(any());
        verify(emailService, times(1)).sendPurchaseConfirmationEmail(
                confirmation.totalAmount(), confirmation.customerName(), confirmation.email(), confirmation.products()
        );
    }
}
