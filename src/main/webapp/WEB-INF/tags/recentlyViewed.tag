<%@ tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:useBean id="recentlyViewed" scope="request" type="java.util.List"/>
<c:if test="${not empty recentlyViewed}">
    <h2>Recently viewed</h2>
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
</c:if>