# SpringMVC异常处理机制

[TOC]

### SpringMVC九大组件之一：HandlerExceptionResolver

##### 一、HandlerExceptionResolver介绍

* DispatcherServlet#initHandlerExceptionResolvers方法：初始化HandlerExceptionResolver

```java
/**
 * Initialize the HandlerExceptionResolver used by this class.
 * <p>If no bean is defined with the given name in the BeanFactory for this namespace,
 * we default to no exception resolver.
 */
private void initHandlerExceptionResolvers(ApplicationContext context) 
{
    this.handlerExceptionResolvers = null;

    if (this.detectAllHandlerExceptionResolvers) {
        // Find all HandlerExceptionResolvers in the ApplicationContext, including ancestor contexts.
        Map<String, HandlerExceptionResolver> matchingBeans = BeanFactoryUtils
            .beansOfTypeIncludingAncestors(context, HandlerExceptionResolver.class, true, false);
        if (!matchingBeans.isEmpty()) {
            this.handlerExceptionResolvers = new ArrayList<HandlerExceptionResolver>(matchingBeans.values());
            // We keep HandlerExceptionResolvers in sorted order.
            AnnotationAwareOrderComparator.sort(this.handlerExceptionResolvers);
        }
    }
    else {
        try {
            HandlerExceptionResolver her =
                context.getBean(HANDLER_EXCEPTION_RESOLVER_BEAN_NAME, HandlerExceptionResolver.class);
            this.handlerExceptionResolvers = Collections.singletonList(her);
        }
        catch (NoSuchBeanDefinitionException ex) {
            // Ignore, no HandlerExceptionResolver is fine too.
        }
    }

    // Ensure we have at least some HandlerExceptionResolvers, by registering
    // default HandlerExceptionResolvers if no other resolvers are found.
    if (this.handlerExceptionResolvers == null) {
        this.handlerExceptionResolvers = getDefaultStrategies(context, HandlerExceptionResolver.class);
        if (logger.isDebugEnabled()) {
            logger.debug("No HandlerExceptionResolvers found in servlet '" + getServletName() + "': using default");
        }
    }
}
```

* 默认的HandlerExceptionResolver（DispatcherServlet.properties中）

  ```properties
  org.springframework.web.servlet.HandlerExceptionResolver=org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerExceptionResolver,\
  	org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver,\
  	org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver
  ```

##### 二、SpringMVC中的异常处理流程

* 首先进入到doDispatch方法中

  ```java
  //截取部分代码
      try {
          //省略部分代码
          ............
          // 执行目标方法
          mv = ha.handle(processedRequest, response, mappedHandler.getHandler());
          ............
      }
  	//如果执行目标方法发送了异常，将异常传递给dispatchException
      catch (Exception ex) {
          dispatchException = ex;
      }
  	//如果执行目标方法发生了error错误，new NestedServletException返回
      catch (Throwable err) {
          // As of 4.3, we're processing Errors thrown from handler methods as well,
          // making them available for @ExceptionHandler methods and other scenarios.
          dispatchException = new NestedServletException("Handler dispatch failed", err);
      }
  	//将异常传递到视图解析流程中
      processDispatchResult(processedRequest, response, mappedHandler, mv, dispatchException);
  ```

* 发生异常后将异常信息传递到processDispatchResult方法中

  ```java
  /**
   * Handle the result of handler selection and handler invocation, which is
   * either a ModelAndView or an Exception to be resolved to a ModelAndView.
   */
  private void processDispatchResult(HttpServletRequest request, HttpServletResponse response,HandlerExecutionChain mappedHandler, ModelAndView mv, Exception exception) throws Exception {
  
      boolean errorView = false;
  	//如果发生了异常，进入该层处理
      if (exception != null) {
          //判断是否是ModelAndViewDefiningException异常
          if (exception instanceof ModelAndViewDefiningException) {
              logger.debug("ModelAndViewDefiningException encountered", exception);
              mv = ((ModelAndViewDefiningException) exception).getModelAndView();
          }
          //如果不是ModelAndViewDefiningException异常，进入下面处理
          else {
              Object handler = (mappedHandler != null ? mappedHandler.getHandler() : null);
              //处理异常并返回mv
              mv = processHandlerException(request, response, handler, exception);
              errorView = (mv != null);
          }
      }
  
      // Did the handler return a view to render?
      if (mv != null && !mv.wasCleared()) {
          render(mv, request, response);
          if (errorView) {
              WebUtils.clearErrorRequestAttributes(request);
          }
      }
      else {
          if (logger.isDebugEnabled()) {
              logger.debug("Null ModelAndView returned to DispatcherServlet with name '" + getServletName() +
                           "': assuming HandlerAdapter completed request handling");
          }
      }
  
      if (WebAsyncUtils.getAsyncManager(request).isConcurrentHandlingStarted()) {
          // Concurrent handling started during a forward
          return;
      }
  
      if (mappedHandler != null) {
          mappedHandler.triggerAfterCompletion(request, response, null);
      }
  }
  ```

