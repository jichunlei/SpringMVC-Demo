

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

  1. 方式一：自定义区域信息解析器

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

  
  2.  方式二：使用mvc提供的解析器（SessionLocaleResolver）
  
     * 往session中传递Locale信息
  
       ```java
       @RequestMapping("/login2")
       public String toLoginPage2(@RequestParam(value = "locale",required = false) String localeStr,  HttpSession session)
       {
           Locale locale;
           if("zh_CN".equals(localeStr)){
               locale=new Locale("zh","CN");
           }else if("en_US".equals(localeStr)){
               locale=new Locale("en","US");
           }else{
               log.warn("未知的区域信息！");
               locale=new Locale("zh","CN");
           }
           //将区域信息赋值到session中
           session.setAttribute(SessionLocaleResolver.class.getName() + ".LOCALE",locale);
           return "login";
       }
       ```
  
     * 配置SessionLocaleResolver到SpringMVC配置文件中
  
       ```xml
       <!--使用SessionLocaleResolver区域信息解析器-->
       <bean class="org.springframework.web.servlet.i18n.SessionLocaleResolver" id="localeResolver"/>
       ```
  
     * 页面传递locale传递参数（同方式一）
  
  3. 方式三：使用LocaleChangeInterceptor拦截器配合SessionLocaleResolver
  
     * 配置LocaleChangeInterceptor拦截器到mvc配置文件则，并配置上需要拦截的url即可
  
       ```xml
       <!--配置拦截器-->
       <mvc:interceptors>
           <mvc:interceptor>
               <mvc:mapping path="/login"/>
               <bean class="org.springframework.web.servlet.i18n.LocaleChangeInterceptor"/>
           </mvc:interceptor>
       </mvc:interceptors>
       
       <!--使用SessionLocaleResolver区域信息解析器-->
       <bean class="org.springframework.web.servlet.i18n.SessionLocaleResolver" id="localeResolver"/>
       ```

* 拦截器与过滤器

  | --比较点       | --filter                                   | --Interceptor                                           |
  | -------------- | ------------------------------------------ | ------------------------------------------------------- |
  | 多个的执行顺序 | 根据filter mapping配置的先后顺序           | 按照配置的顺序，但是可以通过order控制顺序               |
  | 规范           | 在Servlet规范中定义的，是Servlet容器支持的 | Spring容器内的，是Spring框架支持的。                    |
  | 使用范围       | 只能用于Web程序中                          | 既可以用于Web程序，也可以用于Application、Swing程序中。 |
  | 深度           | Filter在只在Servlet前后起作用              | 拦截器能够深入到方法前后、异常抛出前后等                |

  * 总结：

    * 过滤器（filter）：它依赖于servlet容器。在实现上，基于函数回调，它可以对几乎所有请求进行过滤，但是缺点是一个过滤器实例只能在容器初始化时调用一次。使用过滤器的目的，是用来做一些过滤操作，获取我们想要获取的数据，比如：在Javaweb中，对传入的request、response提前过滤掉一些信息，或者提前设置一些参数，然后再传入servlet或者Controller进行业务逻辑操作。通常用的场景是：在过滤器中修改字符编码（CharacterEncodingFilter）、在过滤器中修改HttpServletRequest的一些参数（XSSFilter(自定义过滤器)），如：过滤低俗文字、危险字符等。

    * 拦截器（Interceptor）：它依赖于web框架，在SpringMVC中就是依赖于SpringMVC框架。在实现上,基于Java的反射机制，属于面向切面编程（AOP）的一种运用，就是在service或者一个方法前，调用一个方法，或者在方法后，调用一个方法，比如动态代理就是拦截器的简单实现，在调用方法前打印出字符串（或者做其它业务逻辑的操作），也可以在调用方法后打印出字符串，甚至在抛出异常的时候做业务逻辑的操作。由于拦截器是基于web框架的调用，因此可以使用Spring的依赖注入（DI）进行一些业务操作，同时一个拦截器实例在一个controller生命周期之内可以多次调用。但是缺点是只能对controller请求进行拦截，对其他的一些比如直接访问静态资源的请求则没办法进行拦截处理。

    * 两者的本质区别：拦截器（Interceptor）是基于Java的反射机制，而过滤器（Filter）是基于函数回调。从灵活性上说拦截器功能更强大些，Filter能做的事情，都能做，而且可以在请求前，请求后执行，比较灵活。Filter主要是针对URL地址做一个编码的事情、过滤掉没用的参数、安全校验（比较泛的，比如登录不登录之类），太细的话，还是建议用interceptor。不过还是根据不同情况选择合适的。

    * 执行顺序：

      ​	过滤前-->拦截前-->action执行-->拦截后-->过滤后

    * ![图示](D:\GitRepositories\SpringMVC-Demo\note\拦截器与过滤器.png)