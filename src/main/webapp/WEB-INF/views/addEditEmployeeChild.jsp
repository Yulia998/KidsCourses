<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: New User
  Date: 12.05.2020
  Time: 23:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<html>
<head>
    <title>Добавление/Редактирование
        <c:if test="${isEmployee}">сотрудника</c:if>
        <c:if test="${!isEmployee}">ребенка</c:if>
    </title>
    <style><%@include file="css/style.css"%><%@include file="css/adminStyle.css"%></style>
</head>
<body>
    <header>
        <div class="username">${name}</div>
        <div class="logout">
            <a href="<c:url value='/logout' />">Выйти</a>
        </div>
    </header>
    <div class="container">
        <c:if test="${peopleEdit.getId() != null}">
        <c:set var="emplId" value="?id=${peopleEdit.getId()}"/>
        </c:if>
    <form action="<c:url value='/${urlForm}/${action}${emplId}' />" method="POST">
        <c:if test="${errorAdd}">
            <div><label style="color: red">Работник не добавлен/изменен. Логин уже существует</label></div>
        </c:if>
        <table>
            <tr>
                <td><label>Имя</label></td>
                <td><input type="text" name="name" required value="${peopleEdit.getFullName()}"></td>
            </tr>
            <tr>
                <td><label>День рождение</label></td>
                <td><input type="date" name="birthday" required value="${peopleEdit.getBirthday()}"></td>
            </tr>
            <tr>
                <td><label>Телефон</label></td>
                <td><input type="text" maxlength="13" name="tel" value="${peopleEdit.getTelephone()}"></td>
            </tr>
            <tr>
                <td><label>Город</label></td>
                <td><input type="text" name="city" required value="${peopleEdit.getCity()}"></td>
            </tr>
            <c:if test="${isEmployee}">
            <tr>
                <td><label>Начальник</label></td>
                <td>
                    <select name="manager" required>
                        <option selected value="${peopleEdit.getManager().getId()}">${peopleEdit.getManager().getFullName()}</option>
                        <c:forEach var="employee" items="${employees}">
                            <c:if test="${employee.getId() ne peopleEdit.getManager().getId() && employee.getId() ne peopleEdit.getId()}">
                            <option value="${employee.getId()}">${employee.getFullName()}</option>
                            </c:if>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr>
                <td><label>Логин</label></td>
                <td><input type="text" name="login" required value="${peopleEdit.getLogin()}"> </td>
            </tr>
            <tr>
                <td><label>Пароль</label></td>
                <td><input type="text" name="password" required value="${peopleEdit.getPassword()}"></td>
            </tr>
            </c:if>
            <tr>
                <td><input type="submit" class="btn" value="${actBtn}"></td>
            </tr>
        </table>
    </form>
    </div>
</body>
</html>
