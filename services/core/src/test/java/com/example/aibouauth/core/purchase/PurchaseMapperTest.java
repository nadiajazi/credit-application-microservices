package com.example.aibouauth.core.purchase;

import com.example.aibouauth.core.product.Product;
import com.example.aibouauth.core.product.ProductPurchaseRequest;
import com.example.aibouauth.core.product.ProductRepository;
import com.example.aibouauth.core.user.User;
import com.example.aibouauth.core.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PurchaseMapperTest {

    private PurchaseMapper mapper;
    private UserRepository userRepository;
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        productRepository = Mockito.mock(ProductRepository.class);
        mapper = new PurchaseMapper(userRepository, productRepository);
    }

    @Test
    void shouldMapPurchaseRequestToPurchase() {
        User user = new User(1, "test", "test@example.com", "123456789");
        when(userRepository.findUserById(1)).thenReturn(Optional.of(user));
        when(productRepository.findByName("Laptop")).thenReturn(new Product(1, "Laptop", new BigDecimal("1000"), "https://example.com/laptop.jpg", "L123", 10));

        ProductPurchaseRequest productPurchaseRequest = new ProductPurchaseRequest("Laptop", 1);
        PurchaseRequest purchaseRequest = new PurchaseRequest(1, List.of(productPurchaseRequest));

        Purchase purchase = mapper.toPurchase(purchaseRequest);

        assertEquals(user, purchase.getUser());
        assertEquals(1, purchase.getProducts().size());
        assertEquals("Laptop", purchase.getProducts().get(0).getName());
    }

    @Test
    void shouldThrowNullPointerExceptionWhenPurchaseRequestIsNull() {
        var exp = assertThrows(NullPointerException.class, () -> mapper.toPurchase(null));
        assertEquals("the purchase request should not be null", exp.getMessage());
    }

    @Test
    void shouldThrowBusinessExceptionWhenUserNotFound() {
        when(userRepository.findUserById(anyInt())).thenReturn(Optional.empty());


        PurchaseRequest request = new PurchaseRequest(1, Arrays.asList(new ProductPurchaseRequest("Product A",50)));


        assertNull(mapper.toPurchase(request));
    }

    @Test
    void shouldThrowBusinessExceptionWhenProductNotFound() {


            User mockUser = new User(1, "test", "test@example.com", "123456789");
            when(userRepository.findUserById(1)).thenReturn(Optional.of(mockUser));
            when(productRepository.findByName(anyString())).thenReturn(null);


            PurchaseRequest request = new PurchaseRequest(1, Arrays.asList(new ProductPurchaseRequest("NonExistingProduct",0)));

            assertNull(mapper.toPurchase(request));

    }

    @Test
    void shouldMapPurchaseToPurchaseResponse() {
        User user = new User(1, "test", "test@example.com", "123456789");
        Product product = new Product(1, "Laptop", new BigDecimal("1000"), "https://example.com/laptop.jpg", "L123", 10);

        Purchase purchase = Purchase.builder()
                .user(user)
                .products(List.of(product))
                .build();

        PurchaseResponse response = mapper.fromPurchase(purchase);

        assertNotNull(response);
        assertEquals(user.getUsername(), response.userResponse().name());
        assertEquals(1, response.products().size());
        assertEquals("Laptop", response.products().get(0).name());
    }


}
