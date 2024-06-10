package com.example.aibouauth.core.purchase;


import com.example.aibouauth.core.product.ProductPurchaseRequest;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record PurchaseRequest(

        @NotNull(message = "Customer should be present")


        Integer userId,
        @NotEmpty(message = "You should at least purchase one product")
        List<ProductPurchaseRequest> products
) {
}
