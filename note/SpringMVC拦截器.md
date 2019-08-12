

 SpringMVC拦截器

## 1、介绍

SpringMVC提供了拦截器机制：允许允许目标方法之前进行一些拦截工作，或者目标方法运行之后进行一些其他处理。（HandlerInterceptor）

![HandlerInterceptor](D:\学习笔记\Java\框架\SringMVC\HandlerInterceptor.png)

* 方法介绍：

```java
//1、preHandle方法：进入Handler方法之前执行。可以用于身份认证、身份授权。比如如果认证没有通过表示用户没有登陆，需要此方法拦截不再往下执行（return false），否则就放行（return true）。
boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception;

//2、postHandle：进入Handler方法之后，返回ModelAndView之前执行。可以看到该方法中有个modelAndView的形参。应用场景：从modelAndView出发：将公用的模型数据（比如菜单导航之类的）在这里传到视图，也可以在这里同一指定视图
void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception;

//3、afterCompletion：执行Handler完成之后执行。应用场景：统一异常处理，统一日志处理等
void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception;
```

* 自定义拦截器

  * 实现HandlerInterceptor接口

    ```java
    public class MyFirstInterceptor implements HandlerInterceptor {
        
    @Override
    	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler) throws Exception 
    	{
            System.out.println("preHandle...");
            return true;
        }
    
        @Override
        public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception 
        {
            System.out.println("postHandle...");
        }
        
        @Override
        public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception 
        {
            System.out.println("afterCompletion...");
        }
    ```

    

  * 定义目标方法

    ```java
     @RequestMapping(value = "/hello1",method = RequestMethod.GET)
     public String hello01()
     {
         System.out.println("hello！");
          return "hello";
     }
    ```

    

  * 跳转页面

    ```html
    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <html>
    <head>
        <title>hello</title>
    </head>
    <body>
    <%
        System.out.println("hello.jsp");
    %>
        hello
    </body>
    </html>
    
    ```

    

  * mvc配置文件中配置拦截器

  ```xml
  <!--配置拦截器-->
  <mvc:interceptors>
      <!--此配置默认拦截所有请求-->
      <bean class="cn.jicl.interceptor.MyFirstInterceptor"/>
  </mvc:interceptors>
  ```

  * 运行结果

    ```
    preHandle...
    hello！
    postHandle...
    hello.jsp
    afterCompletion...
    ```

* 拦截器执行流程
  * 正常执行流程：拦截器preHandle-->目标方法-->拦截器postHandle-->跳转页面-->拦截器afterCompletion
  * 只要preHandle不放行，后续流程均不存在
  * 只要preHandle放行，afterCompletion一定执行
  * 多个拦截器：
    * 同过滤器执行原理（先配置先执行）
    * 正常情况下：preHandle顺序执行，postHandle和afterCompletion逆序执行
    * 已经放行的拦截器，它对应的afterCompletion一定执行