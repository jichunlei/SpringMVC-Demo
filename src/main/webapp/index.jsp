<%@ page import="java.util.Date" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<html>
<body>
<h2>Hello World!</h2>
<a href="book/1">查询图书</a>
<form action="book" method="post">
    书名：<input type="text" name="BookName"/><br/>
    作者：<input type="text" name="author.name"/><br/>
    价格：<input type="text" name="price"/><br/>
    <button type="submit">新增图书</button>
</form>
<form action="book/2" method="post">
    <input type="hidden" name="_method" value="put"/>
    <button type="submit">更新图书</button>
</form>
<form action="book/3" method="post">
    <input type="hidden" name="_method" value="delete"/>
    <button type="submit">删除图书</button>
</form>

<a href="book/param?bid=123&username1=root">获取请求参数</a><br/>

<a href="hello3">返回参数到界面</a><br/>
<a href="hello4">返回mv到界面</a><br/>
<a href="hello4">从session中获取参数</a><br/>

<form action="book/update" method="post">
    <input type="hidden" name="_method" value="put"/>
    价格：<input type="text" name="price"/>
    <button type="submit">更新图书</button>
</form>
<hr/>
<a href="hello5">hello5转发</a><br/>
<a href="hello6">hello6转发</a><br/>
<a href="hello7">hello7重定向</a><br/>
<a href="hello8">hello8重定向</a><br/>

<a href="login">登陆</a><br/>
<a href="tologin">登陆2</a><br/>

<a href="hello9">自定义视图解析器</a><br/>

<form action="book/quickadd" method="post">
    书名：<input type="text" name="bookInfo"/><br/>
    <input type="submit" value="快速新增图书">
</form>

<form action="hello10" method="post">
    姓名：<input type="text" name="name"/><form:errors path="name"/><br/>
    生日：<input type="text" name="birth"/><form:errors path="birth"/><br/>
    薪水：<input type="text" name="salary"/><form:errors path="salary"/><br/>
    邮箱：<input type="text" name="email"/><form:errors path="email"/><br/>
    <input type="submit" value="数据格式化">
</form>

<a href="add_page">添加员工</a><br/>

<form action="getHttpEntity" method="post" enctype="multipart/form-data">
    姓名：<input type="text" name="name"/><br/>
    生日：<input type="text" name="birth"/><br/>
    薪水：<input type="text" name="salary"/><br/>
    邮箱：<input type="text" name="email"/><br/>
    简历：<input type="file" name="file"/><br/>
    <input type="submit" value="提交"/>
</form>

<form action="file/upload" method="post" enctype="multipart/form-data">
    姓名：<input type="text" name="username"/><br/>
    简历：<input type="file" name="file"/><br/>
    <input type="submit" value="单文件提交"/>
</form>

<form action="file/uploads" method="post" enctype="multipart/form-data">
    姓名：<input type="text" name="username"/><br/>
    简历1：<input type="file" name="file"/><br/>
    简历2：<input type="file" name="file"/><br/>
    简历3：<input type="file" name="file"/><br/>
    <input type="submit" value="多文件提交"/>
</form>
</body>
</html>
