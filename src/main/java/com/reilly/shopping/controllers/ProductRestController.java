package com.reilly.shopping.controllers;

import com.reilly.shopping.entities.Product;
import com.reilly.shopping.exceptions.ProductMinimumPriceException;
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

  @GetMapping(params = "min")
  public List<Product> getProductsByMinPrice(@RequestParam(defaultValue = "0.0") double min) {
    if(min < 0 ) throw new ProductMinimumPriceException(min);
    return productService.findAllProductsByMinPrice(min);
  }

  @PostMapping
  public ResponseEntity<Product> saveProduct(@RequestBody Product product) {
    Product p = productService.saveProduct(product);
    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
      .path("/{id}")
      .buildAndExpand(p.getId())
      .toUri();
    return ResponseEntity.created(location).body(p);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
    return productService.findProductById(id)
      .map(p -> {
        p.setName(product.getName());
        p.setPrice(product.getPrice());
        return ResponseEntity.ok(productService.saveProduct(p));
      })
      .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
    return productService.findProductById(id)
      .map(p -> {
        productService.deleteProduct(p);
        return ResponseEntity.noContent().build();
      })
      .orElse(ResponseEntity.notFound().build());

  }
}
