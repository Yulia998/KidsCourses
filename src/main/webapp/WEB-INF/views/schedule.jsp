<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: New User
  Date: 06.05.2020
  Time: 19:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>

</head>
<body>
<%
    List<HashMap<String, String>> schedule = (List<HashMap<String, String>>) request.getAttribute("schedule");
    int elemNumber = 0;
    while (elemNumber < schedule.size()) {
%>
<div id="header-text"><%= schedule.get(elemNumber).get("dayOfWeek")%>,
    <%= schedule.get(elemNumber).get("dayDate")%>
</div>
<hr>
<div id="main">
    <% do { %>
    <div id="main-box">
        <div id="main-header">
            <%=schedule.get(elemNumber).get("lessonDate")%>
        </div>
        <div id="main-text">
            <a href="/KidsCourses/infoGroup?groupId=<%=schedule.get(elemNumber).get("groupId")%>"><%=schedule.get(elemNumber).get("groupName")%>
            </a> <br>
            Статус: <%=schedule.get(elemNumber).get("statusName")%>
        </div>
    </div>
    <%
            elemNumber++;
        } while (elemNumber < schedule.size() &&
                schedule.get(elemNumber).get("dayDate").equals(schedule.get(elemNumber - 1).get("dayDate"))); %>
</div>
<% } %>
</body>
</html>
