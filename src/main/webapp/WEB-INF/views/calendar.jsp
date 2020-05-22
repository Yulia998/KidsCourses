<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: New User
  Date: 13.05.2020
  Time: 22:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
    <style><%@include file="css/style.css"%><%@include file="css/adminStyle.css"%></style>
    <script>
        <%@include file="js/script.js"%>
    </script>
</head>
<body>
<form class="userForm" name="date">
    <div class="text" style="border-bottom: 1px solid cadetblue">
        <label>Выберите дату</label>
    </div>
    <div class="text">
        <label>C</label>
        <input type="date" name="startDate" value="${startdate}" required>
        <label>По</label>
        <input type="date" name="endDate" value="${enddate}" required>
    </div>
    <c:if test="${!user}">
        <div class="text" style="border-bottom: 1px solid cadetblue">
            <label>Выберите преподователя</label>
        </div>
        <div class="text">
            <select id="employee" required>
                <option selected> </option>
                <c:forEach var="employee" items="${employees}">
                    <option value="${employee.getId()}">${employee.getFullName()}</option>
                </c:forEach>
            </select>
        </div>
    </c:if>
    <input type="button" class="btn" value="Показать" onclick="searchInfo(${user})">
</form>
<div id="schedule">
    <c:if test="${user}">
        <jsp:include page="schedule.jsp"/>
    </c:if>
</div>
</body>
</html>
