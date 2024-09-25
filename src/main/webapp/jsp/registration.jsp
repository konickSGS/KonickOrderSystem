<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<c:set var="title" value="Registration" scope="page"/>
<%@include file="head.jspf" %>
<body>

<c:set var="currentPage" value="account" scope="page"/>
<%@include file="header.jspf" %>
<body>
<div class="row">
    <div>
        <form action="${pageContext.request.contextPath}/account/login" method="post" class="form-container">
            <div class="form-group">
                <label for="inputLogin">Логин</label>
                <input name="login" id="inputLogin" placeholder="Login" required>
            </div>

            <div class="form-group">
                <label for="inputEmail">Email</label>
                <input type="email" name="email" id="inputEmail" placeholder="Email"
                       required>
            </div>
            <div class="form-group">
                <label for="inputAddress">Адрес</label>
                <input name="address" id="inputAddress" placeholder="Address" required>
            </div>
            <div class="form-group">
                <label for="inputPassword">Пароль</label>
                <input type="password" required title="не менее 6 символов" name="password"
                       id="inputPassword" type="password" placeholder="Password" required>
            </div>

            <div class="form-group">
                <label for="confirmInputPassword">Подтвердить пароль</label>
                <input name="confirmPassword" id="confirmInputPassword" type="password"
                       placeholder="Password" required>
            </div>

            <button type="submit">Register</button>

        </form>
    </div>
</div>
</body>
</html>