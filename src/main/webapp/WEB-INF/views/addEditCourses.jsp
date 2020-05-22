<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: New User
  Date: 14.05.2020
  Time: 21:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Добавление/Редактирование курса</title>
    <style><%@include file="css/style.css"%><%@include file="css/adminStyle.css"%></style>
</head>
<body>
<header>
    <div class="username">${name}</div>
    <div class="logout">
        <a href="<c:url value='/logout' />">Выйти</a>
    </div>
</header>
<div class="container" style="width: 500px">
    <c:if test="${courseEdit.getId() != null}">
        <c:set var="emplId" value="?id=${courseEdit.getId()}"/>
    </c:if>
    <form action="<c:url value='/addEditCourses/${action}${emplId}' />" method="POST">
        <table>
            <tr>
                <td><label>Название</label></td>
                <td><input type="text" name="name" required value="${courseEdit.getName()}"></td>
            </tr>
            <tr>
                <td><label>Описание</label></td>
                <td><textarea name="description" rows="8" cols="40" required>${courseEdit.getDescription()}</textarea></td>
            </tr>
            <tr>
                <td><label>Количество занятий</label></td>
                <td><input type="number" name="lessons" value="${courseEdit.getDuration()}"></td>
            </tr>
            <tr>
                <td><label>Цена</label></td>
                <td><input type="number" name="price" required value="${courseEdit.getPrice()}"></td>
            </tr>
            <tr>
                <td><input type="submit" class="btn" value="${actBtn}"></td>
            </tr>
        </table>
    </form>
</div>
</body>
</html>
