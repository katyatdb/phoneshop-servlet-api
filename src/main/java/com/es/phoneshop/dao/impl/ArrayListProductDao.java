package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.enums.OrderBy;
import com.es.phoneshop.enums.SortBy;
import com.es.phoneshop.model.Product;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public class ArrayListProductDao implements ProductDao {
    private ArrayList<Product> products = new ArrayList<>();

    private ArrayListProductDao() {
    }

    public static ArrayListProductDao getInstance() {
        return ProductDaoHolder.instance;
    }

    private static class ProductDaoHolder {
        private static final ArrayListProductDao instance = new ArrayListProductDao();
    }

    @Override
    public synchronized Product getProduct(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Product id cannot be null");
        }

        return products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Product not found"));
    }

    @Override
    public synchronized List<Product> findProducts() {
        return products.stream()
                .filter(product -> product.getPrice() != null)
                .filter(product -> product.getStock() > 0)
                .collect(Collectors.toList());
    }

    @Override
    public synchronized List<Product> findProducts(String query) {
        if (query == null || query.isEmpty()) {
            return findProducts();
        }

        String[] words = query.toLowerCase().split(" ");

        Predicate<Product> hasMatch = product -> Arrays.stream(words)
                .anyMatch(word -> product.getDescription().toLowerCase().contains(word));

        ToIntFunction<Product> matchCount = product -> (int) Arrays.stream(words)
                .filter(word -> product.getDescription().toLowerCase().contains(word))
                .count();

        return products.stream()
                .filter(product -> product.getPrice() != null)
                .filter(product -> product.getStock() > 0)
                .filter(hasMatch)
                .sorted(Comparator.comparingInt(matchCount).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public synchronized List<Product> findProducts(String query, String sort, String order) {
        List<Product> products = findProducts(query);
        if (sort == null || sort.isEmpty()) {
            return products;
        }

        OrderBy orderBy;
        if (order == null || order.isEmpty()) {
            orderBy = OrderBy.ASC;
        } else {
            orderBy = OrderBy.valueOf(order.toUpperCase());
        }

        SortBy sortBy = SortBy.valueOf(sort.toUpperCase());
        Comparator<Product> comparator = null;

        switch (sortBy) {
            case PRICE:
                comparator = Comparator.comparing(Product::getPrice);
                break;
            case DESCRIPTION:
                comparator = Comparator.comparing(product -> product.getDescription().toLowerCase());
                break;
        }

        if (orderBy == OrderBy.DESC) {
            comparator = comparator.reversed();
        }

        return products.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    @Override
    public synchronized void save(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (product.getId() == null) {
            throw new IllegalArgumentException("Product id cannot be null");
        }

        boolean noneMatchEqualId = products.stream()
                .noneMatch(product1 -> product1.getId().equals(product.getId()));

        if (noneMatchEqualId) {
            products.add(product);
        } else {
            throw new IllegalArgumentException("Product with such id is already exist");
        }
    }

    @Override
    public synchronized void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Product id cannot be null");
        }

        boolean isDeleted = products.removeIf(product -> product.getId().equals(id));
        if (!isDeleted) {
            throw new NoSuchElementException("Product with such id doesn't exist");
        }
    }
}
