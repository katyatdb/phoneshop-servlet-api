package com.es.phoneshop.dao;

import com.es.phoneshop.model.Order;

public interface OrderDao extends Dao<Order> {
    Order getBySecureId(String id);
}
