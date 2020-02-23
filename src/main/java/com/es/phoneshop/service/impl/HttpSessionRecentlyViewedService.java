package com.es.phoneshop.service.impl;

import com.es.phoneshop.model.Product;
import com.es.phoneshop.service.RecentlyViewedService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

public class HttpSessionRecentlyViewedService implements RecentlyViewedService {
    private static final String SESSION_RECENTLY_VIEWED_KEY = "recentlyViewedKey";

    private HttpSessionRecentlyViewedService() {
    }

    public static HttpSessionRecentlyViewedService getInstance() {
        return RecentlyViewedServiceHolder.instance;
    }

    private static class RecentlyViewedServiceHolder {
        private static HttpSessionRecentlyViewedService instance = new HttpSessionRecentlyViewedService();
    }

    @Override
    public ArrayList<Product> getProducts(HttpServletRequest request) {
        HttpSession session = request.getSession();
        ArrayList<Product> recentlyViewed = (ArrayList<Product>) session.getAttribute(SESSION_RECENTLY_VIEWED_KEY);

        if (recentlyViewed == null) {
            recentlyViewed = new ArrayList<>();
            session.setAttribute(SESSION_RECENTLY_VIEWED_KEY, recentlyViewed);
        }

        return recentlyViewed;
    }

    @Override
    public void add(HttpServletRequest request, Product product) {
        ArrayList<Product> recentlyViewed = getProducts(request);

        if (recentlyViewed.contains(product)) {
            recentlyViewed.remove(product);
        } else if (recentlyViewed.size() == 3) {
            recentlyViewed.remove(recentlyViewed.size() - 1);
        }

        recentlyViewed.add(0, product);
    }
}
