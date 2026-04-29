package com.reilly.shopping.controllers;

import com.reilly.shopping.entities.Product;
import com.reilly.shopping.services.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductRestController {

  private final ProductService productService;

  public ProductRestController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping
  public List<Product> findAllProducts() {
    return productService.findAllProducts();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Product> findProductById(@PathVariable Long id) {
    return ResponseEntity.of(productService.findProductById(id));
  }

  @PostMapping
  public ResponseEntity<Product> saveProduct(@RequestBody Product product) {
    Product p = productService.save(product);
    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
      .path("/{id}")
      .buildAndExpand(p.getId())
      .toUri();
    return ResponseEntity.created(location).body(p);
  }
}
