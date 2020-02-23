<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" class="com.es.phoneshop.model.Product" scope="request"/>
<tags:master pageTitle="Product Details">
    <div>${cart}</div>
    <h2>${product.description}</h2>
    <img src="${product.imageUrl}"/>
    <div>
        Price:
        <fmt:formatNumber value="${product.price}" type="currency"
                          currencySymbol="${product.currency.symbol}"/>
    </div>
    <div>Stock: ${product.stock} </div>

    <form class="form" method="post" action="${pageContext.request.contextPath}/products/${product.id}">
        <input class="quantity" type="text" name="quantity"
               value="${not empty param.quantity ? param.quantity : 1}">
        <button type="submit" value="Add to cart">Add to cart</button>
    </form>
    <div class="message">${param.message}</div>
    <div class="error">${error}</div>

    <tags:recentlyViewed/>

    <a href="${pageContext.request.contextPath}">Back</a>
</tags:master>