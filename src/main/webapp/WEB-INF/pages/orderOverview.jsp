<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" class="com.es.phoneshop.model.Order" scope="request"/>
<tags:master pageTitle="Order Overview">
    <h2>Thank you for order!</h2>

    <table class="cart">
        <thead>
        <tr>
            <td>Image</td>
            <td>Description</td>
            <td>Quantity</td>
            <td>Price</td>
        </tr>
        </thead>
        <c:forEach var="cartItem" items="${order.cartItems}">
            <c:set var="product" value="${cartItem.product}"/>
            <tr>
                <td>
                    <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
                        <img class="product-tile" src="${product.imageUrl}"/>
                    </a>
                </td>
                <td>${product.description}</td>
                <td>${cartItem.quantity}</td>
                <td>
                    <fmt:formatNumber type="currency" value="${product.price}"
                                      currencySymbol="${product.currency.symbol}"/>
                </td>
            </tr>
        </c:forEach>

        <tr>
            <td class="price" colspan="3">Subtotal:</td>
            <td>
                <fmt:formatNumber type="currency" value="${order.subtotalPrice}"
                                  currencySymbol="${order.cartItems.get(0).product.currency.symbol}"/>
            </td>
        </tr>

        <tr>
            <td class="price" colspan="3">Delivery cost:</td>
            <td>
                <fmt:formatNumber type="currency" value="${order.deliveryCost}"
                                  currencySymbol="${order.cartItems.get(0).product.currency.symbol}"/>
            </td>
        </tr>

        <tr>
            <td class="price" colspan="3">Total:</td>
            <td>
                <fmt:formatNumber type="currency" value="${order.totalPrice}"
                                  currencySymbol="${order.cartItems.get(0).product.currency.symbol}"/>
            </td>
        </tr>
    </table>

    <p>First name: ${order.firstName}</p>
    <p>Last name: ${order.lastName}</p>
    <p>Phone number: ${order.phoneNumber}</p>
    <p>Delivery date: ${order.deliveryDate}</p>
    <p>Delivery address: ${order.deliveryAddress}</p>
    <p>Payment method: ${order.paymentMethod.toString().toLowerCase().replace("_", " ")}</p>

    <a href="${pageContext.servletContext.contextPath}">Back</a>
</tags:master>