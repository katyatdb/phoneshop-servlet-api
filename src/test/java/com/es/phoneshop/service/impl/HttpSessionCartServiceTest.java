package com.es.phoneshop.service.impl;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.Cart;
import com.es.phoneshop.model.CartItem;
import com.es.phoneshop.model.Product;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class HttpSessionCartServiceTest {
    private static final String SESSION_CART_KEY = "cartKey";

    @Mock
    private ProductDao productDao;
    @Mock
    private Product product1;
    @Mock
    private Product product2;
    @Mock
    private HttpSession session;
    @Mock
    private HttpServletRequest request;

    @Spy
    private Cart cart;
    @Spy
    private ArrayList<CartItem> cartItems;

    @InjectMocks
    private HttpSessionCartService cartService = HttpSessionCartService.getInstance();

    @Before
    public void setup() {
        when(cart.getCartItems()).thenReturn(cartItems);
        when(productDao.getProduct(1L)).thenReturn(product1);
        when(productDao.getProduct(2L)).thenReturn(product2);

        when(product1.getId()).thenReturn(1L);
        when(product1.getStock()).thenReturn(5);
        when(product1.getPrice()).thenReturn(new BigDecimal(100));

        when(product2.getStock()).thenReturn(5);
        when(product2.getPrice()).thenReturn(new BigDecimal(200));
    }

    @Test
    public void testGetCart() {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(SESSION_CART_KEY)).thenReturn(null);

        cartService.getCart(request);

        verify(session).setAttribute(eq(SESSION_CART_KEY), any(Cart.class));
    }

    @Test
    public void testAddProduct() throws OutOfStockException {
        cartService.add(cart, 1L, 1);

        assertEquals(1, cart.getCartItems().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddProductWithNegativeQuantity() throws OutOfStockException {
        cartService.add(cart, 1L, -1);
    }

    @Test
    public void testAddProductsWithEqualId() throws OutOfStockException {
        cartService.add(cart, 1L, 1);
        cartService.add(cart, 1L, 1);

        assertEquals(1, cart.getCartItems().size());
        assertEquals(2, cart.getCartItems().get(0).getQuantity());
    }

    @Test(expected = OutOfStockException.class)
    public void testAddProductOutOfStock() throws OutOfStockException {
        cartService.add(cart, 1L, 8);
    }

    @Test
    public void testTotalPrice() throws OutOfStockException {
        cartService.add(cart, 1L, 1);
        cartService.add(cart, 2L, 2);

        assertEquals(new BigDecimal(500), cart.getTotalPrice());
    }
}
