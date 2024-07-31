package com.example.aibouauth.payment.payment;


import com.example.aibouauth.payment.client.UpdateMontantRequest;
import com.example.aibouauth.payment.client.UserClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureStubRunner(
        ids = "com.example.aibouauth:core:+:stubs:0.0.1-SNAPSHOT-stubs:8010",
        stubsMode = StubRunnerProperties.StubsMode.LOCAL
)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",

})
@ActiveProfiles("test")
public class PaymentContractTest {

    @MockBean
    private UserClient userClient;



    @Value("${spring.application.configuration.url}")
    private String userServiceUrl;

    @Test
    void shouldGetUserById() {
        RestTemplate restTemplate = new RestTemplate();
        String url = userServiceUrl + "/user/1";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer some-token");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<CustomerResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity, CustomerResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().firstname()).isEqualTo("Nadia");
        assertThat(response.getBody().lastname()).isEqualTo("Jazi");
        assertThat(response.getBody().email()).isEqualTo("jazinadia@gmail.com");
        assertThat(response.getBody().phone()).isEqualTo("1234567890");
    }
    @Test
    public void shouldFindUserIdByToken() {

        RestTemplate restTemplate = new RestTemplate();
        String url = userServiceUrl + "/user/byToken";


        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer some-token");
        HttpEntity<String> entity = new HttpEntity<>(headers);


        when(userClient.findUserIdByToken(anyString())).thenReturn(1);


        ResponseEntity<Integer> response = restTemplate.exchange(url, HttpMethod.GET, entity, Integer.class);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(1);
    }

    @Test
    void shouldGetUserMontant() {
        RestTemplate restTemplate = new RestTemplate();
        String url = userServiceUrl + "/user/montant";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer some-token");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        when(userClient.getUserMontant(anyString())).thenReturn(new BigDecimal("100.00"));

        ResponseEntity<BigDecimal> response = restTemplate.exchange(url, HttpMethod.GET, entity, BigDecimal.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(new BigDecimal("100.00"));
    }

    @Test
    void shouldUpdateUserMontant() {
        RestTemplate restTemplate = new RestTemplate();
        String url = userServiceUrl + "/user/updateMontant";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer some-token");
        headers.setContentType(MediaType.APPLICATION_JSON);

        UpdateMontantRequest request = new UpdateMontantRequest(1, new BigDecimal("150.00"));
        HttpEntity<UpdateMontantRequest> entity = new HttpEntity<>(request, headers);

        doNothing().when(userClient).updateUserMontant(anyString(), any(UpdateMontantRequest.class));

        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.PUT, entity, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


}
