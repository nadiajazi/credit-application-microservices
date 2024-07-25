package com.example.aibouauth.core.purchase;

import com.example.aibouauth.core.kafka.PurchaseConfirmation;
import com.example.aibouauth.core.kafka.PurchaseProducer;
import com.example.aibouauth.core.product.ProductPurchaseRequest;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, topics = {"purchase-topic"})
public class PurchaseProducerTest {

   /* @Autowired
    private PurchaseProducer purchaseProducer;

    private Consumer<String, String> consumer;

    private static KafkaContainer kafkaContainer;

    static {
        kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));
        kafkaContainer.start();
    }

    @BeforeEach
    public void setup() {
        System.setProperty("spring.kafka.bootstrap-servers", kafkaContainer.getBootstrapServers());

        Properties consumerProps = createConsumerProperties();
        consumer = new KafkaConsumer<>(consumerProps);
        consumer.subscribe(Collections.singletonList("purchase-topic"));
    }

    @Test
    public void testSendPurchaseConfirmation() throws InterruptedException {
        PurchaseConfirmation expectedConfirmation = new PurchaseConfirmation(
                new BigDecimal("100.00"), "test test", "test@example.com", List.of(new ProductPurchaseRequest("product1", 2))
        );

        purchaseProducer.sendPurchaseConfirmation(expectedConfirmation);

        // Adding a small delay to ensure message is sent
        Thread.sleep(500);

        ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(7));

        assertNotNull(records);

        ConsumerRecord<String, String> receivedRecord = records.iterator().next();
        assertNotNull(receivedRecord);

        String jsonMessage = receivedRecord.value();
        assertTrue(jsonMessage.contains("\"totalAmount\":100.00"));
        assertTrue(jsonMessage.contains("\"customerName\":\"test test\""));
        assertTrue(jsonMessage.contains("\"email\":\"test@example.com\""));
        assertTrue(jsonMessage.contains("\"products\":[{\"productName\":\"product1\",\"quantity\":2}]"));
    }

    private Properties createConsumerProperties() {
        Properties consumerProps = new Properties();
        consumerProps.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
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

    */
}
