package com.example.aibouauth.payment.payment;

import com.example.aibouauth.payment.notification.NotificationProducer;
import com.example.aibouauth.payment.notification.PaymentNotificationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.testcontainers.junit.jupiter.Testcontainers;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureMessageVerifier
@Testcontainers
public class BaseClassForTests {

    @Container
    public static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));

    @Autowired
    private NotificationProducer notificationProducer;

    @BeforeEach
    void setup() {
        kafka.start();
        System.setProperty("spring.kafka.bootstrap-servers", kafka.getBootstrapServers());
    }


    public void sendNotification() {
        PaymentNotificationRequest request = new PaymentNotificationRequest(
                BigDecimal.valueOf(150.00),
                PaymentMethod.CREDIT_CARD,
                "Nadia",
                "Jazi",
                "jazinadia@gmail.com"
        );
        notificationProducer.sendNotification(request);
    }
}
