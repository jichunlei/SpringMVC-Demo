<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<html>
<body>
<h2>Hello World!</h2>
<a href="/mvc/book/1">查询图书</a>
<form action="/mvc/book" method="post">
    <button type="submit">新增图书</button>
</form>
<form action="/mvc/book/2" method="post">
    <input type="hidden" name="_method" value="put"/>
    <button type="submit">更新图书</button>
</form>
<form action="/mvc/book/3" method="post">
    <input type="hidden" name="_method" value="delete"/>
    <button type="submit">删除图书</button>
</form>

<a href="/mvc/book/param?bid=123&username1=root">获取请求体参数</a>
</body>
</html>
