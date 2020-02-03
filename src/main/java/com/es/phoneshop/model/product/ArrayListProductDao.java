package com.es.phoneshop.model.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class ArrayListProductDao implements ProductDao {
    private static ArrayListProductDao instance;
    private volatile ArrayList<Product> products = new ArrayList<>();

    private ArrayListProductDao() {
    }

    public static ArrayListProductDao getInstance() {
        if (instance == null) {
            synchronized (ArrayListProductDao.class) {
                if (instance == null) {
                    instance = new ArrayListProductDao();
                }
            }
        }

        return instance;
    }

    public synchronized void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    @Override
    public synchronized Product getProduct(Long id) throws Exception {
        Optional<Product> tmpProduct = products.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst();

        if (!tmpProduct.isPresent()) {
            throw new Exception("Product not found");
        }

        return tmpProduct.get();
    }

    @Override
    public synchronized List<Product> findProducts() {
        return products.stream()
                .filter(product -> product.getPrice() != null && product.getStock() > 0)
                .collect(Collectors.toList());
    }

    @Override
    public synchronized void save(Product product) {
        if (product != null && products.stream().noneMatch(product1 -> product1.getId().equals(product.getId()))) {
            products.add(product);
        }
    }

    @Override
    public synchronized void delete(Long id) {
        products.removeIf(product -> product.getId().equals(id));
    }
}
