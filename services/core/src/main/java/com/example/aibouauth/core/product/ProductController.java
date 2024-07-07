package com.example.aibouauth.core.product;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor

public class ProductController {


    private final ProductRepository repository;
    private final ProductService productService;




    @PostMapping("/product")
    public ResponseEntity<ProductPurchaseResponse> createProduct(@RequestBody ProductRequest request) {
        ProductPurchaseResponse product = productService.createProduct(request);
        return ResponseEntity.ok(product);
    }
    @GetMapping("/Products")
    List<Product>getAllProducts(){
        return  repository.findAll();
    }
    @GetMapping("/Product/{id}")
    Product getProductById(@PathVariable Integer id){
        return  repository.findById(id)
                .orElseThrow(()-> new ProductNotFoundException(id));
    }
    @PutMapping("/Product/{id}")
    Product updateProduct(@RequestBody ProductRequest newProduct,@PathVariable Integer id){
        return repository.findById(id)
                .map(product -> {
                    product.setRef(newProduct.ref());
                    product.setName(newProduct.name());
                    product.setPrice(newProduct.price());
                    product.setImages(newProduct.images());
                    product.setQuantity(newProduct.quantity());
               return  repository.save(product);
                }).orElseThrow(()-> new ProductNotFoundException(id));
    }
    @DeleteMapping("Product/{id}")
    String deleteProduct(@PathVariable  Integer id){
        if(!repository.existsById(id)){
            throw  new ProductNotFoundException(id);
        }
        repository.deleteById(id);
        return "Product with id "+id+ " has been deleted success";
    }
}
