package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.AbstractDao;
import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.model.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class ArrayListOrderDao extends AbstractDao<Order> implements OrderDao {
    private ArrayList<Order> orders;

    private ArrayListOrderDao() {
        orders = new ArrayList<>();
        super.init(orders);
    }

    public static ArrayListOrderDao getInstance() {
        return OrderDaoHolder.instance;
    }

    private static class OrderDaoHolder {
        private static final ArrayListOrderDao instance = new ArrayListOrderDao();
    }

    @Override
    public synchronized List<Order> getAll() {
        return super.getAll();
    }

    @Override
    public synchronized void save(Order order) {
        super.save(order);
    }

    @Override
    public Order getBySecureId(String secureId) {
        if (secureId == null) {
            throw new IllegalArgumentException();
        }

        return orders.stream()
                .filter(item -> item.getSecureId().equals(secureId))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }
}
