package com.es.phoneshop.web;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.Cart;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.impl.HttpSessionCartService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class CartPageServlet extends HttpServlet {

    private CartService cartService;

    @Override
    public void init() throws ServletException {
        cartService = HttpSessionCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cart cart = cartService.getCart(req);
        req.setAttribute("cart", cart);
        req.getRequestDispatcher("/WEB-INF/pages/cart.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cart cart = cartService.getCart(req);
        String[] quantities = req.getParameterValues("quantity");
        String[] productIds = req.getParameterValues("productId");
        Map<Long, String> errors = new HashMap<>();

        NumberFormat numberFormat = NumberFormat.getInstance(req.getLocale());

        for (int i = 0; i < productIds.length; i++) {
            if (quantities[i] != null) {
                long productId = Long.parseLong(productIds[i]);

                try {
                    int quantity = numberFormat.parse(quantities[i]).intValue();
                    cartService.update(cart, productId, quantity);
                } catch (ParseException e) {
                    errors.put(productId, "Value must be a number");
                } catch (IllegalArgumentException | OutOfStockException e) {
                    errors.put(productId, "Invalid value");
                }
            }
        }

        if (errors.isEmpty()) {
            resp.sendRedirect(req.getRequestURI() + "?message=Cart updated successfully");
        } else {
            req.setAttribute("errors", errors);
            doGet(req, resp);
        }
    }
}
