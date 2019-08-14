# SpringMCV与Spring整合

[TOC]

### 1、整合目的

* 目的就是为了分工明确
* SpringMVC配置文件：配置网页转发等相关逻辑。例如视图解析器、文件上传解析器、支持ajax等
* Spring配置文件：配置和业务处理相关的。例如事务控制、数据源等

### 2、整合细节

* Spring和SpringMVC各有一个容器，其中Spring容器和SpringMVC容器为父子关系
* 子容器可以获取父容器的注入要素，但是父容器不能获取子容器数据
* 即Spring容器Bean不能引用SpringMVC容器中的bean，但是SpringMVC容器Bean可以引用Spring容器中的bean

### 3、整合步骤

##### 3.1 Spring配置文件的配置（applicationContext.xml）

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:p="http://www.springframework.org/schema/p"
       xmlns:util="http://www.springframework.org/schema/util"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                           http://www.springframework.org/schema/beans/spring-beans.xsd
                           http://www.springframework.org/schema/util
                           http://www.springframework.org/schema/util/spring-util-4.0.xsd
                           http://www.springframework.org/schema/context
                           http://www.springframework.org/schema/context/spring-context-4.2.xsd
                           http://www.springframework.org/schema/aop
                           http://www.springframework.org/schema/aop/spring-aop-4.2.xsd
                           http://www.springframework.org/schema/tx
                           http://www.springframework.org/schema/tx/spring-tx-4.2.xsd">
    <!--Spring配置文件-->


    <!-- 使用注解的方式进行开发，扫描非Controller和ControllerAdvice注解的包 -->
    <context:component-scan base-package="cn.jicl">
        <context:exclude-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
        <context:exclude-filter type="annotation" expression="org.springframework.web.bind.annotation.ControllerAdvice"/>
    </context:component-scan>

    <!--引用外部配置文件-->
    <context:property-placeholder location="classpath:config/db.properties"/>
    <!--配置数据源-->
    <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
        <property name="user" value="${jdbc_username}"/>
        <property name="password" value="${jdbc_password}"/>
        <property name="jdbcUrl" value="${jdbc_jdbcUrl}"/>
        <property name="driverClass" value="${jdbc_driverClass}"/>
    </bean>

    <!-- 配置JdbcTemplate对应的bean，并装配dataSource数据源属性 -->
    <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
        <property name="dataSource" ref="dataSource"></property>
    </bean>

    <!--事务控制-->
    <!--配置事务管理器（进行事务控制）-->
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <!--配置数据源-->
        <property name="dataSource" ref="dataSource"/>
    </bean>
    <!--2.开启基于事务注解的事务注解模式（依赖tx名称空间）-->
    <tx:annotation-driven/>
</beans>
```

##### 3.2、 配置SpringMVC配置文件（dispatcher-servlet.xml）

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd http://www.springframework.org/schema/mvc http://www.springframework.org/schema/mvc/spring-mvc.xsd">
    <!--此文件负责整个mvc中的配置-->

    <!--启用spring的一些annotation -->
    <context:annotation-config/>

    <!-- 使用注解的方式进行开发，扫描Controller和和ControllerAdvice注解的类 -->
    <context:component-scan base-package="cn.jicl" use-default-filters="false">
        <context:include-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
        <context:include-filter type="annotation" expression="org.springframework.web.bind.annotation.ControllerAdvice"/>
    </context:component-scan>

    <!--静态资源映射-->
    <!--本项目把静态资源放在了webapp的statics目录下，资源映射如下-->
    <mvc:resources mapping="/css/**" location="/statics/css/"/>
    <mvc:resources mapping="/js/**" location="/statics/js/"/>
    <mvc:resources mapping="/image/**" location="/statics/images/"/>
    <!--这句要加上，要不然可能会访问不到静态资源，具体作用自行百度-->
    <mvc:default-servlet-handler />

    <!-- 对模型视图名称的解析，即在模型视图名称添加前后缀(如果最后一个还是表示文件夹,则最后的斜杠不要漏了) 使用JSP-->
    <!-- 默认的视图解析器 在上边的解析错误时使用 (默认使用html)- -->
    <!--视图解析器：能够帮我们拼接页面访问地址-->
    <bean id="defaultViewResolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <property name="viewClass" value="org.springframework.web.servlet.view.JstlView"/>
        <property name="prefix" value="/views/"/><!--设置JSP文件的目录位置-->
        <property name="suffix" value=".jsp"/>
        <property name="exposeContextBeansAsAttributes" value="true"/>
    </bean>

    <!--配置自定义视图解析器-->
    <bean class="cn.jicl.view.MyViewResolver" id="myViewResolver">
        <property name="order" value="1"/>
    </bean>

    <!--将国际化配置文件交给SpringMVC管理，其中id必须为messageSource-->
    <bean class="org.springframework.context.support.ResourceBundleMessageSource" id="messageSource">
        <property name="basename" value="i18n.login"></property>
    </bean>

    <!--指定那个请求映射哪个页面，无需写代码配置
        但是会导致其他请求无法映射，所以需要加上配置mvc:annotation-driven-->
    <mvc:view-controller path="/tologin" view-name="login"/>
    <!--开启mvc注解驱动-->
    <!-- 配置注解驱动 可以将request参数与绑定到controller参数上 -->
    <mvc:annotation-driven conversion-service="conversionService"/>
    <mvc:default-servlet-handler/>

    <!--配置自定义转换器
        建议使用FormattingConversionServiceFactoryBean,不仅有数据转换作用，而且具有数据格式化作用-->
    <bean id="conversionService" class="org.springframework.format.support.FormattingConversionServiceFactoryBean">
        <property name="converters">
            <set>
                <ref bean="myConverter"/>
            </set>
        </property>
    </bean>

    <!--配置文件上传解析器（id必填）-->
    <bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
        <property name="maxUploadSize" value="#{1024*1024*20}"/>
        <property name="defaultEncoding" value="UTF-8"/>
    </bean>

    <!--配置拦截器-->
    <mvc:interceptors>
        <!--此配置默认拦截所有请求-->
        <bean class="cn.jicl.interceptor.MyFirstInterceptor"/>
        <mvc:interceptor>
            <mvc:mapping path="/login"/>
            <bean class="org.springframework.web.servlet.i18n.LocaleChangeInterceptor"/>
        </mvc:interceptor>
    </mvc:interceptors>

    <!--自定义区域信息解析器-->
    <!--<bean class="cn.jicl.resolver.locale.MyLocaleResolver" id="localeResolver"/>-->
    <!--使用SessionLocaleResolver区域信息解析器-->
    <bean class="org.springframework.web.servlet.i18n.SessionLocaleResolver" id="localeResolver"/>

    <!--配置异常处理器-->
    <bean id="exceptionResolver" class="org.springframework.web.servlet.handler.SimpleMappingExceptionResolver">
        <!--配置异常类型及转发页面-->
        <property name="exceptionMappings">
            <props>
                <!--key：异常类型；value：转发页面（视图解析器会拼接前后缀）-->
                <prop key="java.lang.ArrayIndexOutOfBoundsException">error</prop>
            </props>
        </property>
        <!--配置异常封装属性名-->
        <property name="exceptionAttribute" value="ex" />
    </bean>
</beans>
```