* 如果存在异常，且异常不是ModelAndViewDefiningException类型，则进入processHandlerException方法处理，并返回一个ModelAndView

  ```java
  /**
   * Determine an error ModelAndView via the registered HandlerExceptionResolvers.
   * @param request current HTTP request
   * @param response current HTTP response
   * @param handler the executed handler, or {@code null} if none chosen at the time of the exception
   * (for example, if multipart resolution failed)
   * @param ex the exception that got thrown during handler execution
   * @return a corresponding ModelAndView to forward to
   * @throws Exception if no error ModelAndView found
   */
  protected ModelAndView processHandlerException(HttpServletRequest request, HttpServletResponse response,Object handler, Exception ex) throws Exception {
      // Check registered HandlerExceptionResolvers...
      ModelAndView exMv = null;
      //循环所有ioc容器里面的异常解析器
      for (HandlerExceptionResolver handlerExceptionResolver : this.handlerExceptionResolvers) {
          //每个异常解析器尝试去解析异常，如果处理不了返回null
          exMv = handlerExceptionResolver.resolveException(request, response, handler, ex);
          //一旦有一个处理器可以处理，则跳出循环
          if (exMv != null) {
              break;
          }
      }
      //如果存在可以处理异常的解析器，进入下面处理
      if (exMv != null) {
          if (exMv.isEmpty()) {
              request.setAttribute(EXCEPTION_ATTRIBUTE, ex);
              return null;
          }
          // We might still need view name translation for a plain error model...
          if (!exMv.hasView()) {
              exMv.setViewName(getDefaultViewName(request));
          }
          if (logger.isDebugEnabled()) {
              logger.debug("Handler execution resulted in exception - forwarding to resolved error view: " + exMv, ex);
          }
          WebUtils.exposeErrorRequestAttributes(request, ex, getServletName());
          return exMv;
      }
  	//如果没有可以处理该异常的解析器，则直接抛出异常，之后会被tomcat的默认异常处理器解决，界面就会出现常见的tomcat异常图景
      throw ex;
  }
  ```

##### 三、SpringMVC提供的异常解析器

*  ExceptionHandlerExceptionHandler：处理@ExceptionHandler标注

  ```java
  //ControllerAdvice表示该类是全局异常处理类
  @ControllerAdvice
  public class ExceptionController {
      
      /**
       * @Description: 统一处理该类的异常--算术异常
       * @param exception 1
       * @return: org.springframework.web.servlet.ModelAndView
       * @auther: xianzilei
       * @date: 2019/8/14 8:56
       **/
      //SpringMVC只识别该方法参数上的Exception类型，
      //如果想要返回异常到页面，可以设置方法返回值为ModelAndView
      //如果有多个ExceptionHandler注解标注的方法，按照精确匹配
      @ExceptionHandler(value = ArithmeticException.class)
      public ModelAndView exceptionHandler1(Exception exception){
          System.out.println("执行exceptionHandler1...");
          System.out.println("异常信息为："+exception);
          //组装ModelAndView
          ModelAndView modelAndView=new ModelAndView("error");
          modelAndView.addObject("ex",exception);
          return modelAndView;
      }
  
      /**
       * @Description: 统一处理该类的异常--空指针异常
       * @param exception 1
       * @return: org.springframework.web.servlet.ModelAndView
       * @auther: xianzilei
       * @date: 2019/8/14 8:59
       **/
      //SpringMVC只识别该方法参数上的Exception类型，
      //如果想要返回异常到页面，可以设置方法返回值为ModelAndView
      //如果有多个ExceptionHandler注解标注的方法，按照精确匹配
      @ExceptionHandler(value = NullPointerException.class)
      public ModelAndView exceptionHandler2(Exception exception){
          System.out.println("执行exceptionHandler2...");
          System.out.println("异常信息为："+exception);
          //组装ModelAndView
          ModelAndView modelAndView=new ModelAndView("error");
          modelAndView.addObject("ex",exception);
          return modelAndView;
      }
  }
  
  ```

  

