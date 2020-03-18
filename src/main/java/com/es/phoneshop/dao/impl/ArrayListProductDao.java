package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.AbstractDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.enums.OrderBy;
import com.es.phoneshop.enums.SortBy;
import com.es.phoneshop.enums.WordCount;
import com.es.phoneshop.model.Product;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArrayListProductDao extends AbstractDao<Product> implements ProductDao {
    private ArrayList<Product> products;

    private ArrayListProductDao() {
        products = new ArrayList<>();
        super.init(products);
    }

    public static ArrayListProductDao getInstance() {
        return ProductDaoHolder.instance;
    }

    private static class ProductDaoHolder {
        private static final ArrayListProductDao instance = new ArrayListProductDao();
    }

    @Override
    public synchronized <E> Product get(E id) {
        return super.get(id);
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
    public synchronized List<Product> findProducts(String description, String wordCountStr, BigDecimal minPrice, BigDecimal maxPrice) {
        boolean isNullQuery = Stream.of(description, wordCountStr, minPrice, maxPrice)
                .allMatch(Objects::isNull);

        if (isNullQuery) {
            return new ArrayList<>();
        }

        WordCount wordCount = WordCount.valueOf(wordCountStr.toUpperCase().replace(" ", "_"));

        if (description == null || description.isEmpty()) {
            return products.stream()
                    .filter(product -> product.getPrice() != null)
                    .filter(product -> product.getStock() > 0)
                    .filter(minPrice != null ? product -> product.getPrice().compareTo(minPrice) >= 0 : product -> true)
                    .filter(maxPrice != null ? product -> product.getPrice().compareTo(maxPrice) <= 0 : product -> true)
                    .collect(Collectors.toList());

        }

        String[] words = description.toLowerCase().split(" ");
        Predicate<Product> match;

        if (wordCount == WordCount.ANY_WORD) {
            match = product -> Arrays.stream(words)
                    .anyMatch(word -> product.getDescription().toLowerCase().contains(word));
        } else {
            match = product -> Arrays.stream(words)
                    .allMatch(word -> product.getDescription().toLowerCase().contains(word));
        }

        return products.stream()
                .filter(product -> product.getPrice() != null)
                .filter(product -> product.getStock() > 0)
                .filter(match)
                .filter(minPrice != null ? product -> product.getPrice().compareTo(minPrice) >= 0 : product -> true)
                .filter(maxPrice != null ? product -> product.getPrice().compareTo(maxPrice) <= 0 : product -> true)
                .collect(Collectors.toList());
    }

    @Override
    public synchronized void save(Product product) {
        super.save(product);
    }

    @Override
    public synchronized <E> void delete(E id) {
        super.delete(id);
    }
}
