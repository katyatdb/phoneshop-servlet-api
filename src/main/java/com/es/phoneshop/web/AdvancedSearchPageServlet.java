package com.es.phoneshop.web;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.enums.WordCount;
import com.es.phoneshop.model.Product;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

public class AdvancedSearchPageServlet extends HttpServlet {

    private ProductDao productDao;

    @Override
    public void init() {
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String description = req.getParameter("description");
        String wordCountStr = req.getParameter("wordCount");
        String minPriceStr = req.getParameter("minPrice");
        String maxPriceStr = req.getParameter("maxPrice");

        BigDecimal minPrice = null;
        BigDecimal maxPrice = null;

        try {
            if (minPriceStr != null && !minPriceStr.trim().isEmpty()) {
                minPrice = new BigDecimal(Integer.parseInt(minPriceStr));
            }
            if (maxPriceStr != null && !maxPriceStr.trim().isEmpty()) {
                maxPrice = new BigDecimal(Integer.parseInt(maxPriceStr));
            }
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Price must be a number");
        }

        req.setAttribute("products", productDao.findProducts(description, wordCountStr, minPrice, maxPrice));
        req.getRequestDispatcher("/WEB-INF/pages/advancedSearch.jsp").forward(req, resp);
    }
}
