package cn.jicl.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: xianzilei
 * @Date: 2019/8/12 22:31
 * @Description: TODO
 */
public class MyFirstInterceptor implements HandlerInterceptor {
    /**
     * @Description: 目标方法执行前调用
     * @param request 1
     * @param response 2
     * @param handler 3
     * @return: boolean
     * @auther: xianzilei
     * @date: 2019/8/12 22:32
     **/
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("preHandle...");
        return true;
    }

    /**
     * @Description: 目标方法执行后，跳转页面前执行
     * @param request 1
     * @param response 2
     * @param handler 3
     * @param modelAndView 4
     * @return: void
     * @auther: xianzilei
     * @date: 2019/8/12 22:32
     **/
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("postHandle...");
    }

    /**
     * @Description: 跳转页面后执行
     * @param request 1
     * @param response 2
     * @param handler 3
     * @param ex 4
     * @return: void
     * @auther: xianzilei
     * @date: 2019/8/12 22:32
     **/
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("afterCompletion...");
    }
}
