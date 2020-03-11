package com.es.phoneshop.web;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.Cart;
import com.es.phoneshop.service.CartService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CartPageServletTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private CartService cartService;
    @Mock
    private Cart cart;

    @InjectMocks
    private CartPageServlet servlet = new CartPageServlet();

    @Before
    public void setup() {
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(cartService.getCart(request)).thenReturn(cart);
        when(request.getParameterValues("quantity")).thenReturn(new String[]{"1", "2"});
        when(request.getParameterValues("productId")).thenReturn(new String[]{"1", "2"});
        when(request.getLocale()).thenReturn(Locale.UK);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(request).setAttribute("cart", cart);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPost() throws ServletException, IOException {
        servlet.doPost(request, response);

        verify(response).sendRedirect(anyString());
    }

    @Test
    public void testDoPostParseException() throws ServletException, IOException {
        when(request.getParameterValues("quantity")).thenReturn(new String[]{"one", "2"});

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("errors"), anyMap());
    }

    @Test
    public void testDoPostIllegalArgumentException() throws ServletException, IOException, OutOfStockException {
        when(request.getParameterValues("quantity")).thenReturn(new String[]{"-1", "1"});
        doThrow(new IllegalArgumentException("Invalid value"))
                .when(cartService).update(cart, 1L, -1);

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("errors"), anyMap());
    }

    @Test
    public void testDoPostOutOfStockException() throws ServletException, IOException, OutOfStockException {
        when(request.getParameterValues("quantity")).thenReturn(new String[]{"5", "1"});
        doThrow(new OutOfStockException("Out od stock"))
                .when(cartService).update(cart, 1L, 5);

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("errors"), anyMap());
    }
}
