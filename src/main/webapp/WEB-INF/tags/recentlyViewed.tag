<%@ tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<table class="recently-viewed">
    <tr>
        <c:forEach var="product" items="${recentlyViewed}">
            <td>
                <img src="${product.imageUrl}">
                <div>${product.description}</div>
                <div>
                    <fmt:formatNumber value="${product.price}" type="currency"
                                      currencySymbol="${product.currency.symbol}"/>
                </div>
            </td>
        </c:forEach>
    </tr>
</table>