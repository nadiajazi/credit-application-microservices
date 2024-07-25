package com.example.aibouauth.core.purchase;

import com.example.aibouauth.core.product.*;
import com.example.aibouauth.core.user.User;
import com.example.aibouauth.core.user.UserResponse;
import com.example.aibouauth.core.user.UsersService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class PurchaseControllerTest {



    @Mock
    private PurchaseService purchaseService;

    @Mock
    private UsersService userService;
    @InjectMocks
    private PurchaseController purchaseController;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(purchaseController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void testFindAllPurchases() throws Exception {
        List<PurchaseResponse> purchases = new ArrayList<>();
        List<ProductPurchaseResponse> products1 = new ArrayList<>();
        List<ProductPurchaseResponse> products2 = new ArrayList<>();
        UserResponse userResponse1 = new UserResponse("test","28377510");
        UserResponse userResponse2 = new UserResponse("nadia","28377510");
        ProductPurchaseResponse product1 = new ProductPurchaseResponse(1,"product1",BigDecimal.valueOf(10.0));
        ProductPurchaseResponse product2 = new ProductPurchaseResponse(2,"product2",BigDecimal.valueOf(20.0));
        products1.add(product1);
        products2.add(product2);
        purchases.add(new PurchaseResponse(1, BigDecimal.valueOf(100.0), userResponse1, products1, null));
        purchases.add(new PurchaseResponse(2, BigDecimal.valueOf(200.0), userResponse2, products2, null));

        when(purchaseService.findAllPurchases()).thenReturn(purchases);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/purchases/admin/allpurchases")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].amount").value(100.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].amount").value(200.0));

        verify(purchaseService, times(1)).findAllPurchases();
    }

    @Test
    void testGetClientPurchases() throws Exception {
        Integer clientId = 1;
        User client = new User(clientId, "test", "test@example.com","28377510");

        List<PurchaseResponse> purchases = new ArrayList<>();
        purchases.add(new PurchaseResponse(1, BigDecimal.valueOf(100.0), null, null, null));
        purchases.add(new PurchaseResponse(2, BigDecimal.valueOf(200.0), null, null, null));

        when(userService.getUserById(clientId)).thenReturn(client);
        when(purchaseService.getUserPurchases(client)).thenReturn(purchases);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/purchases/client/{clientId}", clientId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].amount").value(100.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].amount").value(200.0));

        verify(userService, times(1)).getUserById(clientId);
        verify(purchaseService, times(1)).getUserPurchases(client);
    }

    @Test
    void testFindById() throws Exception {
        Integer purchaseId = 1;
        PurchaseResponse purchase = new PurchaseResponse(purchaseId, BigDecimal.valueOf(100.0), null, null, null);

        when(purchaseService.findById(purchaseId)).thenReturn(purchase);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/purchases/admin/{purchase-id}", purchaseId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(100.0));

        verify(purchaseService, times(1)).findById(purchaseId);
    }

    @Test
    void testCreatePurchase() throws Exception {
        PurchaseRequest request = new PurchaseRequest(1, new ArrayList<>());

        when(purchaseService.createPurchase(any(PurchaseRequest.class))).thenReturn(1);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/purchases/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("1"));

        verify(purchaseService, times(1)).createPurchase(any(PurchaseRequest.class));
    }


    private String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
