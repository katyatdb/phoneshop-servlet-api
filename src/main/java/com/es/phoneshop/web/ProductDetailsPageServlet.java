package com.es.phoneshop.web;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.Product;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.RecentlyViewedService;
import com.es.phoneshop.service.impl.HttpSessionCartService;
import com.es.phoneshop.service.impl.HttpSessionRecentlyViewedService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.NoSuchElementException;

public class ProductDetailsPageServlet extends HttpServlet {

    private ProductDao productDao;
    private CartService cartService;
    private RecentlyViewedService recentlyViewedService;

    @Override
    public void init() {
        productDao = ArrayListProductDao.getInstance();
        cartService = HttpSessionCartService.getInstance();
        recentlyViewedService = HttpSessionRecentlyViewedService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Long productId = getProductId(req);
            Product product = productDao.get(productId);

            recentlyViewedService.add(req, product);

            req.setAttribute("product", product);
            req.setAttribute("recentlyViewed", recentlyViewedService.getProducts(req));
            req.getRequestDispatcher("/WEB-INF/pages/productDetails.jsp").forward(req, resp);
        } catch (IllegalArgumentException | NoSuchElementException e) {
            resp.sendError(404, "Product not found");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long productId = getProductId(req);
        NumberFormat numberFormat = NumberFormat.getInstance(req.getLocale());
        int quantity;

        try {
            quantity = numberFormat.parse(req.getParameter("quantity")).intValue();
        } catch (ParseException e) {
            req.setAttribute("error", "Value must be a number");
            doGet(req, resp);
            return;
        }

        try {
            cartService.add(cartService.getCart(req), productId, quantity);
        } catch (IllegalArgumentException | OutOfStockException e) {
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
            return;
        }

        String message = "Product added to cart successfully";
        resp.sendRedirect(req.getRequestURI() + "?quantity=" + quantity + "&message=" + message);
    }

    private long getProductId(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        return Long.parseLong(pathInfo.split("/")[1]);
    }
}
