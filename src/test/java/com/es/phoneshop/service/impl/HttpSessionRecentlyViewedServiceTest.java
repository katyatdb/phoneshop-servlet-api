package com.es.phoneshop.service.impl;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HttpSessionRecentlyViewedServiceTest {
    private static final String SESSION_RECENTLY_VIEWED_KEY = "recentlyViewedKey";

    @Mock
    private HttpSession session;
    @Mock
    private HttpServletRequest request;
    @Mock
    private Product product1;
    @Mock
    private Product product2;

    @Spy
    private ArrayList<Product> recentlyViewed;

    @InjectMocks
    private HttpSessionRecentlyViewedService recentlyViewedService =
            HttpSessionRecentlyViewedService.getInstance();

    @Before
    public void setup() {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(SESSION_RECENTLY_VIEWED_KEY)).thenReturn(recentlyViewed);
        recentlyViewed.add(product1);
    }

    @Test
    public void testGetProducts() {
        when(session.getAttribute(SESSION_RECENTLY_VIEWED_KEY)).thenReturn(null);

        recentlyViewedService.getProducts(request);

        verify(session).setAttribute(eq(SESSION_RECENTLY_VIEWED_KEY), any());
    }

    @Test
    public void testAddProduct() {
        recentlyViewedService.add(request, product2);

        assertEquals(Arrays.asList(product2, product1), recentlyViewed);
    }

    @Test
    public void testAddEqualProduct() {
        recentlyViewedService.add(request, product1);

        assertEquals(Collections.singletonList(product1), recentlyViewed);
    }

    @Test
    public void testAddProductToFullCart() {
        recentlyViewed.addAll(Arrays.asList(product1, product1));

        recentlyViewedService.add(request, product2);

        assertEquals(Arrays.asList(product2, product1, product1), recentlyViewed);
    }
}
