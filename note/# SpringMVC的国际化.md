# SpringMVC的国际化

* 使用步骤

  * 写好国际化配置文件

    * 默认国际化配置（login.properties）

      ```properties
      login.username=用户名
      login.password=密码
      login.welcome=欢迎登陆
      login.login=登陆
      ```

    *  中文配置

      ```properties
      login.username=用户名
      login.password=密码
      login.welcome=欢迎登陆
      login.login=登陆
      ```

    * 英文配置

      ```properties
      login.username=username
      login.password=password
      login.welcome=welcome
      login.login=login
      ```

      

  * mvc配置文件中配置国际化解析器

    ```xml
    <!--将国际化配置文件交给SpringMVC管理，其中id必须为messageSource-->
    <bean class="org.springframework.context.support.ResourceBundleMessageSource" id="messageSource">
        <property name="basename" value="i18n.login"></property>
    </bean>
    ```

    

  * 页面使用fmt标签

    ```html
    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <html>
    <head>
        <title>登陆页面</title>
    </head>
    <body>
        <h2><fmt:message key="login.welcome"/></h2>
        <form action="login">
            <fmt:message key="login.username"/>:<input type="text"/><br/>
            <fmt:message key="login.password"/>:<input type="text"/><br/>
            <input type="submit" value='<fmt:message key="login.login"/>'/><br/>
        </form>
    </body>
    </html>
    ```

    

* 原理解析

  * 配置获取区域信息

    * 区域信息解析器（SpringMVC九大组件之一）：LocaleResolver

    * LocaleResolver在DispatcherServlet.properties配置文件中默认出厂设置

      ```properties
      #根据请求头来获取区域信息
      org.springframework.web.servlet.LocaleResolver=org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver
      ```

    * AcceptHeaderLocaleResolver获取区域信息：根据请求头获取区域信息

      ```java
      @Override
      public Locale resolveLocale(HttpServletRequest request) 
      {
          Locale defaultLocale = getDefaultLocale();
          if (defaultLocale != null && request.getHeader("Accept-Language") == null) 	   {
              return defaultLocale;
          }
          Locale requestLocale = request.getLocale();
          List<Locale> supportedLocales = getSupportedLocales();
          if (supportedLocales.isEmpty() || supportedLocales.contains(requestLocale))    {
              return requestLocale;
          }
          Locale supportedLocale = findSupportedLocale(request, supportedLocales);
          if (supportedLocale != null) {
              return supportedLocale;
          }
          return (defaultLocale != null ? defaultLocale : requestLocale);
      }
      ```

    * 实际获取区域信息位置（举例）

      ```java
      //视图渲染位置
      //调用resolveLocale获取区域信息
      Locale locale = this.localeResolver.resolveLocale(request);
      ```

  * 程序获取区域信息

    * controller增加参数Locale

    * 注入国际化文件管理器

      ```java
      @Autowired
      private MessageSource messageSource;
      ```

    * 获取国际化配置信息

      ```java
      String username = messageSource.getMessage("login.username", null, locale);
      ```

* 超链接实现国际化

  1. 方式一

     * 创建自定义区域信息解析器（实现LocaleResolver接口）

       ```java
       public class MyLocaleResolver implements LocaleResolver {
       
           public final static String ZH_CN="zh_CN";
           public final static String en_US="en_US";
       
           /**
            * @Description: 解析Locale
            * @param request 1
            * @return: java.util.Locale
            * @auther: xianzilei
            * @date: 2019/8/13 18:25
            **/
           @Override
           public Locale resolveLocale(HttpServletRequest request) 
           {
               Locale locale;
               String localeParameter = request.getParameter("locale");
               //如果存在且符合支持的区域信息，则使用传递的参数设置
               if(!StringUtils.isBlank(localeParameter)){
                   if(ZH_CN.equals(localeParameter)){
                       locale=new Locale("zh","CN");
                   }else if(en_US.equals(localeParameter)){
                       locale=new Locale("en","US");
                   }else{
                       log.warn("无效的区域信息！");
                       locale=request.getLocale();
                   }
               }
               //如果不存在，则使用
               else{
                   locale=request.getLocale();
               }
               return locale;
           }
       
           /**
            * @Description: 修改Locale
            * @param request 1
            * @param response 2
            * @param locale 3
            * @return: void
            * @auther: xianzilei
            * @date: 2019/8/13 18:25
            **/
           @Override
           public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) 
           {
       
           }
       }
       ```

     * 配置自定义区域信息到SpringMVC配置文件中

       ```xml
       <!--自定义区域信息解析器-->
       <bean class="cn.jicl.controller.locale.MyLocaleResolver" id="localeResolver"/>
       ```

     * 页面传递locale传递参数

       ```html
       <h2><fmt:message key="login.welcome"/></h2>
       <form action="login">
           <fmt:message key="login.username"/>:<input type="text"/><br/>
           <fmt:message key="login.password"/>:<input type="text"/><br/>
           <input type="submit" value='<fmt:message key="login.login"/>'/><br/>
           <a href="login?locale=zh_CN">中文</a> <a href="login?locale=en_US">English</a><br/>
       </form>
       ```

       

