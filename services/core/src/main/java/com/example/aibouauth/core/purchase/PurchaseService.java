package com.example.aibouauth.core.purchase;

import com.example.aibouauth.core.exception.BusinessException;
import com.example.aibouauth.core.kafka.PurchaseConfirmation;
import com.example.aibouauth.core.kafka.PurchaseProducer;
import com.example.aibouauth.core.product.ProductPurchaseRequest;
import com.example.aibouauth.core.product.ProductRepository;
import com.example.aibouauth.core.user.User;
import com.example.aibouauth.core.user.UserRepository;
import com.example.aibouauth.core.user.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final UsersService userService;
    private final PurchaseMapper mapper;
    private final PurchaseProducer purchaseProducer;


    public List<PurchaseResponse> getUserPurchases(User user) {
        return purchaseRepository.findByUser(user).stream()
                .map(mapper::fromPurchase)
                .collect(Collectors.toList());
    }
    @Transactional
    public Integer createPurchase(PurchaseRequest request) {


        var customer = userRepository.findUserById(request.userId())
                .orElseThrow(() -> new BusinessException("Cannot create order: No customer exists with the provided ID"));

        BigDecimal totalAmount = BigDecimal.ZERO;

        // Create the Purchase entity
        Purchase purchase = mapper.toPurchase(request);
        purchase.setUser(customer);
        purchase.setCreatedDate(LocalDateTime.now());

        for (ProductPurchaseRequest productPurchaseRequest : request.products()) {
            var product = productRepository.findByName(productPurchaseRequest.productName());

            int availableQuantity = product.getQuantity();
            if (productPurchaseRequest.quantity() > availableQuantity) {
                throw new IllegalArgumentException("Purchase quantity exceeds available product quantity");
            }

            BigDecimal amount = product.getPrice().multiply(BigDecimal.valueOf(productPurchaseRequest.quantity()));
            totalAmount = totalAmount.add(amount);

            product.setQuantity(availableQuantity - productPurchaseRequest.quantity());
            productRepository.save(product);

            // Add the product to the purchase with the requested quantity
            purchase.addProduct(product);
        }

        // Set the total amount for the purchase
        purchase.setTotalAmount(totalAmount);

        // Update the user's total amount
        userService.updateMontant(customer, totalAmount);

        purchaseProducer.sendPurchaseConfirmation(
                new PurchaseConfirmation(
                        purchase.getTotalAmount(),
                        customer.getUsername(),
                        customer.getEmail(),
                        request.products()
                )
        );

        purchaseRepository.save(purchase);

        return purchase.getId();
    }

    public List<PurchaseResponse> findAllPurchases() {
        return this.purchaseRepository.findAll()
                .stream()
                .map(this.mapper::fromPurchase)
                .collect(Collectors.toList());
    }

    public PurchaseResponse findById(Integer id) {
        return this.purchaseRepository.findById(id)
                .map(this.mapper::fromPurchase)
                .orElseThrow(() -> new EntityNotFoundException(String.format("No purchase found with the provided ID: %d", id)));
    }

}