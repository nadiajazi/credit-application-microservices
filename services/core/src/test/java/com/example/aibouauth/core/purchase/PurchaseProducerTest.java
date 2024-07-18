package com.example.aibouauth.core.purchase;

import com.example.aibouauth.core.kafka.PurchaseConfirmation;
import com.example.aibouauth.core.kafka.PurchaseProducer;
import com.example.aibouauth.core.product.ProductPurchaseRequest;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, topics = {"purchase-topic"})
public class PurchaseProducerTest {

    @Autowired
    private PurchaseProducer purchaseProducer;

    @BeforeAll
    static void setup() {
        System.setProperty("spring.kafka.bootstrap-servers", "localhost:29092");
    }

    @Test
    public void testSendPurchaseConfirmation() {
        PurchaseConfirmation expectedConfirmation = new PurchaseConfirmation(
                new BigDecimal("100.00"), "John Doe", "johndoe@example.com", List.of(new ProductPurchaseRequest("product1", 2))
        );

        purchaseProducer.sendPurchaseConfirmation(expectedConfirmation);

        Properties consumerProps = createConsumerProperties();

        try (Consumer<String, String> consumer = new KafkaConsumer<>(consumerProps)) {
            consumer.subscribe(Collections.singletonList("purchase-topic"));

            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(10));

            assertNotNull(records);
            assertEquals(1, records.count(), "Expected exactly one record in Kafka topic");

            ConsumerRecord<String, String> receivedRecord = records.iterator().next();
            assertNotNull(receivedRecord);

            String jsonMessage = receivedRecord.value();
            assertTrue(jsonMessage.contains("\"totalAmount\":100.00"));
            assertTrue(jsonMessage.contains("\"customerName\":\"John Doe\""));
            assertTrue(jsonMessage.contains("\"email\":\"johndoe@example.com\""));
            assertTrue(jsonMessage.contains("\"products\":[{\"productName\":\"product1\",\"quantity\":2}]"));
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
}
