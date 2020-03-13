package com.es.phoneshop.service.impl;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.dao.impl.ArrayListOrderDao;
import com.es.phoneshop.model.Cart;
import com.es.phoneshop.model.Order;
import com.es.phoneshop.service.OrderService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderServiceImpl implements OrderService {

    private OrderDao orderDao;

    private OrderServiceImpl() {
        orderDao = ArrayListOrderDao.getInstance();
    }

    public static OrderServiceImpl getInstance() {
        return OrderServiceHolder.instance;
    }

    private static class OrderServiceHolder {
        private static final OrderServiceImpl instance = new OrderServiceImpl();
    }

    @Override
    public Order getBySecureId(String secureId) {
        return orderDao.getBySecureId(secureId);
    }

    @Override
    public Order placeOrder(Cart cart) {
        Order order = new Order();
        order.setCartItems(new ArrayList<>(cart.getCartItems()));
        order.setSubtotalPrice(cart.getTotalPrice());
        order.setDeliveryCost(new BigDecimal(15));
        order.setTotalPrice(order.getSubtotalPrice().add(order.getDeliveryCost()));

        long prevId;
        List<Order> orders = orderDao.getAll();

        if (!orders.isEmpty()) {
            prevId = orders.get(orders.size() - 1).getId();
        } else {
            prevId = 0L;
        }

        order.setId(++prevId);
        order.setSecureId(UUID.randomUUID().toString());
        orderDao.save(order);

        return order;
    }

    @Override
    public int getDeliveryCost() {
        return 15;
    }
}
