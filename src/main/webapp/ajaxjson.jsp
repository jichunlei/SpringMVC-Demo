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
</head>
<body>
<a href="getResponseBody">发送json到服务器</a>
<script type="text/javascript">
    $("a:first").click(function () {
        //js对象
        var obj={
            name:"仙子磊",
            salary:"100.00"
            <%--birth:"${date}"--%>
        };
        alert(typeof obj);
        var str=JSON.stringify(obj);
        alert(typeof str);
        $.ajax({
            url:"getResponseBody",
            type:"POST",
            contentType:"application/json",
            data:str,
            success:function (data) {
                alert(data);
            }
        });
        return false;
    });
</script>
</body>
</html>
