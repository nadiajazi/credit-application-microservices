package com.example.aibouauth.core.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
class ProductControllerTest {
    @Mock
    private ProductRepository repository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testCreateProduct() throws Exception {
        ProductRequest productRequest = new ProductRequest("Product 1", BigDecimal.valueOf(100.0), "image1", "ref1", 10);
        ProductPurchaseResponse productPurchaseResponse = new ProductPurchaseResponse(1, "Product 1", BigDecimal.valueOf(100.0));

        when(productService.createProduct(any(ProductRequest.class))).thenReturn(productPurchaseResponse);

        mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(1))
                .andExpect(jsonPath("$.name").value("Product 1"))
                .andExpect(jsonPath("$.price").value(100.0));

        verify(productService, times(1)).createProduct(any(ProductRequest.class));
    }

    @Test
    public void testGetAllProducts() throws Exception {
        List<Product> products = new ArrayList<>();
        products.add(new Product(1, "Product 1", BigDecimal.valueOf(100.0), "image1", "ref1", 10));
        products.add(new Product(2, "Product 2", BigDecimal.valueOf(200.0), "image2", "ref2", 20));

        when(repository.findAll()).thenReturn(products);

        mockMvc.perform(get("/Products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Product 1"))
                .andExpect(jsonPath("$[1].name").value("Product 2"));

        verify(repository, times(1)).findAll();
    }

    @Test
    public void testGetProductById() throws Exception {
        Product product = new Product(1, "Product 1", BigDecimal.valueOf(100.0), "image1", "ref1", 10);

        when(repository.findById(1)).thenReturn(Optional.of(product));

        mockMvc.perform(get("/Product/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Product 1"));

        verify(repository, times(1)).findById(1);
    }

    @Test
    public void testUpdateProduct() throws Exception {
        Product product = new Product(1, "Product 1", BigDecimal.valueOf(100.0), "image1", "ref1", 10);
        ProductRequest newProduct = new ProductRequest("Product 1 Updated", BigDecimal.valueOf(150.0), "image1_updated", "ref1_updated", 15);

        when(repository.findById(1)).thenReturn(Optional.of(product));
        when(repository.save(any(Product.class))).thenReturn(new Product(1, "Product 1 Updated", BigDecimal.valueOf(150.0), "image1_updated", "ref1_updated", 15));

        mockMvc.perform(put("/Product/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Product 1 Updated"))
                .andExpect(jsonPath("$.price").value(150.0))
                .andExpect(jsonPath("$.images").value("image1_updated"))
                .andExpect(jsonPath("$.quantity").value(15));

        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).save(any(Product.class));
    }

    @Test
    public void testDeleteProduct() throws Exception {
        when(repository.existsById(1)).thenReturn(true);

        mockMvc.perform(delete("/Product/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Product with id 1 has been deleted success"));

        verify(repository, times(1)).existsById(1);
        verify(repository, times(1)).deleteById(1);
    }

    @Test
    public void testDeleteProductNotFound() throws Exception {

        int nonExistentProductId = 1;
        when(repository.existsById(nonExistentProductId)).thenReturn(false);


        mockMvc.perform(delete("/Product/{id}", nonExistentProductId))
                .andExpect(status().isNotFound());

        verify(repository, times(1)).existsById(nonExistentProductId);
        verify(repository, never()).deleteById(nonExistentProductId);
    }


}