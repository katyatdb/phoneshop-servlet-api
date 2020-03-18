<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Advanced Search">
    <h2>Advanced search</h2>
    <form method="get" action="${pageContext.servletContext.contextPath}/advanced-search">
        <label>
            Description:
            <input type="text" name="description"
                   value="${not empty param.description ? param.description : ""}">
        </label>
        <label>
            <select name="wordCount">
                <option name="allWords">All words</option>
                <option name="anyWord">Any word</option>
            </select>
        </label>
        <br><br>
        <label>
            Min price:
            <input type="text" name="minPrice"
                   value="${not empty param.minPrice ? param.minPrice : ""}">
        </label>
        <br><br>
        <label>
            Max price:
            <input type="text" name="maxPrice"
                   value="${not empty param.maxPrice ? param.maxPrice : ""}">
        </label>
        <br><br>

        <div class="error">${requestScope.error}</div>

        <input type="submit" value="Search">
    </form>

    <c:if test="${not empty products and empty requestScope.error}">
        <table>
            <thead>
            <tr>
                <td>Image</td>
                <td>
                    Description
                </td>
                <td class="price">
                    Price
                </td>
            </tr>
            </thead>
            <c:forEach var="product" items="${products}">
                <tr>
                    <td>
                        <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
                            <img class="product-tile"
                                 src="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${product.imageUrl}">
                        </a>
                    </td>

                    <td>
                            ${product.description}
                    </td>

                    <td class="price">
                        <fmt:formatNumber value="${product.price}" type="currency"
                                          currencySymbol="${product.currency.symbol}"/>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:if>
</tags:master>
