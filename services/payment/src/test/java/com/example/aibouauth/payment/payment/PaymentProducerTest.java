package com.example.aibouauth.payment.payment;

import com.example.aibouauth.payment.notification.NotificationProducer;
import com.example.aibouauth.payment.notification.PaymentNotificationRequest;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;



import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, topics = {"payment-topic"})
public class PaymentProducerTest {


    @Autowired
    private NotificationProducer notificationProducer;

    private Consumer<String, String> consumer;
    @BeforeEach
    public void setup() {

        System.setProperty("spring.kafka.bootstrap-servers", "localhost:29092");


        Properties consumerProps = createConsumerProperties();
        consumer = new KafkaConsumer<>(consumerProps);
        consumer.subscribe(Collections.singletonList("purchase-topic"));
    }


    @Test
    public void testSendPaymentConfirmation() {
        PaymentNotificationRequest expectedConfirmation = new PaymentNotificationRequest(
                new BigDecimal("100.00"), PaymentMethod.CREDIT_CARD, "Nadia", "Jazi", "jazinadia7@gmail.com"
        );

        notificationProducer.sendNotification(expectedConfirmation);

        Properties consumerProps = createConsumerProperties();

        try (Consumer<String, String> consumer = new KafkaConsumer<>(consumerProps)) {
            consumer.subscribe(Collections.singletonList("payment-topic"));

            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(10));

            assertNotNull(records);
            assertEquals(1, records.count(), "Expected exactly one record in Kafka topic");

            ConsumerRecord<String, String> receivedRecord = records.iterator().next();
            assertNotNull(receivedRecord);

            String jsonMessage = receivedRecord.value();
            assertTrue(jsonMessage.contains("\"amount\":100.00"));
            assertTrue(jsonMessage.contains("\"paymentMethod\":\"CREDIT_CARD\""));
            assertTrue(jsonMessage.contains("\"customerFirstname\":\"Nadia\""));
            assertTrue(jsonMessage.contains("\"customerLastname\":\"Jazi\""));
            assertTrue(jsonMessage.contains("\"customerEmail\":\"jazinadia7@gmail.com\""));
        }
    }

    private Properties createConsumerProperties() {
        Properties consumerProps = new Properties();
        consumerProps.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        consumerProps.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "testGroup");
        consumerProps.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerProps.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProps.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        return consumerProps;
    }

    @AfterEach
    public void tearDown() {
        if (consumer != null) {
            consumer.close();
        }
    }
}
