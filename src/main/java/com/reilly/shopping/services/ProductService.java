package com.reilly.shopping.services;

import com.reilly.shopping.dao.ProductRepository;
import com.reilly.shopping.entities.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService {

  private final ProductRepository productRepository;

  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public void initializeDatabase() {
    if (productRepository.count() == 0) {
      productRepository.saveAll(List.of(
        new Product("Ink Wand", BigDecimal.valueOf(23.99)),
        new Product("Conjuring Ink", BigDecimal.valueOf(3.99)),
        new Product("Palm Quire", BigDecimal.valueOf(10.99))
      )).forEach(System.out::println);
    }
  }

  public List<Product> findAllProducts() {
    return productRepository.findAll();
  }

  public Optional<Product> findProductById(Long id) {
    return productRepository.findById(id);
  }
}
