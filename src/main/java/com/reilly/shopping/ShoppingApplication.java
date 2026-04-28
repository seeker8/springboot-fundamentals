package com.reilly.shopping;

import com.reilly.shopping.dao.ProductRepository;
import com.reilly.shopping.entities.Product;
import com.reilly.shopping.service.ProductService;
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
  public CommandLineRunner initializeDatabase(ProductService productService) {
    return args -> productService.initializeDatabase();
  }

}
