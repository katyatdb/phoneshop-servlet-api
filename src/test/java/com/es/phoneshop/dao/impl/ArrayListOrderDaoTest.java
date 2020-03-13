package com.es.phoneshop.dao.impl;

import com.es.phoneshop.model.Order;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ArrayListOrderDaoTest {

    @Mock
    private Order order1;
    @Mock
    private Order order2;
    @Mock
    private Order orderToSave;
    @Spy
    private ArrayList<Order> orders;

    @InjectMocks
    private ArrayListOrderDao orderDao = ArrayListOrderDao.getInstance();

    @Before
    public void setup() {
        orders.addAll(Arrays.asList(order1, order2));
        when(order1.getSecureId()).thenReturn("secure1");
        when(order2.getSecureId()).thenReturn("secure2");
    }

    @Test
    public void testGetAllOrders() {
        assertEquals(Arrays.asList(order1, order2), orderDao.getAll());
    }

    @Test
    public void testSaveOrder() {
        when(order1.getId()).thenReturn(1L);
        when(order2.getId()).thenReturn(2L);
        when(orderToSave.getId()).thenReturn(3L);

        orderDao.save(orderToSave);

        assertEquals(3, orders.size());
    }

    @Test
    public void testGetBySecureId() {
        assertEquals(order2, orderDao.getBySecureId("secure2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNullSecureId() {
        orderDao.getBySecureId(null);
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetByWrongSecureId() {
        orderDao.getBySecureId("secure3");
    }
}
