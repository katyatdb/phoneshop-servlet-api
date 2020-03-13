package com.es.phoneshop.web;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.Product;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ProductPriceHistoryServlet extends HttpServlet {

    private ProductDao productDao;

    @Override
    public void init() {
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long productId = getProductId(req);
        Product product = productDao.get(productId);

        Map<LocalDate, BigDecimal> sortedPriceHistory = product
                .getPriceHistory().entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v2, LinkedHashMap::new));

        product.setPriceHistory(sortedPriceHistory);

        req.setAttribute("product", product);
        req.getRequestDispatcher("/WEB-INF/pages/productPriceHistory.jsp").forward(req, resp);
    }

    private long getProductId(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        return Long.parseLong(pathInfo.split("/")[1]);
    }
}
