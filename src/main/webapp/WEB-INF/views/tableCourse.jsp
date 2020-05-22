<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: New User
  Date: 21.05.2020
  Time: 11:50
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
    <a href="<c:url value='/getCourseForm/add' />" class="btn">Добавить</a>
</div>
<table class="adminTable">
    <tr>
        <th>Название</th>
        <th>Описание</th>
        <th>Кол-во занятий</th>
        <th>Цена 1 занятия</th>
        <th>Действия</th>
    </tr>
    <tr>
        <c:forEach var="course" items="${courses}">
    <tr>
        <td>${course.getName()}</td>
        <td>${course.getDescription()}</td>
        <td>${course.getDuration()}</td>
        <td>${course.getPrice()}</td>
        <td>
            <a href="<c:url value='/getCourseForm/edit?id=${course.getId()}' />">Редактировать</a><br>
            <a href="<c:url value='/deleteCourse/${course.getId()}' />">Удалить</a>
        </td>
    </tr>
    </c:forEach>
    </tr>
</table>
</body>
</html>
