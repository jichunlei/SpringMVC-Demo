<%--
  Created by IntelliJ IDEA.
  User: xianzilei
  Date: 2019/8/10
  Time: 8:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Date" %>
<html>
<% pageContext.setAttribute("ctp",request.getContextPath()); %>
<head>
    <title>ajax请求</title>
    <%--<script type="text/javascript" src="${ctp}/statics/js/jquery-3.4.1.min.js"/>--%>
    <script type="text/javascript" src="${ctp}/statics/js/jquery-1.9.1.min.js"></script>
</head>
<body>
<%= new Date()%><br/>
<a href="${ctp}/ajaxRequest">ajax请求</a>
<div>

</div>
<script type="text/javascript">
    $("a:first").click(function () {
        $.ajax({
           url:"${ctp}/ajaxRequest",
           type:"GET",
            success:function (data) {
               //将json对象转成字符串
               // var str=JSON.stringify(data);
               //  $("div").append(str+"<br/>");
                $("div").append(data.name+"<br/>");
                $("div").append(data.birth+"<br/>");
                $("div").append(data.salary+"<br/>");
            }
        });
       return false;
    });
</script>
</body>
</html>
