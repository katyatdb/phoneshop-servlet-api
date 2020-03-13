package com.es.phoneshop.service.impl;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.model.Cart;
import com.es.phoneshop.model.CartItem;
import com.es.phoneshop.model.Order;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceImplTest {

    private static final String secureId = "abcd";
    private static final BigDecimal cartTotalPrice = BigDecimal.valueOf(100L);

    @Mock
    private OrderDao orderDao;
    @Mock
    private Order order1;
    @Mock
    private Order order2;
    @Mock
    private Order orderToSave;
    @Mock
    private Cart cart;

    @Spy
    private ArrayList<Order> orders;
    @Spy
    private ArrayList<CartItem> cartItems;

    @InjectMocks
    private OrderServiceImpl orderService = OrderServiceImpl.getInstance();

    @Before
    public void setup() {
        orders.addAll(Arrays.asList(order1, order2));
    }

    @Test
    public void testGetBySecureId() {
        when(orderDao.getBySecureId(secureId)).thenReturn(order1);

        assertEquals(order1, orderService.getBySecureId(secureId));
    }

    @Test
    public void testPlaceOrder() {
        when(cart.getCartItems()).thenReturn(cartItems);
        when(cart.getTotalPrice()).thenReturn(cartTotalPrice);
        when(orderDao.getAll()).thenReturn(orders);
        when(order2.getId()).thenReturn(2L);

        orderToSave = orderService.placeOrder(cart);

        assertEquals((Long) 3L, orderToSave.getId());
    }

}
