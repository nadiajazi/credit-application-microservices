package com.example.aibouauth.payment.payment;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.cloud.contract.verifier.converter.YamlContract;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifierReceiver;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static java.util.Collections.emptyMap;
import static java.util.concurrent.TimeUnit.SECONDS;

abstract class KafkaEventVerifier implements MessageVerifierReceiver<Message<?>> {

    private final Set<Message<?>> consumedEvents = Collections.synchronizedSet(new HashSet<>());

    @KafkaListener(topics = "payment-topic", groupId = "paymentGroup")
    void consumeOrderCreated(ConsumerRecord<String, String> payload) {
        consumedEvents.add(MessageBuilder.createMessage(payload.value(), new MessageHeaders(emptyMap())));
    }

    @Override
    public Message<?> receive(String destination, long timeout, TimeUnit timeUnit, @Nullable YamlContract contract) {
        for (int i = 0; i < timeout; i++) {
            Message<?> msg = consumedEvents.stream().findFirst().orElse(null);
            if (msg != null) {
                return msg;
            }
            try {
                timeUnit.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return consumedEvents.stream().findFirst().orElse(null);
    }

    @Override
    public Message<?> receive(String destination, YamlContract contract) {
        return receive(destination, 10, SECONDS, contract);
    }
}
