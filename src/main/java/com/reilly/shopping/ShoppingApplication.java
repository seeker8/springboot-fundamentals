package com.reilly.shopping;

import com.reilly.shopping.dao.ProductRepository;
import com.reilly.shopping.entities.Product;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.List;

@SpringBootApplication
public class ShoppingApplication {

  public static void main(String[] args) {
    SpringApplication.run(ShoppingApplication.class, args);
  }

  @Bean
  public CommandLineRunner initializeDatabase(ProductRepository productRepository) {
    return args -> {
      if (productRepository.count() == 0) {
        productRepository.saveAll(List.of(
          new Product("Ink Wand", BigDecimal.valueOf(3.99)),
          new Product("Conjuring Ink", BigDecimal.valueOf(3.99)),
          new Product("Palm Quire", BigDecimal.valueOf(3.99))
        )).forEach(System.out::println);
      }
    };
  }

}
