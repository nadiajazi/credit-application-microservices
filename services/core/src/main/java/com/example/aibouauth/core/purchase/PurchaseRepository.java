package com.example.aibouauth.core.purchase;

import com.example.aibouauth.core.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase,Integer> {
        List<Purchase> findByUser(User user);




}
