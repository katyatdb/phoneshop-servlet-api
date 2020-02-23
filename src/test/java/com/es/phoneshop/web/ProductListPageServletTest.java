package com.es.phoneshop.web;

import com.es.phoneshop.model.Product;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.service.RecentlyViewedService;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductListPageServletTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private Product product1;
    @Mock
    private Product product2;
    @Mock
    private ProductDao productDao;
    @Mock
    private RecentlyViewedService recentlyViewedService;

    @InjectMocks
    private ProductListPageServlet servlet = new ProductListPageServlet();

    private String query = "sony";
    private String sortBy = "price";
    private String orderBy = "desc";

    @Before
    public void setup() {
        when(request.getParameter("query")).thenReturn(query);
        when(request.getParameter("sortBy")).thenReturn(sortBy);
        when(request.getParameter("orderBy")).thenReturn(orderBy);

        when(recentlyViewedService.getProducts(request))
                .thenReturn(new ArrayList<>(Collections.singletonList(product1)));

        when(productDao.findProducts(query, sortBy, orderBy)).thenReturn(Arrays.asList(product1, product2));
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(request).setAttribute("recentlyViewed", recentlyViewedService.getProducts(request));
        verify(request).setAttribute("products", productDao.findProducts(query, sortBy, orderBy));
        verify(requestDispatcher).forward(request, response);
    }
}
