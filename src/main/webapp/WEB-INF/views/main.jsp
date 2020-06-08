<%--
  Created by IntelliJ IDEA.
  User: New User
  Date: 12.05.2020
  Time: 15:52
  To change this template use File | Settings | File Templates.
  onload="loadPage(${url})"
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Main</title>
    <style><%@include file="css/style.css"%><%@include file="css/adminStyle.css"%></style>
    <script>
        <%@include file="js/script.js"%>
    </script>
</head >
<body onload="loadPage('${url}')">
    <header>
        <div class="username">${name}</div>
        <div class="logout">
            <a href="<c:url value='/logout' />">Выйти</a>
        </div>
    </header>
    <c:if test="${!user}">
    <ul class="menu-main">
        <li><a href="javascript:loadPage('calendar')">Календарь</a></li>
        <li><a href="javascript:loadPage('employees')">Работники</a></li>
        <li><a href="javascript:loadPage('courses')">Курсы</a></li>
        <li><a href="javascript:loadPage('children')">Дети</a></li>
        <li><a href="javascript:loadPage('classes')">Группы</a></li>
    </ul>
    </c:if>
    <div id="data">
    </div>
</body>
</html>
