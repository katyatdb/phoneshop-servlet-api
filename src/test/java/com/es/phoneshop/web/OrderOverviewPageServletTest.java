package com.es.phoneshop.web;

import com.es.phoneshop.model.Order;
import com.es.phoneshop.service.OrderService;
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderOverviewPageServletTest {

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

    @InjectMocks
    private OrderOverviewPageServlet servlet = new OrderOverviewPageServlet();

    @Before
    public void setup() {
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getPathInfo()).thenReturn("/abcd");
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        when(orderService.getBySecureId("abcd")).thenReturn(order);

        servlet.doGet(request, response);

        verify(request).setAttribute("order", order);
        verify(requestDispatcher).forward(request, response);
    }
}
