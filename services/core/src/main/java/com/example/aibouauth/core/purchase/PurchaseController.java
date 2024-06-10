package com.example.aibouauth.core.purchase;


import com.example.aibouauth.core.user.User;
import com.example.aibouauth.core.user.UsersService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private UsersService userService;

    private static final Logger logger = LoggerFactory.getLogger(PurchaseController.class);


    @GetMapping("/admin/allpurchases")
    public ResponseEntity<List<PurchaseResponse>> findAll() {
        return ResponseEntity.ok(this.purchaseService.findAllPurchases());
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<PurchaseResponse>> getClientPurchases(@PathVariable Integer clientId) {
        User client = userService.getUserById(clientId);
        List<PurchaseResponse> purchases = purchaseService.getUserPurchases(client);
        return ResponseEntity.ok(purchases);
    }

    @GetMapping("/admin/{purchase-id}")
    public ResponseEntity<PurchaseResponse> findById(
            @PathVariable("purchase-id") Integer orderId
    ) {
        return ResponseEntity.ok(this.purchaseService.findById(orderId));
    }

    @PostMapping("/admin")
    public ResponseEntity<Integer> createPurchase(
            @RequestBody @Valid PurchaseRequest request
    ) {
        return ResponseEntity.ok(purchaseService.createPurchase(request));
    }
}
