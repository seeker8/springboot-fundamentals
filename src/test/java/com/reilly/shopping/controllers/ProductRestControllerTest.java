package com.reilly.shopping.controllers;

import com.reilly.shopping.entities.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureWebTestClient
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class ProductRestControllerTest {

  @Autowired
  private WebTestClient client;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  private List<Long> getIds() {
    return jdbcTemplate.queryForList("SELECT id FROM product", Long.class);
  }

  private Product getProduct(Long id) {
    return jdbcTemplate.queryForObject("select * from product where id =?",
      (rs, num) -> new Product(rs.getLong("id"),
        rs.getString("name"),
        rs.getBigDecimal("price")), id);
  }

  @Test
  void getAllProducts() {
    List<Long> productIds = getIds();
    client.get()
      .uri("/products")
      .exchange()
      .expectStatus().isOk()
      .expectBodyList(Product.class).hasSize(3)
      .consumeWith(System.out::println);
  }

  @ParameterizedTest(name = "Product ID: {0}")
  @MethodSource("getIds")
  void getProductsThatExist(Long id) {
    client.get()
      .uri("/products/{id}", id)
      .exchange()
      .expectStatus().isOk()
      .expectBody()
      .jsonPath("$.id").isEqualTo(id);
  }

  @Test
  void getProductThatDoesNotExist() {
    List<Long> productIds = getIds();
    assertThat(productIds).doesNotContain(999L);
    client.get()
      .uri("/products/{id}", 999L)
      .exchange()
      .expectStatus().isNotFound();
  }

  @Test
  void insertProduct() {
    List<Long> productIds = getIds();
    assertThat(productIds).doesNotContain(999L);
    Product product = new Product("Searneedle Wand", BigDecimal.valueOf(300.50));
    client.post()
      .uri("/products")
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON)
      .body(Mono.just(product), Product.class)
      .exchange()
      .expectStatus().isCreated()
      .expectBody()
      .jsonPath("$.id").isNotEmpty()
      .jsonPath("$.name").isEqualTo("Searneedle Wand")
      .jsonPath("$.price").isEqualTo(300.50);
  }

  @Test
  void updateProduct() {
    Product product = getProduct(getIds().get(0));
    product.setPrice(product.getPrice().add(BigDecimal.ONE));

    client.put()
      .uri("/products", product.getId())
      .body(Mono.just(product), Product.class)
      .exchange()
      .expectStatus().isOk()
      .expectBody(Product.class)
      .consumeWith(System.out::println);
  }

  @Test
  void deleteAllProducts() {
    List<Long> products = getIds();

    client.delete()
      .uri("/products")
      .exchange()
      .expectStatus().isNoContent();

    client.get()
      .uri("/products")
      .exchange()
      .expectBodyList(Product.class).hasSize(0);
  }
}
