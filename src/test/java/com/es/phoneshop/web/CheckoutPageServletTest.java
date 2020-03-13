package com.es.phoneshop.web;

import com.es.phoneshop.model.Cart;
import com.es.phoneshop.model.CartItem;
import com.es.phoneshop.model.Order;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.OrderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CheckoutPageServletTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private OrderService orderService;
    @Mock
    private Order order;
    @Mock
    private CartService cartService;
    @Mock
    private Cart cart;
    @Spy
    private ArrayList<CartItem> cartItems;

    @InjectMocks
    private CheckoutPageServlet servlet = new CheckoutPageServlet();

    @Before
    public void setup() {
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(cartService.getCart(request)).thenReturn(cart);
        when(cart.getCartItems()).thenReturn(cartItems);
        when(orderService.placeOrder(cart)).thenReturn(order);

        when(request.getParameter("firstName")).thenReturn("fisrtName");
        when(request.getParameter("lastName")).thenReturn("lastName");
        when(request.getParameter("phoneNumber")).thenReturn("375297852572");
        when(request.getParameter("deliveryDate")).thenReturn("13.05.2020");
        when(request.getParameter("deliveryAddress")).thenReturn("delivery address");
        when(request.getParameter("paymentMethod")).thenReturn("cash");
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        when(orderService.getDeliveryCost()).thenReturn(15);

        servlet.doGet(request, response);

        verify(request).setAttribute("cart", cart);
        verify(request).setAttribute("deliveryCost", orderService.getDeliveryCost());
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPost() throws ServletException, IOException {
        servlet.doPost(request, response);

        verify(request).setAttribute("order", order);
        verify(response).sendRedirect(anyString());
    }

    @Test
    public void testDoPostEmptyFields() throws ServletException, IOException {
        when(request.getParameter("lastName")).thenReturn("");

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("error"), anyString());
    }

    @Test
    public void testDoPostWrongDateFormat() throws ServletException, IOException {
        when(request.getParameter("deliveryDate")).thenReturn("12-06-20");

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("error"), anyString());
    }
}