* ResponseStatusExceptionResolver：@ResponseStatus

  ```java
  /**
   * @Auther: xianzilei
   * @Date: 2019/8/14 12:16
   * @Description: 自定义用户不存在异常
   */
  @ResponseStatus(reason = "用户不存在！",value = HttpStatus.NOT_ACCEPTABLE)
  public class UserNotExistException extends Exception{
  
  }
  ```

  

* DefaultHandlerExceptionResolver：SpringMVC自带的异常

  * 如果异常是SpringMVC自带的异常，且无处理器，则DefaultHandlerExceptionResolver参与处理

    ```java
    @Override
    @SuppressWarnings("deprecation")
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            if (ex instanceof org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException) {
                return handleNoSuchRequestHandlingMethod((org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException) ex,
                                                         request, response, handler);
            }
            else if (ex instanceof HttpRequestMethodNotSupportedException) {
                return handleHttpRequestMethodNotSupported((HttpRequestMethodNotSupportedException) ex, request,
                                                           response, handler);
            }
            else if (ex instanceof HttpMediaTypeNotSupportedException) {
                return handleHttpMediaTypeNotSupported((HttpMediaTypeNotSupportedException) ex, request, response,
                                                       handler);
            }
            else if (ex instanceof HttpMediaTypeNotAcceptableException) {
                return handleHttpMediaTypeNotAcceptable((HttpMediaTypeNotAcceptableException) ex, request, response,
                                                        handler);
            }
            else if (ex instanceof MissingPathVariableException) {
                return handleMissingPathVariable((MissingPathVariableException) ex, request,
                                                 response, handler);
            }
            else if (ex instanceof MissingServletRequestParameterException) {
                return handleMissingServletRequestParameter((MissingServletRequestParameterException) ex, request,
                                                            response, handler);
            }
            else if (ex instanceof ServletRequestBindingException) {
                return handleServletRequestBindingException((ServletRequestBindingException) ex, request, response,
                                                            handler);
            }
            else if (ex instanceof ConversionNotSupportedException) {
                return handleConversionNotSupported((ConversionNotSupportedException) ex, request, response, handler);
            }
            else if (ex instanceof TypeMismatchException) {
                return handleTypeMismatch((TypeMismatchException) ex, request, response, handler);
            }
            else if (ex instanceof HttpMessageNotReadableException) {
                return handleHttpMessageNotReadable((HttpMessageNotReadableException) ex, request, response, handler);
            }
            else if (ex instanceof HttpMessageNotWritableException) {
                return handleHttpMessageNotWritable((HttpMessageNotWritableException) ex, request, response, handler);
            }
            else if (ex instanceof MethodArgumentNotValidException) {
                return handleMethodArgumentNotValidException((MethodArgumentNotValidException) ex, request, response,
                                                             handler);
            }
            else if (ex instanceof MissingServletRequestPartException) {
                return handleMissingServletRequestPartException((MissingServletRequestPartException) ex, request,
                                                                response, handler);
            }
            else if (ex instanceof BindException) {
                return handleBindException((BindException) ex, request, response, handler);
            }
            else if (ex instanceof NoHandlerFoundException) {
                return handleNoHandlerFoundException((NoHandlerFoundException) ex, request, response, handler);
            }
            else if (ex instanceof AsyncRequestTimeoutException) {
                return handleAsyncRequestTimeoutException(
                    (AsyncRequestTimeoutException) ex, request, response, handler);
            }
        }
        catch (Exception handlerException) {
            if (logger.isWarnEnabled()) {
                logger.warn("Handling of [" + ex.getClass().getName() + "] resulted in Exception", handlerException);
            }
        }
        return null;
    }
    ```

* SimpleMappingExceptionResolver：通过配置方法处理异常（优先级最低）

  ```xml
  <!--配置异常处理器SimpleMappingExceptionResolver-->
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
  ```

  