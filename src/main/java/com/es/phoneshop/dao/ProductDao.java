package com.es.phoneshop.dao;

import com.es.phoneshop.model.Product;

import java.util.List;

public interface ProductDao extends Dao<Product> {
    List<Product> findProducts();

    List<Product> findProducts(String query);

    List<Product> findProducts(String query, String sortBy, String orderBy);
}
