<%--
  Created by IntelliJ IDEA.
  User: xianzilei
  Date: 2019/8/1
  Time: 18:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>hello</title>
</head>
<body>
<%
    System.out.println("hello.jsp");
%>
    requestScope.msg:${requestScope.get("msg")}<br/>
    requestScope.msg2:${requestScope.get("msg2")}<br/>
    requestScope.msg3:${requestScope.get("msg3")}<br/>
    requestScope.msg3:${requestScope.get("msg4")}<br/>
    <hr/>
    sessionScope.msg:${sessionScope.get("msg")}<br/>
    sessionScope.msg2:${sessionScope.get("msg2")}<br/>
    sessionScope.msg3:${sessionScope.get("msg3")}<br/>
    sessionScope.msg3:${sessionScope.get("msg4")}<br/>
</body>
</html>
