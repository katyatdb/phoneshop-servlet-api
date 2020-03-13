package com.es.phoneshop.web;

import com.es.phoneshop.enums.PaymentMethod;
import com.es.phoneshop.model.Cart;
import com.es.phoneshop.model.Order;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.OrderService;
import com.es.phoneshop.service.impl.HttpSessionCartService;
import com.es.phoneshop.service.impl.OrderServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

public class CheckoutPageServlet extends HttpServlet {

    private OrderService orderService;
    private CartService cartService;

    @Override
    public void init() throws ServletException {
        orderService = OrderServiceImpl.getInstance();
        cartService = HttpSessionCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cart cart = cartService.getCart(req);

        req.setAttribute("cart", cart);
        req.setAttribute("deliveryCost", orderService.getDeliveryCost());
        req.getRequestDispatcher("/WEB-INF/pages/checkout.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String> params = getParams(req);

        boolean isCorrect = params.values().stream()
                .allMatch(this::checkParameter);

        if (!isCorrect) {
            req.setAttribute("error", "All fields must be filled");
            doGet(req, resp);
            return;
        }

        Cart cart = cartService.getCart(req);
        Order order = orderService.placeOrder(cart);

        try {
            setOrderFields(order, params);
        } catch (DateTimeParseException e) {
            req.setAttribute("error", "Invalid date format");
            doGet(req, resp);
            return;
        }

        cart.getCartItems().clear();
        cart.setTotalPrice(BigDecimal.ZERO);
        req.setAttribute("order", order);
        resp.sendRedirect(req.getContextPath() + "/order/overview/" + order.getSecureId());
    }

    private LocalDate parseDate(String date) throws DateTimeParseException {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd.MM.uuuu");
        return LocalDate.parse(date, pattern);
    }

    private boolean checkParameter(String param) {
        return param != null && !param.trim().isEmpty();
    }

    private Map<String, String> getParams(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        map.put("firstName", request.getParameter("firstName"));
        map.put("lastName", request.getParameter("lastName"));
        map.put("phoneNumber", request.getParameter("phoneNumber"));
        map.put("deliveryDate", request.getParameter("deliveryDate"));
        map.put("deliveryAddress", request.getParameter("deliveryAddress"));
        map.put("paymentMethodStr", request.getParameter("paymentMethod"));

        String paymentMethod = request.getParameter("paymentMethod");
        paymentMethod = paymentMethod.replace(" ", "_").toUpperCase();
        map.put("paymentMethod", paymentMethod);

        return map;
    }

    private void setOrderFields(Order order, Map<String, String> params) throws DateTimeParseException {
        order.setFirstName(params.get("firstName"));
        order.setLastName(params.get("lastName"));
        order.setPhoneNumber(params.get("phoneNumber"));
        order.setDeliveryAddress(params.get("deliveryAddress"));

        LocalDate deliveryDate = parseDate(params.get("deliveryDate"));
        order.setDeliveryDate(deliveryDate);

        PaymentMethod paymentMethod = PaymentMethod.valueOf(params.get("paymentMethod"));
        order.setPaymentMethod(paymentMethod);
    }
}
