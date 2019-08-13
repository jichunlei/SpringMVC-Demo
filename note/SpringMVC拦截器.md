



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

* 源码分析：

  * doDispatch方法

  ```java
  //doDispatch方法
  protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception 
  {
  		HttpServletRequest processedRequest = request;
  		HandlerExecutionChain mappedHandler = null;
  		boolean multipartRequestParsed = false;
  
  		WebAsyncManager asyncManager =  WebAsyncUtils.getAsyncManager(request);
  
  		try {
  			ModelAndView mv = null;
  			Exception dispatchException = null;
  
  			try {
  				processedRequest = checkMultipart(request);
  				multipartRequestParsed = (processedRequest != request);
  
  				// Determine handler for the current request.
  				//拿到方法的执行链，包含拦截器
  				mappedHandler = getHandler(processedRequest);
  				if (mappedHandler == null || mappedHandler.getHandler() == null) {
  					noHandlerFound(processedRequest, response);
  					return;
  				}
  
  				// Determine handler adapter for the current request.
  				HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());
  
  				// Process last-modified header, if supported by the handler.
  				String method = request.getMethod();
  				boolean isGet = "GET".equals(method);
  				if (isGet || "HEAD".equals(method)) {
  					long lastModified = ha.getLastModified(request, mappedHandler.getHandler());
  					if (logger.isDebugEnabled()) {
  						logger.debug("Last-Modified value for [" + getRequestUri(request) + "] is: " + lastModified);
  					}
  					if (new ServletWebRequest(request, response).checkNotModified(lastModified) && isGet) {
  						return;
  					}
  				}
  				//拦截器的preHandle执行位置
  				if (!mappedHandler.applyPreHandle(processedRequest, response)) {
  					return;
  				}
  
  				// Actually invoke the handler.
  				mv = ha.handle(processedRequest, response, mappedHandler.getHandler());
  
  				if (asyncManager.isConcurrentHandlingStarted()) {
  					return;
  				}
  
  				applyDefaultViewName(processedRequest, mv);
  				//执行拦截器的postHandle方法
  				mappedHandler.applyPostHandle(processedRequest, response, mv);
  			}
  			catch (Exception ex) {
  				dispatchException = ex;
  			}
  			catch (Throwable err) {
  				// As of 4.3, we're processing Errors thrown from handler methods as well,
  				// making them available for @ExceptionHandler methods and other scenarios.
  				dispatchException = new NestedServletException("Handler dispatch failed", err);
  			}
  			processDispatchResult(processedRequest, response, mappedHandler, mv, dispatchException);
  		}
  		catch (Exception ex) {
  			//try内任何期间有异常，执行拦截器的afterCompletion方法
  			triggerAfterCompletion(processedRequest, response, mappedHandler, ex);
  		}
  		catch (Throwable err) {
  			triggerAfterCompletion(processedRequest, response, mappedHandler,
  					new NestedServletException("Handler processing failed", err));
  		}
  		finally {
  			if (asyncManager.isConcurrentHandlingStarted()) {
  				// Instead of postHandle and afterCompletion
  				if (mappedHandler != null) {
  					mappedHandler.applyAfterConcurrentHandlingStarted(processedRequest, response);
  				}
  			}
  			else {
  				// Clean up any resources used by a multipart request.
  				if (multipartRequestParsed) {
  					cleanupMultipart(processedRequest);
  				}
  			}
  		}
  	}
  ```

  * applyPreHandle方法：执行拦截器的preHandle方法

    ```java
    /**
    * Apply preHandle methods of registered interceptors.
    * @return {@code true} if the execution chain should proceed with the
    * next interceptor or the handler itself. Else, DispatcherServlet assumes
    * that this interceptor has already dealt with the response itself.
    */
    boolean applyPreHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HandlerInterceptor[] interceptors = getInterceptors();
        if (!ObjectUtils.isEmpty(interceptors)) {
            for (int i = 0; i < interceptors.length; i++) {
                HandlerInterceptor interceptor = interceptors[i];
                if (!interceptor.preHandle(request, response, this.handler)) {
                //如果某一个拦截器返回false，则继续执行完该拦截器的afterCompletion之后返回false
                    triggerAfterCompletion(request, response, null);
                    return false;
                }
                //记录最后一个拦截器的索引,为之后选择执行哪几个拦截器的afterCompletion
                this.interceptorIndex = i;
            }
        }
        return true;
    }
    ```

  * applyPostHandle方法：执行拦截器的postHandle方法

    ```java
    
    /**
     * Apply postHandle methods of registered interceptors.
     */
    void applyPostHandle(HttpServletRequest request, HttpServletResponse response, ModelAndView mv) throws Exception {
        HandlerInterceptor[] interceptors = getInterceptors();
        if (!ObjectUtils.isEmpty(interceptors)) {
            //逆序执行每一个拦截器的postHandle方法
            for (int i = interceptors.length - 1; i >= 0; i--) {
                HandlerInterceptor interceptor = interceptors[i];
                interceptor.postHandle(request, response, this.handler, mv);
            }
        }
    }
    ```

  * triggerAfterCompletion方法：执行拦截器的afterCompletion方法

    ```java
    /**
     * Handle the result of handler selection and handler invocation, which is
     * either a ModelAndView or an Exception to be resolved to a ModelAndView.
     */
    //页面渲染方法
    private void processDispatchResult(HttpServletRequest request, HttpServletResponse response,
                                       HandlerExecutionChain mappedHandler, ModelAndView mv, Exception exception) throws Exception {
    
        boolean errorView = false;
    
        if (exception != null) {
            if (exception instanceof ModelAndViewDefiningException) {
                logger.debug("ModelAndViewDefiningException encountered", exception);
                mv = ((ModelAndViewDefiningException) exception).getModelAndView();
            }
            else {
                Object handler = (mappedHandler != null ? mappedHandler.getHandler() : null);
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
            //如果页面渲染成功执行拦截器的afterCompletion方法,即使页面渲染失败，外层的catch中也会执行，所以一旦放行afterCompletion总会执行
            mappedHandler.triggerAfterCompletion(request, response, null);
        }
    }
    
    
    
    
    ----------------------------------------------------------------------------------
        /**
    	 * Trigger afterCompletion callbacks on the mapped HandlerInterceptors.
    	 * Will just invoke afterCompletion for all interceptors whose preHandle invocation
    	 * has successfully completed and returned true.
    	 */
        //执行拦截器的afterCompletion方法
        void triggerAfterCompletion(HttpServletRequest request, HttpServletResponse response, Exception ex)
        throws Exception {
        HandlerInterceptor[] interceptors = getInterceptors();
        if (!ObjectUtils.isEmpty(interceptors)) {
            //interceptorIndex记录最后一个拦截器的索引，然后逆序放行
            for (int i = this.interceptorIndex; i >= 0; i--) {
                HandlerInterceptor interceptor = interceptors[i];
                try {
                    interceptor.afterCompletion(request, response, this.handler, ex);
                }
                catch (Throwable ex2) {
                    logger.error("HandlerInterceptor.afterCompletion threw exception", ex2);
                }
            }
        }
    }
    ```

    