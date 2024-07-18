package com.example.aibouauth.core.purchase;


import com.example.aibouauth.core.kafka.PurchaseConfirmation;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class PurchaseConfirmationDeserializer implements Deserializer<PurchaseConfirmation> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // No configuration needed
    }

    @Override
    public PurchaseConfirmation deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, PurchaseConfirmation.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize PurchaseConfirmation", e);
        }
    }

    @Override
    public void close() {
        // No cleanup needed
    }
}

