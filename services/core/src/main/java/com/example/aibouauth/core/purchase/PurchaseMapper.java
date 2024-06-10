package com.example.aibouauth.core.purchase;

import com.example.aibouauth.core.exception.BusinessException;
import com.example.aibouauth.core.product.Product;
import com.example.aibouauth.core.product.ProductPurchaseRequest;
import com.example.aibouauth.core.product.ProductPurchaseResponse;
import com.example.aibouauth.core.product.ProductRepository;
import com.example.aibouauth.core.user.UserRepository;
import com.example.aibouauth.core.user.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseMapper {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public Purchase toPurchase(PurchaseRequest request) {
        if (request == null) {
            return null;
        }

        var user = userRepository.findUserById(request.userId())
                .orElseThrow(() -> new BusinessException("User not found"));

        List<Product> products = request.products().stream()
                .map(productRequest -> {
                    Product product = productRepository.findByName(productRequest.productName());
                    if (product == null) {
                        throw new BusinessException("Product not found: " + productRequest.productName());
                    }
                    return product;
                })
                .collect(Collectors.toList());

        return Purchase.builder()
                .user(user)
                .products(products)
                .build();
    }

    public PurchaseResponse fromPurchase(Purchase purchase) {
        if (purchase == null) {
            return null;
        }

        List<ProductPurchaseResponse> productResponses = purchase.getProducts().stream()
                .map(product -> new ProductPurchaseResponse(
                        product.getId(),
                        product.getName(),
                        product.getPrice()
                ))
                .collect(Collectors.toList());

        UserResponse userResponse = new UserResponse(
                purchase.getUser().getUsername(),
                purchase.getUser().getPhone()
        );

        return new PurchaseResponse(
                purchase.getId(),
                purchase.getTotalAmount(),
                userResponse,
                productResponses,
                purchase.getCreatedDate()
        );
    }
}
