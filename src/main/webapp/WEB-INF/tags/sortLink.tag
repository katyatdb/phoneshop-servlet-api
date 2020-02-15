<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="sortBy" required="true" %>
<%@ attribute name="orderBy" required="true" %>

<a href="?query=${param.query}&sortBy=${sortBy}&orderBy=${orderBy}"
${sortBy eq param.sortBy and orderBy eq param.orderBy ? 'style="font-weight: bold"' : ''}>
    ${orderBy}
</a>