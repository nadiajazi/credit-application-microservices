package com.example.aibouauth.core.product;

import com.example.aibouauth.core.product.ProductRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;
    @Transactional
    public ProductPurchaseResponse createProduct(ProductRequest request) {
        Product product = mapper.toProduct(request);
        Product savedProduct = repository.save(product);
        return mapper.toProductPurchaseResponse(savedProduct);
    };
    @Transactional
    public List<ProductPurchaseResponse> purchaseProducts(
            List<ProductPurchaseRequest> request
    ) {

        var productNames = request
                .stream()
                .map(ProductPurchaseRequest::productName)
                .toList();


        var storedProducts = repository.findAllByNameInOrderByName(productNames);


        if (productNames.size() != storedProducts.size()) {
            throw new ProductPurchaseException("One or more products does not exist");
        }


        var sortedRequest = request
                .stream()
                .sorted(Comparator.comparing(ProductPurchaseRequest::productName))
                .toList();

        var purchasedProducts = new ArrayList<ProductPurchaseResponse>();

        for (int i = 0; i < storedProducts.size(); i++) {
            var product = storedProducts.get(i);
            var productRequest = sortedRequest.get(i);


            if (product.getQuantity() < productRequest.quantity()) {
                throw new ProductPurchaseException("Insufficient stock quantity for product with name: " + productRequest.productName());
            }


            var newAvailableQuantity = product.getQuantity() - productRequest.quantity();
            product.setQuantity(newAvailableQuantity);
            repository.save(product);


            purchasedProducts.add(mapper.toProductPurchaseResponse(product));
        }

        return purchasedProducts;
    }
}
