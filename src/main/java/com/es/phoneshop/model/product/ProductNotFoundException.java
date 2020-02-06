package com.es.phoneshop.model.product;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException() {
        super();
    }

    public ProductNotFoundException(String message) {
        super(message);
    }
}
