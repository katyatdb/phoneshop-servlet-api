package com.es.phoneshop.service;

import com.es.phoneshop.model.Product;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

public interface RecentlyViewedService {
    ArrayList<Product> getProducts(HttpServletRequest request);

    void add(HttpServletRequest request, Product product);
}
