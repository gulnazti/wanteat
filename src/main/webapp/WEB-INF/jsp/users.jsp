<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <base href="${pageContext.request.contextPath}/"/>
    <title>WantEat!</title>
</head>
<body>
<a href="${pageContext.request.contextPath}">Home</a>
<table>
    <thead>
        <tr>
            <th>Name</th>
        </tr>
    </thead>
    <c:forEach items="${users}" var="user">
    <jsp:useBean id="user" type="org.gulnaz.wanteat.model.User"/>
    <tr>
        <td>${user.name}</td>
    </tr>
    </c:forEach>
</table>
</body>
</html>
