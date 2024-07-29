package com.example.aibouauth.notification.kafka;

import com.example.aibouauth.notification.NotificationApplication;
import com.example.aibouauth.notification.notification.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.StubTrigger;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = {NotificationApplication.class, TestConfiguration.class})
@AutoConfigureStubRunner(ids = "com.example.aibouauth:payment:+:stubs:0.0.1-SNAPSHOT-stubs:8090", stubsMode = StubRunnerProperties.StubsMode.LOCAL)
@Testcontainers
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password="
})
public class EventHandlerPayment {

    @Container
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    StubTrigger stubTrigger;

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        kafka.start();
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @Test
    void should_initiate_payment_on_OrderCreatedEvent() {
        // When
        stubTrigger.trigger("notification_sent");

        // Then
        await().atMost(10, SECONDS).untilAsserted(() -> {
            var notifications = notificationRepository.findAll();
            assertThat(notifications).hasSize(1);
        });
    }
}
