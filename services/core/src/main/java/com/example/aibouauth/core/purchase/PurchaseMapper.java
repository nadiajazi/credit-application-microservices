package com.example.aibouauth.core.purchase;

import com.example.aibouauth.core.exception.BusinessException;
import com.example.aibouauth.core.product.Product;
import com.example.aibouauth.core.product.ProductPurchaseRequest;
import com.example.aibouauth.core.product.ProductPurchaseResponse;
import com.example.aibouauth.core.product.ProductRepository;
import com.example.aibouauth.core.user.User;
import com.example.aibouauth.core.user.UserRepository;
import com.example.aibouauth.core.user.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseMapper {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public Purchase toPurchase(PurchaseRequest request) {
        if (request == null) {
            throw new NullPointerException("the purchase request should not be null");
        }

        Optional<User> userOptional = userRepository.findUserById(request.userId());
        if (userOptional.isEmpty()) {
            return null;
        }
        User user = userOptional.get();

        List<Product> products = request.products().stream()
                .map(productRequest -> {
                    Product product = productRepository.findByName(productRequest.productName());
                    return Optional.ofNullable(product);
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        if (products.size() != request.products().size()) {
            return null;
        }

        return Purchase.builder()
                .user(user)
                .products(products)
                .build();
    }


    public PurchaseResponse fromPurchase(Purchase purchase) {
        if (purchase == null) {
            throw new NullPointerException("the purchase request should not be null");

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