<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: New User
  Date: 07.05.2020
  Time: 21:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Group Information</title>
    <style><%@include file="css/style.css"%></style>
</head>
<body>
<header>
    <div class="username">${name}</div>
    <div class="logout">
        <a href="<c:url value='/logout' />">Выйти</a>
    </div>
</header>
    <h2>${group}</h2>
    <div class="courseInfo">
        <div align="center"><b>${course.get("name")}</b></div>
        <div>
            <p><i>Описание:</i> ${course.get("description")}</p>
            <p><i>Количество занятий:</i> ${course.get("duration")}</p>
        </div>
    </div>
    <div class="infoTable">
        <div><b>Список детей</b></div>
    <table>
        <tr>
            <th>ФИО</th>
            <th>Возраст</th>
        </tr>
        <c:forEach var="child" items="${children}">
            <tr>
                <td>${child.get("name")} </a></td>
                <td>${child.get("age")}</td>
            </tr>
        </c:forEach>
    </table>
    </div>
</body>
</html>
