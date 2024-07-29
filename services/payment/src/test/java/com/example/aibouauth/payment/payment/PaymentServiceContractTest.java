package com.example.aibouauth.payment.payment;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.contract.verifier.messaging.internal.ContractVerifierMessage;
import org.springframework.cloud.contract.verifier.messaging.internal.ContractVerifierMessaging;
import org.springframework.cloud.contract.verifier.messaging.internal.ContractVerifierObjectMapper;

import static com.toomuchcoding.jsonassert.JsonAssertion.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;



import org.springframework.beans.factory.annotation.Autowired;

class PaymentServiceContractTest extends BaseClassForTests {

    @Autowired
    ContractVerifierMessaging contractVerifierMessaging;

    @Autowired
    ContractVerifierObjectMapper contractVerifierObjectMapper;

    @Test
    void validate_shouldProduceOrderCreatedEvent() throws Exception {
        // When
        sendNotification();

        // Then
        ContractVerifierMessage response = contractVerifierMessaging.receive("payment-topic");
        assertThat(response).isNotNull();

        // And
        DocumentContext parsedJson = JsonPath.parse(contractVerifierObjectMapper.writeValueAsString(response.getPayload()));
        assertThatJson(parsedJson).field("['amount']").matches("(\\d+\\.\\d+)");
        assertThatJson(parsedJson).field("['paymentMethod']").matches("(PAYPAL|CREDIT_CARD|VISA|MASTER_CARD|BITCOIN)");
        assertThatJson(parsedJson).field("['customerFirstname']").matches("[A-Za-z ]+");
        assertThatJson(parsedJson).field("['customerLastname']").matches("[A-Za-z ]+");
        assertThatJson(parsedJson).field("['customerEmail']").matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
    }
}
