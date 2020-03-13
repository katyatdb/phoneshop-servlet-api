<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cart" class="com.es.phoneshop.model.Cart" scope="request"/>

<tags:master pageTitle="Cart">
    <c:if test="${empty cart.cartItems}">
        <h2>Cart is empty</h2>
    </c:if>

    <c:if test="${not empty requestScope.errors}">
        <div class="error">Cart update failed</div>
    </c:if>

    <div class="message">${param.message}</div>

    <c:if test="${not empty cart.cartItems}">
        <form method="post" action="${pageContext.servletContext.contextPath}/cart">
            <table class="cart">
                <thead>
                <tr>
                    <td>Image</td>
                    <td>Description</td>
                    <td>Quantity</td>
                    <td>Price</td>
                    <td>Action</td>
                </tr>
                </thead>

                <c:forEach var="cartItem" items="${cart.cartItems}" varStatus="status">
                    <c:set var="product" value="${cartItem.product}"/>
                    <tr>
                        <td>
                            <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
                                <img class="product-tile" src="${product.imageUrl}"/>
                            </a>
                        </td>
                        <td>${product.description}</td>
                        <td>
                            <input class="quantity" type="text" name="quantity"
                                   value="${not empty paramValues.quantity[status.index] ?
                               paramValues.quantity[status.index] : cartItem.quantity}">

                            <c:if test="${not empty requestScope.errors[product.id]}">
                                <p class="error">${requestScope.errors[product.id]}</p>
                            </c:if>
                            <input type="hidden" name="productId" value="${product.id}">
                        </td>
                        <td>
                            <fmt:formatNumber value="${product.price}" type="currency"
                                              currencySymbol="${product.currency.symbol}"/>
                        </td>
                        <td>
                            <button formaction="${pageContext.servletContext.contextPath}/cart/deleteCartItem/${product.id}">
                                Delete
                            </button>
                        </td>
                    </tr>
                </c:forEach>
            </table>

            <div class="total-price">
                Total price:
                <fmt:formatNumber type="currency" value="${cart.totalPrice}"
                                  currencySymbol="${cart.cartItems.get(0).product.currency.symbol}"/>
            </div>

            <input class="update" type="submit" value="Update">
        </form>

        <form method="get" action="${pageContext.servletContext.contextPath}/checkout">
            <input type="submit" value="Checkout">
        </form>
    </c:if>

    <a href="${pageContext.request.contextPath}">Back</a>
</tags:master>