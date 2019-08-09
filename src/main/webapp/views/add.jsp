<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: xianzilei
  Date: 2019/8/9
  Time: 8:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>添加员工</title>
</head>
<body>
<form:form action="add" modelAttribute="person" method="post">
    姓名：<form:input path="name"/><form:errors path="name"/><br/>
    生日：<form:input path="birth"/><form:errors path="birth"/><br/>
    薪水：<form:input path="salary"/><form:errors path="salary"/><br/>
    邮箱：<form:input path="email"/><form:errors path="email"/><br/>
    <input type="submit" value="提交">
</form:form>
</body>
</html>
