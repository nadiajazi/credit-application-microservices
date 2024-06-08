package com.example.aibouauth.notification.kafka.payment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class PaymentConfirmationConverter implements AttributeConverter<PaymentConfirmation, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(PaymentConfirmation attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting PaymentConfirmation to JSON", e);
        }
    }

    @Override
    public PaymentConfirmation convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, PaymentConfirmation.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting JSON to PaymentConfirmation", e);
        }
    }
}
