package com.example.aibouauth.core.product;

import com.example.aibouauth.core.category.Category;
import com.example.aibouauth.core.purchase.Purchase;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private BigDecimal price;
    private String images;
    private String ref;
    private Integer quantity;
    @ManyToMany(mappedBy = "products")
    private Set<Purchase> purchases = new HashSet<>();
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public Product(Integer id, String name, BigDecimal price, String images, String ref, Integer quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.images = images;
        this.ref = ref;
        this.quantity = quantity;
    }

    public Product(String name, BigDecimal price, String images, String ref, int quantity) {

        this.name = name;
        this.price = price;
        this.images = images;
        this.ref = ref;
        this.quantity = quantity;
    }
}


