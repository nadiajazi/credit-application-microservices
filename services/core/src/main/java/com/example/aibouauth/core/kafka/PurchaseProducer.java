package com.example.aibouauth.core.kafka;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PurchaseProducer {

    private final KafkaTemplate<String, PurchaseConfirmation> kafkaTemplate;
    public void sendPurchaseConfirmation(PurchaseConfirmation purchaseConfirmation){
        log.info("sending purchase confirmation");
        Message<PurchaseConfirmation> message = MessageBuilder
                .withPayload(purchaseConfirmation)
                .setHeader(KafkaHeaders.TOPIC, "purchase-topic")
                .build();
        kafkaTemplate.send(message);



    }
}
