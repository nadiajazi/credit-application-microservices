package com.example.aibouauth.core.product;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Integer> {
    Product findByName(String name);
    List<Product> findAllByNameInOrderByName(List<String> names);

}
