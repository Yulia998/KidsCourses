<%--
  Created by IntelliJ IDEA.
  User: New User
  Date: 03.05.2020
  Time: 20:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <!-- Optional theme -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css"
          integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">
    <title>Авторизация</title>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="col-md-6 col-md-offset-3">
            <div class="panel panel-default" style="margin-top:45px">
                <div class="panel-heading">
                    <h3 class="panel-title">Вход с помощью логина и пароля</h3>
                </div>
                <div class="panel-body">
                    <c:if test="${logout}">
                    <div class="alert alert-info" role="alert">Вы успешно вышли.</div>
                    </c:if>
                <c:if test="${error}">
                <div class="alert alert-danger" role="alert">Неверный логин или пароль!</div>
                </c:if>

            <form method="post">
                <div class="form-group">
                    <label for="username">Логин</label>
                    <input type="text" class="form-control" id="username" placeholder="Логин"
                           name="username">
                </div>
                <div class="form-group">
                    <label for="password">Пароль</label>
                    <input type="password" class="form-control" id="password" placeholder="Пароль"
                           name="password">
                </div>
                <button type="submit" class="btn btn-default">Войти</button>
            </form>
        </div>
    </div>
</div>
</div>
</div>
</body>
</html>
