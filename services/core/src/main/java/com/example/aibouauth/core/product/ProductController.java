package com.example.aibouauth.core.product;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor

public class ProductController {

    @Autowired
    private ProductRepository repository;



    @PostMapping("/product")

    Product newProduct(@RequestBody Product newProduct){
        return repository.save(newProduct);
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
    Product updateProduct(@RequestBody Product newProduct,@PathVariable Integer id){
        return repository.findById(id)
                .map(product -> {
                    product.setRef(newProduct.getRef());
                    product.setName(newProduct.getName());
                    product.setPrice(newProduct.getPrice());
                    product.setImages(newProduct.getImages());
                    product.setQuantity(newProduct.getQuantity());
               return  repository.save(product);
                }).orElseThrow(()-> new ProductNotFoundException(id));
    }
    @DeleteMapping("Product/{id}")
    String deleteProduct(@PathVariable  Integer id){
        if(!repository.existsById(id)){
            throw  new ProductNotFoundException(id);
        }
        repository.deleteById(id);
        return "Product with id "+id+ "has been deleted success";
    }
}
