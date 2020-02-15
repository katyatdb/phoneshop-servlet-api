<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" class="com.es.phoneshop.model.Product" scope="request"/>
<tags:master pageTitle="Price History">
    <h2>${product.description}</h2>
    <table>
        <thead>
        <tr>
            <td>Date</td>
            <td>Price</td>
        </tr>
        </thead>
        <c:forEach var="entry" items="${product.priceHistory}">
            <tr>
                <td>${entry.key}</td>
                <td>${entry.value}</td>
            </tr>
        </c:forEach>
    </table>

    <a href="${pageContext.request.contextPath}">Back</a>
</tags:master>