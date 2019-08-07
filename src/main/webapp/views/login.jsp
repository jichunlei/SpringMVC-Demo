<%--
  Created by IntelliJ IDEA.
  User: xianzilei
  Date: 2019/8/7
  Time: 8:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>登陆页面</title>
</head>
<body>
    <h2><fmt:message key="login.welcome"/></h2>
    <form action="/mvc/login">
        <fmt:message key="login.username"/>:<input type="text"/><br/>
        <fmt:message key="login.password"/>:<input type="text"/><br/>
        <input type="submit" value='<fmt:message key="login.login"/>'/><br/>
    </form>
</body>
</html>
