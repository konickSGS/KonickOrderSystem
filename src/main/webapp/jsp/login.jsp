<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<c:set var="title" value="LogIn" scope="page"/>
<%@include file="head.jspf" %>
<body>

<c:set var="currentPage" value="account" scope="page"/>
<%@include file="header.jspf" %>

<div>
    <form class="ls_form" action="${pageContext.request.contextPath}/account/login" method="post">
        <div class="form-group">
            <label for="InputLogin">Логин</label>
            <input id="InputLogin" name="login" placeholder="Login">
        </div>
        <div class="form-group">
            <label for="InputPassword">Password</label>
            <input id="InputPassword" type="password" name="password" placeholder="Password">
        </div>
        <input type="submit" value="Войти">
    </form>
    <div>
        <a href="${pageContext.request.contextPath}/account/registration">Регистрация</a>
    </div>
</div>

</body>
</html>