##### 3.3、web.xml配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
         version="3.1">

    <display-name>Archetype Created Web Application</display-name>
    <!--welcome pages-->
    <welcome-file-list>
        <welcome-file>index.jsp</welcome-file>
    </welcome-file-list>

    <!-- 配置监听器，项目启动时加载spring配置信息,Spring框架的底层是listener -->
    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>/WEB-INF/applicationContext.xml</param-value>
    </context-param>

    <!--配置springmvc DispatcherServlet,SpringMVC底层是servlet-->
    <servlet>
        <servlet-name>springMVC</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <!-- 指定SpringMVC框架的配置文件的路径和名称 -->
            <param-name>contextConfigLocation</param-name>
            <param-value>/WEB-INF/dispatcher-servlet.xml</param-value>
        </init-param>
        <!--配置服务启动时创建对象的优先级（值越小优先级越高）-->
        <load-on-startup>1</load-on-startup>
        <async-supported>true</async-supported>
    </servlet>
    <servlet-mapping>
        <servlet-name>springMVC</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>

    <!--定义编码过滤器-->
    <!--必须定义在其他所有filter之前，否则还是乱码-->
    <filter>
        <filter-name>characterEncodingFilter</filter-name>
        <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
        <!--定义编码类型-->
        <init-param>
            <param-name>encoding</param-name>
            <param-value>UTF-8</param-value>
        </init-param>
        <!--定义是否强制设置在请求参数中-->
        <init-param>
            <param-name>forceRequestEncoding</param-name>
            <param-value>true</param-value>
        </init-param>
        <!--定义是否强制设置在响应参数中-->
        <init-param>
            <param-name>forceResponseEncoding</param-name>
            <param-value>true</param-value>
        </init-param>
    </filter>
    <filter-mapping>
        <filter-name>characterEncodingFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>

    <!--定义表单方法过滤器-->
    <!--用来实现提交表单发起DELETE、PUT请求-->
    <filter>
        <filter-name>HttpMethodFilter</filter-name>
        <filter-class>org.springframework.web.filter.HiddenHttpMethodFilter</filter-class>
    </filter>
    <filter-mapping>
        <filter-name>HttpMethodFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>

</web-app>
```

##### 3.4、其余配置

* 数据库连接信息配置（db.properties）

  ```properties
  jdbc_username=root
  jdbc_password=123
  jdbc_jdbcUrl=jdbc:mysql://localhost:3306/test?characterEncoding=utf8
  jdbc_driverClass=com.mysql.jdbc.Driver
  ```

### 4、Spring的IOC容器和SpringMVC的IOC容器的关系

![Spring的IOC容器和SpringMVC的IOC容器的关系](D:\GitRepositories\SpringMVC-Demo\note\Spring的IOC容器和SpringMVC的IOC容器的关系.png)



