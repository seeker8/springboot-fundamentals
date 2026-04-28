package com.reilly.shopping.dao;

import com.reilly.shopping.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
