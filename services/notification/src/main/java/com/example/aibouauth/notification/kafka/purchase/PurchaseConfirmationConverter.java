package com.example.aibouauth.notification.kafka.purchase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class PurchaseConfirmationConverter implements AttributeConverter<PurchaseConfirmation, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(PurchaseConfirmation attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting PurchaseConfirmation to JSON", e);
        }
    }

    @Override
    public PurchaseConfirmation convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, PurchaseConfirmation.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting JSON to PurchaseConfirmation", e);
        }
    }
}
