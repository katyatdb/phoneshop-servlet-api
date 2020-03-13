package com.es.phoneshop.web;

import com.es.phoneshop.model.Order;
import com.es.phoneshop.service.OrderService;
import com.es.phoneshop.service.impl.OrderServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OrderOverviewPageServlet extends HttpServlet {

    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        orderService = OrderServiceImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String secureId = getSecureId(req);
        Order order = orderService.getBySecureId(secureId);

        req.setAttribute("order", order);
        req.getRequestDispatcher("/WEB-INF/pages/orderOverview.jsp").forward(req, resp);
    }

    private String getSecureId(HttpServletRequest request) {
        String path = request.getPathInfo();
        return path.split("/")[1];
    }
}
