package com.example.aibouauth.notification.kafka;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifierSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.cloud.contract.verifier.converter.YamlContract;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.springframework.messaging.support.MessageBuilder.createMessage;

@Configuration
public class TestConfiguration {

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        configProps.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
        configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
    @Bean
    public JavaMailSender javaMailSender() {
        return mock(JavaMailSenderImpl.class);
    }

    @Bean
    public MessageVerifierSender<Message<?>> standaloneMessageVerifier(KafkaTemplate<String, Object> kafkaTemplate) {
        return new MessageVerifierSender<>() {

            @Override
            public void send(Message<?> message, String destination, @Nullable YamlContract contract) {
                kafkaTemplate.send(destination, message.getPayload());
            }

            @Override
            public <T> void send(T payload, Map<String, Object> headers, String destination, @Nullable YamlContract contract) {
                kafkaTemplate.send(
                        destination,
                        createMessage(payload, new MessageHeaders(headers)).getPayload()
                );
            }
        };
    }
}
