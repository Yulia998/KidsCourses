<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: New User
  Date: 16.05.2020
  Time: 20:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Добавление/удаление группы</title>
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
    <c:if test="${classEdit.get('id') != null}">
        <c:set var="classId" value="?id=${classEdit.get('id')}"/>
    </c:if>
    <form action="<c:url value='/addEditClasses/${action}${classId}' />" method="POST">
        <table>
            <tr>
                <td><label>Название</label></td>
                <td><input type="text" name="name" required value="${classEdit.get('Название группы')}"></td>
            </tr>
            <tr>
                <td><label>Дата начала</label></td>
                <td><input name="startDate" type="date" required value="${classEdit.get('Дата начала')}"></td>
            </tr>
            <tr>
                <td><label>Курс</label></td>
                <td>
                    <select name="course" required>
                        <option selected value="${classEdit.get('courseId')}">${classEdit.get('Название курса')}</option>
                        <c:forEach var="course" items="${courses}">
                            <c:if test="${course.get('id') ne classEdit.get('courseId')}">
                            <option value="${course.get("id")}">${course.get("Название")}</option>
                            </c:if>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <c:forEach var="index" begin="1" end="2">
            <tr>
                <td><label>
                    <c:if test="${index==1}">Преподователь</c:if>
                    <c:if test="${index==2}">Помощник</c:if>
                </label></td>
                <td>
                    <c:if test="${index==1}">
                        <select name="pro" required>
                            <c:set var="tescherId" value="${classEdit.get('proId')}"/>
                            <option selected value="${tescherId}">${classEdit.get('Преподователь')}</option>
                        </c:if>
                     <c:if test="${index==2}"><select name="assistant">
                             <c:if test="${action eq 'edit' && classEdit.get('Помощник') ne null}">
                                 <option></option>
                             </c:if>
                             <c:set var="tescherId" value="${classEdit.get('assisId')}"/>
                        <option selected value="${tescherId}">${classEdit.get('Помощник')}</option>
                     </c:if>
                        <c:forEach var="employee" items="${employees}">
                            <c:if test="${employee.get('id') ne tescherId}">
                                <option value="${employee.get("id")}">${employee.get("Имя")}</option>
                            </c:if>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            </c:forEach>
            <tr>
                <td><label>Время занятий</label></td>
                <td>
                    <c:forEach var="day" items="${dayOfWeek}">
                    <div>
                        <input type="checkbox" name="day" value="${day}"
                            <c:if test="${dateTimeMap.containsKey(day)}">checked</c:if>
                        >
                        <label>${day} : </label>
                        <input name="time" type="time"
                               <c:if test="${dateTimeMap.containsKey(day)}">value="${dateTimeMap.get(day)}" </c:if>
                        >
                    </div>
                    </c:forEach>
                </td>
            </tr>
            <tr>
                <td><label>Дети</label></td>
                <td>
                    <% List<HashMap<String, String>> childrenGroup = (List<HashMap<String, String>>)request.getAttribute("childrenGroup");
                        List<LinkedHashMap<String, String>> children = (List<LinkedHashMap<String, String>>)request.getAttribute("children");
                    %>
                    <select name="children" multiple style="width: 200px; height: 200px">
                        <%  long presentChild = 0;
                            for(Map<String, String> child : children) {
                            if (childrenGroup != null) {
                                presentChild = childrenGroup.stream().filter(childGroup -> childGroup.get("id").equals(child.get("id")))
                                        .count();
                            }
                        %>
                        <option value="<%= child.get("id") %>"
                            <% if(presentChild != 0) { %> selected <% } %> >
                            <%= child.get("Имя") %>
                        </option>
                        <%
                            }
                        %>
                    </select>
                </td>
            </tr>
            <tr>
                <td><input type="submit" class="btn" value="${actBtn}"></td>
            </tr>
        </table>
    </form>
</div>
</body>
</html>
