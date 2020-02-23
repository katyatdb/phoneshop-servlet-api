package com.es.phoneshop.web;

import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.service.RecentlyViewedService;
import com.es.phoneshop.service.impl.HttpSessionRecentlyViewedService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProductListPageServlet extends HttpServlet {

    private ProductDao productDao;
    private RecentlyViewedService recentlyViewedService;

    @Override
    public void init() {
        productDao = ArrayListProductDao.getInstance();
        recentlyViewedService = HttpSessionRecentlyViewedService.getInstance();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        String sortBy = request.getParameter("sortBy");
        String orderBy = request.getParameter("orderBy");

        request.setAttribute("recentlyViewed", recentlyViewedService.getProducts(request));
        request.setAttribute("products", productDao.findProducts(query, sortBy, orderBy));
        request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);
    }
}
