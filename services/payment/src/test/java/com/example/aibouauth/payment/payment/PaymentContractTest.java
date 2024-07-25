package com.example.aibouauth.payment.payment;

import com.example.aibouauth.core.user.User;
import com.example.aibouauth.payment.client.UserClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureStubRunner(
        ids = "com.example.aibouauth:core:+:stubs:0.0.1-SNAPSHOT",
        stubsMode = StubRunnerProperties.StubsMode.LOCAL
)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password="
})
public class PaymentContractTest {

    @MockBean
    private UserClient userClient;

    @Test
    public void shouldGetUserById() {
        CustomerResponse mockResponse = new CustomerResponse(1,"John", "Doe", "john.doe@example.com", "1234567890");
        when(userClient.getUserById(anyString(), anyInt())).thenReturn(mockResponse);

        CustomerResponse response = userClient.getUserById("Bearer some-token", 1);
        assertEquals("John", response.firstname());
        assertEquals("Doe", response.lastname());
        assertEquals("john.doe@example.com", response.email());
        assertEquals("1234567890", response.phone());
    }
    @Test
    public void shouldFindUserIdByToken() {
        User mockUser = new User(1, "Nadia", "john.doe@example.com", "1234567890");
        when(userClient.findUserIdByToken(anyString())).thenReturn(mockUser.getId());

        Integer id = userClient.findUserIdByToken("Bearer some-token");
        assertEquals(mockUser.getId(), id);
    }

}
