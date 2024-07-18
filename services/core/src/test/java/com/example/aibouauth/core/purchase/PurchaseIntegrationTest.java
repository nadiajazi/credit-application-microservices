package com.example.aibouauth.core.purchase;

import com.example.aibouauth.core.kafka.PurchaseConfirmation;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.nio.file.*;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"purchase-topic"})
public class PurchaseIntegrationTest {

    @Container
    static final KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:latest")
    ).withEnv("KAFKA_LOG_DIRS", "/tmp/kafka-logs-" + System.nanoTime())
            .waitingFor(Wait.forListeningPort().withStartupTimeout(Duration.ofMinutes(2)));

    @Container
    public static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:latest")
            .withDatabaseName("test_db")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void mysqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
    }

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
        registry.add("spring.kafka.consumer.group-id", () -> "testGroup-" + System.nanoTime());
    }

    @Autowired
    private KafkaTemplate<String, PurchaseConfirmation> kafkaTemplate;

    private static KafkaConsumer<String, PurchaseConfirmation> consumer;

    @BeforeAll
    public static void setUp() {
        kafka.start();

        Map<String, Object> consumerProps = new HashMap<>();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "testGroup");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);

        consumer = new KafkaConsumer<>(consumerProps);
        consumer.subscribe(List.of("purchase-topic"));
    }

    @AfterAll
    public static void tearDown() throws Exception {
        consumer.close();
        kafka.stop();
        cleanUpTemporaryFiles();
    }

    private static void cleanUpTemporaryFiles() throws Exception {
        Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"), "kafka-" + System.nanoTime());
        if (Files.exists(tempDir)) {
            Files.walk(tempDir)
                    .sorted((path1, path2) -> path2.compareTo(path1)) // Reverse order to delete directories after files
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
        }
    }

    @Test
    public void testSendPurchaseMessage() throws InterruptedException {

        assertThat(kafkaTemplate).isNotNull();

        PurchaseConfirmation confirmation = new PurchaseConfirmation(
                new BigDecimal("100.00"), "testuser", "testuser@example.com", List.of()
        );

        kafkaTemplate.send("purchase-topic", confirmation);
        Thread.sleep(1000);

        // Assert that the message was sent to the topic
        ConsumerRecord<String, PurchaseConfirmation> singleRecord = KafkaTestUtils.getSingleRecord(consumer, "purchase-topic");
        assertThat(singleRecord).isNotNull();
        assertThat(singleRecord.value()).isEqualTo(confirmation);
    }
}
