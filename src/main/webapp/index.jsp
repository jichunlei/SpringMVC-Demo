<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<html>
<body>
<h2>Hello World!</h2>
<a href="/mvc/book/1">查询图书</a>
<form action="/mvc/book" method="post">
    书名：<input type="text" name="BookName"/><br/>
    作者：<input type="text" name="author.name"/><br/>
    价格：<input type="text" name="price"/><br/>
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

<a href="/mvc/book/param?bid=123&username1=root">获取请求参数</a><br/>

<a href="/mvc/hello3">返回参数到界面</a><br/>
<a href="/mvc/hello4">返回mv到界面</a><br/>
<a href="/mvc/hello4">从session中获取参数</a><br/>

<form action="/mvc/book/update" method="post">
    <input type="hidden" name="_method" value="put"/>
    价格：<input type="text" name="price"/>
    <button type="submit">更新图书</button>
</form>
<hr/>
<a href="/mvc/hello5">hello5转发</a><br/>
<a href="/mvc/hello6">hello6转发</a><br/>
<a href="/mvc/hello7">hello7重定向</a><br/>
<a href="/mvc/hello8">hello8重定向</a><br/>

<a href="/mvc/login">登陆</a><br/>
<a href="/mvc/tologin">登陆2</a><br/>

<a href="/mvc/hello9">自定义视图解析器</a><br/>

</body>
</html>
