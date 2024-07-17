package com.example.aibouauth.core.purchase;

import com.example.aibouauth.core.exception.BusinessException;
import com.example.aibouauth.core.kafka.PurchaseConfirmation;
import com.example.aibouauth.core.kafka.PurchaseProducer;
import com.example.aibouauth.core.product.Product;
import com.example.aibouauth.core.product.ProductPurchaseRequest;
import com.example.aibouauth.core.product.ProductRepository;
import com.example.aibouauth.core.user.User;
import com.example.aibouauth.core.user.UserRepository;
import com.example.aibouauth.core.user.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;



public class PurchaseServiceTest {

    @Mock
    private PurchaseRepository purchaseRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UsersService userService;


    @Mock
    private PurchaseMapper mapper;

    @Mock
    private PurchaseProducer purchaseProducer;

    @InjectMocks
    private PurchaseService purchaseService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePurchase_Success() {

        PurchaseRequest request = new PurchaseRequest(1, Collections.singletonList(new ProductPurchaseRequest("Product", 1)));
        User user = new User();
        user.setId(1);
        user.setFirstName("testUser");
        user.setEmail("test@example.com");

        Product product = new Product();
        product.setName("Product");
        product.setPrice(BigDecimal.TEN);
        product.setQuantity(10);

        when(userRepository.findUserById(1)).thenReturn(Optional.of(user));
        when(productRepository.findByName("Product")).thenReturn(product);
        when(mapper.toPurchase(request)).thenReturn(new Purchase());


        assertDoesNotThrow(() -> purchaseService.createPurchase(request));
        verify(productRepository, times(1)).save(product);
        verify(purchaseRepository, times(1)).save(ArgumentMatchers.any());
        verify(userService, times(1)).updateMontant(user, BigDecimal.TEN);


        verify(purchaseProducer, times(1)).sendPurchaseConfirmation(ArgumentMatchers.any());
    }
    @Test
    public void testCreatePurchase_UserNotFound() {
        PurchaseRequest request = new PurchaseRequest(1, new ArrayList<>());


        when(userRepository.findUserById(anyInt())).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> purchaseService.createPurchase(request));


        verify(userRepository, times(1)).findUserById(anyInt());
        verifyNoInteractions(productRepository);
        verifyNoInteractions(userService);
        verifyNoInteractions(purchaseProducer);
        verifyNoInteractions(purchaseRepository);
    }

    @Test
    void testCreatePurchase_ProductQuantityExceedsAvailable_ThrowsException() {
        PurchaseRequest request = new PurchaseRequest(1, Collections.singletonList(new ProductPurchaseRequest("Product", 20)));
        User user = new User();
        user.setId(1);

        Product product = new Product();
        product.setName("Product");
        product.setQuantity(10);

        when(userRepository.findUserById(1)).thenReturn(Optional.of(user));
        when(productRepository.findByName("Product")).thenReturn(product);

        assertThrows(NullPointerException.class, () -> purchaseService.createPurchase(request));
    }

    @Test
    public void testSendPurchaseConfirmation() {

        PurchaseConfirmation confirmation = new PurchaseConfirmation(
                new BigDecimal("100.00"), "testuser", "testuser@example.com", List.of()
        );

        purchaseProducer.sendPurchaseConfirmation(confirmation);


        verify(purchaseProducer, times(1)).sendPurchaseConfirmation(confirmation);
    }
}