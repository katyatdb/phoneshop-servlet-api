package com.es.phoneshop.web;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.Cart;
import com.es.phoneshop.model.Product;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.RecentlyViewedService;
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
import java.util.Collections;
import java.util.Locale;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductDetailsPageServletTest {

    private static String productNotFoundMessage = "Product not found";

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private Product product;
    @Mock
    private ProductDao productDao;
    @Mock
    private CartService cartService;
    @Mock
    private Cart cart;
    @Mock
    private RecentlyViewedService recentlyViewedService;
    @Spy
    private ArrayList<Product> recentlyViewed;

    @InjectMocks
    private ProductDetailsPageServlet servlet = new ProductDetailsPageServlet();

    @Before
    public void setup() {
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getPathInfo()).thenReturn("/23");
        when(request.getLocale()).thenReturn(Locale.UK);
        when(cartService.getCart(request)).thenReturn(cart);
        when(recentlyViewedService.getProducts(request)).thenReturn(recentlyViewed);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        when(productDao.getProduct(23L)).thenReturn(product);
        when(recentlyViewedService.getProducts(request))
                .thenReturn(new ArrayList<>(Collections.singletonList(product)));

        servlet.doGet(request, response);

        verify(request).setAttribute("product", product);
        verify(request).setAttribute("recentlyViewed", recentlyViewedService.getProducts(request));
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoGetWithNullId() throws ServletException, IOException {
        when(productDao.getProduct(23L))
                .thenThrow(new IllegalArgumentException());

        servlet.doGet(request, response);

        verify(response).sendError(404, productNotFoundMessage);
    }

    @Test
    public void testDoGetWithWrongId() throws ServletException, IOException {
        when(productDao.getProduct(23L))
                .thenThrow(new NoSuchElementException());

        servlet.doGet(request, response);

        verify(response).sendError(404, productNotFoundMessage);
    }

    @Test
    public void testDoPost() throws ServletException, IOException {
        when(request.getParameter("quantity")).thenReturn("5");

        servlet.doPost(request, response);

        verify(response).sendRedirect(anyString());
    }

    @Test
    public void testDoPostParseException() throws ServletException, IOException {
        when(request.getParameter("quantity")).thenReturn("two");

        servlet.doPost(request, response);

        verify(request).setAttribute("error", "Value must be a number");
    }

    @Test
    public void testDoPostIllegalArgumentException() throws ServletException, IOException, OutOfStockException {
        String illegalArgumentMessage = "Invalid value";
        when(request.getParameter("quantity")).thenReturn("0");
        doThrow(new IllegalArgumentException(illegalArgumentMessage))
                .when(cartService).add(cart, 23L, 0);

        servlet.doPost(request, response);

        verify(request).setAttribute("error", illegalArgumentMessage);
    }

    @Test
    public void testDoPostOutOfStockException() throws ServletException, IOException, OutOfStockException {
        when(request.getParameter("quantity")).thenReturn("5");
        when(product.getStock()).thenReturn(1);
        String outOfStockMessage = "There are not enough products in stock. Number of products: " + product.getStock();
        doThrow(new OutOfStockException(outOfStockMessage))
                .when(cartService).add(cart, 23L, 5);

        servlet.doPost(request, response);

        verify(request).setAttribute("error", outOfStockMessage);
    }
}
