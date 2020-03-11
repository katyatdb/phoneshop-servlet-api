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
import java.util.Arrays;

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
    private CartItem cartItem1;
    @Spy
    private CartItem cartItem2;
    @Spy
    private ArrayList<CartItem> cartItems;

    @InjectMocks
    private HttpSessionCartService cartService = HttpSessionCartService.getInstance();

    @Before
    public void setup() {
        cartItems.add(cartItem1);
        cartItem1.setQuantity(1);

        when(cart.getCartItems()).thenReturn(cartItems);
        when(cartItem1.getProduct()).thenReturn(product1);

        when(productDao.getProduct(1L)).thenReturn(product1);
        when(product1.getId()).thenReturn(1L);
        when(product1.getStock()).thenReturn(5);
        when(product1.getPrice()).thenReturn(new BigDecimal(100));
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
        cartItems.clear();
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
    public void testUpdateCart() throws OutOfStockException {
        cartService.update(cart, 1L, 3);

        assertEquals(3, cart.getCartItems().get(0).getQuantity());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateCartWithNegativeQuantity() throws OutOfStockException {
        cartService.update(cart, 1L, -5);
    }

    @Test(expected = OutOfStockException.class)
    public void testUpdateCartOutOfStock() throws OutOfStockException {
        cartService.update(cart, 1L, 6);
    }

    @Test
    public void testRecalculateTotalPrice() {
        when(cart.getCartItems()).thenReturn(Arrays.asList(cartItem1, cartItem2));
        when(cartItem2.getProduct()).thenReturn(product2);
        when(product2.getPrice()).thenReturn(new BigDecimal(200));
        when(cartItem1.getQuantity()).thenReturn(2);
        when(cartItem2.getQuantity()).thenReturn(3);

        // 2 * 100 + 3 * 200
        cartService.recalculateTotalPrice(cart);

        assertEquals(new BigDecimal(800), cart.getTotalPrice());
    }
}
