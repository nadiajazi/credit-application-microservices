package com.example.aibouauth.core.product;

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
    public List<ProductPurchaseResponse> purchaseProducts(
            List<ProductPurchaseRequest> request
    ) {
        // Extract product names from the request
        var productNames = request
                .stream()
                .map(ProductPurchaseRequest::productName)
                .toList();

        // Fetch products by names
        var storedProducts = repository.findAllByNameInOrderByName(productNames);

        // Check if all requested products exist
        if (productNames.size() != storedProducts.size()) {
            throw new ProductPurchaseException("One or more products does not exist");
        }

        // Sort the request by product names
        var sortedRequest = request
                .stream()
                .sorted(Comparator.comparing(ProductPurchaseRequest::productName))
                .toList();

        var purchasedProducts = new ArrayList<ProductPurchaseResponse>();

        for (int i = 0; i < storedProducts.size(); i++) {
            var product = storedProducts.get(i);
            var productRequest = sortedRequest.get(i);

            // Check if sufficient stock is available
            if (product.getQuantity() < productRequest.quantity()) {
                throw new ProductPurchaseException("Insufficient stock quantity for product with name: " + productRequest.productName());
            }

            // Update product quantity
            var newAvailableQuantity = product.getQuantity() - productRequest.quantity();
            product.setQuantity(newAvailableQuantity);
            repository.save(product);

            // Map to response
            purchasedProducts.add(mapper.toProductPurchaseResponse(product));
        }

        return purchasedProducts;
    }
}
