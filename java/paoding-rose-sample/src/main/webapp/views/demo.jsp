<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="inc/header.jsp" %>
<title>广告统计</title>
</head>
<body>
<%@ include file="inc/navbar.jsp" %>

<div class="container">
    <c:if test="${null!=errorInfo}">
    <div class="alert alert-error">
            ${errorInfo}
    </div>
    </c:if>


    <table class="table">
        <thead>
        <tr>
            <th>#</th>
            <th>id</th>
            <th>ip</th>
            <th>keyword</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="item" items="${demos}" varStatus="status">
            <tr>
                <td>${status.count}</td>
                <td>${item.id}</td>
                <td>${item.ip}</td>
                <td>${item.keyword}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <%@ include file="inc/footer.jsp" %>
</body>
</html>