package com.es.phoneshop.dao;

import com.es.phoneshop.model.Product;

import java.util.List;

public interface ProductDao {
    Product getProduct(Long id);

    List<Product> findProducts();

    List<Product> findProducts(String query);

    List<Product> findProducts(String query, String sortBy, String orderBy);

    void save(Product product);

    void delete(Long id);
}
