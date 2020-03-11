<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:useBean id="cart" class="com.es.phoneshop.model.Cart" scope="request"/>
<c:if test="${not empty cart.cartItems}">
    <h2>Cart</h2>

    <table class="cart">
        <thead>
        <tr>
            <td>Image</td>
            <td>Description</td>
            <td>Quantity</td>
            <td>Price</td>
        </tr>
        </thead>
        <c:forEach var="cartItem" items="${cart.cartItems}">
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
    </table>

    <div class="total-price">
        Total price:
        <fmt:formatNumber type="currency" value="${cart.totalPrice}"
                          currencySymbol="${cart.cartItems.get(0).product.currency.symbol}"/>
    </div>
</c:if>