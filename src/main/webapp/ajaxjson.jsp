<%@ page import="java.util.Date" %><%--
  Created by IntelliJ IDEA.
  User: xianzilei
  Date: 2019/8/10
  Time: 13:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>发送json到服务器</title>
    <script type="text/javascript" src="statics/js/jquery-1.9.1.min.js"></script>
</head>
<body>
<a href="getResponseBody">发送json到服务器</a>
<script type="text/javascript">
    $("a:first").click(function () {
        //js对象
        var obj={
            username:"aa",
            salary:'1000.00'
        };
        alert(typeof obj);
        var objStr=JSON.stringify(obj);
        alert(typeof objStr);
        $.ajax({
            url:'getResponseBody',
            type:'POST',
            contentType:'application/json',
            data:objStr,
            success:function (data) {
                alert(data);
            }

        });
        return false;
    });
</script>
</body>
</html>
