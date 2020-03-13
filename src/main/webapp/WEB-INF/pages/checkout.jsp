<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cart" class="com.es.phoneshop.model.Cart" scope="request"/>
<tags:master pageTitle="Checkout">
    <c:if test="${empty cart.cartItems}">
        <h2>Cart is empty</h2>
    </c:if>

    <c:if test="${not empty requestScope.error}">
        <div class="error">Checkout failed</div>
    </c:if>

    <c:if test="${not empty cart.cartItems}">
        <h2>Checkout</h2>

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

            <tr>
                <td class="price" colspan="3">Subtotal:</td>
                <td>
                    <fmt:formatNumber type="currency" value="${cart.totalPrice}"
                                      currencySymbol="${cart.cartItems.get(0).product.currency.symbol}"/>
                </td>
            </tr>

            <tr>
                <td class="price" colspan="3">Delivery cost:</td>
                <td>
                    <fmt:formatNumber type="currency" value="${requestScope.deliveryCost}"
                                      currencySymbol="${cart.cartItems.get(0).product.currency.symbol}"/>
                </td>
            </tr>

            <tr>
                <td class="price" colspan="3">Total:</td>
                <td>
                    <fmt:formatNumber type="currency" value="${cart.totalPrice + requestScope.deliveryCost}"
                                      currencySymbol="${cart.cartItems.get(0).product.currency.symbol}"/>
                </td>
            </tr>
        </table>

        <form method="post" action="${pageContext.servletContext.contextPath}/checkout">
            <label>
                First name:
                <input type="text" name="firstName"
                       value="${not empty param.firstName ? param.firstName : ""}">
            </label>
            <br><br>
            <label>
                Last name:
                <input type="text" name="lastName"
                       value="${not empty param.lastName ? param.lastName : ""}">
            </label>
            <br><br>
            <label>
                Phone number:
                <input type="text" name="phoneNumber"
                       value="${not empty param.phoneNumber ? param.phoneNumber : ""}">
            </label>
            <br><br>
            <label>
                Delivery date:
                <input type="text" name="deliveryDate" placeholder="dd.mm.yyyy"
                       value="${not empty param.deliveryDate ? param.deliveryDate : ""}">
            </label>
            <br><br>
            <label>
                Delivery address:
                <input type="text" name="deliveryAddress"
                       value="${not empty param.deliveryAddress ? param.deliveryAddress : ""}">
            </label>
            <br><br>
            <label>
                Payment method:
                <select name="paymentMethod">
                    <option name="cash">Cash</option>
                    <option name="creditCard">Credit card</option>
                </select>
            </label>
            <br><br>

            <c:if test="${not empty requestScope.error}">
                <div class="error">${requestScope.error}</div>
            </c:if>

            <input type="submit" value="Place order">
        </form>
    </c:if>
</tags:master>