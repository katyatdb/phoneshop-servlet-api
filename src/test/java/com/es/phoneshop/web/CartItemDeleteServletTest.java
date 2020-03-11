package com.es.phoneshop.web;

import com.es.phoneshop.model.Cart;
import com.es.phoneshop.service.CartService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CartItemDeleteServletTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private CartService cartService;
    @Mock
    private Cart cart;

    @InjectMocks
    private CartItemDeleteServlet servlet = new CartItemDeleteServlet();

    @Before
    public void setup() {
        when(cartService.getCart(request)).thenReturn(cart);
    }

    @Test
    public void testDoPost() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/1");

        servlet.doPost(request, response);

        verify(cartService).delete(cart, 1L);
        verify(response).sendRedirect(anyString());
    }
}
