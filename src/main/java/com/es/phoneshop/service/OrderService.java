package com.es.phoneshop.service;

import com.es.phoneshop.model.Cart;
import com.es.phoneshop.model.Order;

public interface OrderService {
    Order getBySecureId(String secureId);

    Order placeOrder(Cart cart);

    int getDeliveryCost();
}
