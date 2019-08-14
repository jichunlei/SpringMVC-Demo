package cn.jicl.controller.exception;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Auther: xianzilei
 * @Date: 2019/8/14 08:43
 * @Description:
 */
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
    //如果发生异常的方法的类存在异常处理方法，则就近原则
    //全局异常处理与本类异常处理，优先本类（自扫门前雪）
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
     * @date: 2019/8/14 8:56
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
