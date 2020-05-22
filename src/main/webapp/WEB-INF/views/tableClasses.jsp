<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: New User
  Date: 12.05.2020
  Time: 19:55
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
        <c:forEach var="entry" items="${hashList.get(0).entrySet()}">
            <c:if test="${entry.getKey() ne 'id' && entry.getKey() ne 'courseId'}">
                <th>${entry.getKey()}</th>
            </c:if>
        </c:forEach>
            <th>Действия</th>
        </tr>
        <tr>
            <c:forEach var="hash" items="${hashList}">
                <tr>
            <c:forEach var="entry" items="${hash.entrySet()}">
                <c:if test="${entry.getKey() ne 'id' && entry.getKey() ne 'courseId'}">
                    <td>
                        <c:choose>
                            <c:when test="${entry.getKey() eq 'Название группы'}">
                                <a href="<c:url value='//infoGroup?groupId=${hash.get("id")}' />">${entry.getValue()}</a>
                            </c:when>
                            <c:otherwise>
                            ${entry.getValue()}
                            </c:otherwise>
                        </c:choose>
                    </td>
                </c:if>
            </c:forEach>
                <td>
                    <a href="<c:url value='/${urlTable}/edit?id=${hash.get("id")}' />">Редактировать</a><br>
                    <a href="<c:url value='/${delete}/${hash.get("id")}' />">Удалить</a>
                </td>
                </tr>
            </c:forEach>
        </tr>
    </table>

</body>
</html>
