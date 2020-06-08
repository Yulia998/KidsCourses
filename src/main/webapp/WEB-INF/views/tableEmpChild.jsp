<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: New User
  Date: 21.05.2020
  Time: 10:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
    <style><%@include file="css/adminStyle.css"%><%@include file="css/style.css"%></style>
</head>
<body>
<div class="add">
    <a href="<c:url value='/${urlTable}/add' />" class="btn">Добавить</a>
</div>
    <table class="adminTable">
        <tr>
            <th>Имя</th>
            <th>День рождение</th>
            <th>Телефон <c:if test="${urlTable eq 'getChildForm'}">родителя</c:if></th>
            <th>Город</th>
            <c:if test="${urlTable eq 'getEmployeeForm'}">
            <th>Менеджер</th>
            <th>Логин</th>
            <th>Пароль</th>
            </c:if>
            <th>Действия</th>
        </tr>
        <tr>
            <c:forEach var="item" items="${list}">
        <tr>
            <td>${item.getFullName()}</td>
            <td>${item.getBirthday()}</td>
            <td>${item.getTelephone()}</td>
            <td>${item.getCity()}</td>
            <c:if test="${urlTable eq 'getEmployeeForm'}">
            <td>${item.getManager().getFullName()}</td>
            <td>${item.getLogin()}</td>
            <td>${item.getPassword()}</td>
            </c:if>
            <td>
                <a href="<c:url value='/${urlTable}/edit?id=${item.getId()}' />">Редактировать</a><br>
                <a href="<c:url value='/${delete}/${item.getId()}' />">Удалить</a>
            </td>
        </tr>
        </c:forEach>
        </tr>
    </table>
</body>
</html>
