package cn.jicl.controller;

import cn.jicl.pojo.Person;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.Map;

/**
 * @Auther: xianzilei
 * @Date: 2019/8/1 18:42
 * @Description:
 */
//告诉SpringMVC这个类是一个处理器类
@Controller
//@SessionAttributes(value = {"msg","msg2"},types = {String.class})
@SessionAttributes(value = {"msg","msg2"},types = {String.class})
public class HelloController {

    //其中的/可以省略不写
    @RequestMapping(value = "/hello1",method = RequestMethod.POST)
    public String hello01(){
        System.out.println("处理请求完成！");
        //视图解析器字段拼串
        return "hello";
    }

    @RequestMapping(value = "/hello2/{id}")
    public String hello02(@PathVariable("id") int id){
        System.out.println("打印参数:"+id);
        //视图解析器字段拼串
        return "hello";
    }

    @RequestMapping(value = "/hello3")
    public String hello03(Map<String,Object> map1, Model model, ModelMap modelMap){
        //不管参数是map、model还是modelMap，
        //最终实现类都是org.springframework.validation.support.BindingAwareModelMap
        System.out.println(map1.getClass());
        System.out.println(model.getClass());
        System.out.println(modelMap.getClass());
        map1.put("msg","hello");
        model.addAttribute("msg2",new Date());
        modelMap.addAttribute("msg3",new Person("咸子磊"));
        //视图解析器字段拼串
        return "hello";
    }

    @RequestMapping(value = "/hello4")
    public ModelAndView hello04(){
        ModelAndView modelAndView = new ModelAndView("hello");
        modelAndView.addObject("msg",new Person("咸子磊"));
        modelAndView.addObject("msg2",new Date());
        modelAndView.addObject("msg3","hello");
        modelAndView.addObject("msg4",11);
        return modelAndView;
    }

    /**
     * @Description: forward:表示转发，转发到当前项目下的xx资源
     * @return: java.lang.String
     * @auther: xianzilei
     * @date: 2019/8/6 22:24
     **/
    @RequestMapping(value = "/hello5")
    public String hello05(){
        System.out.println("hello05");
        //转发到页面
        return "forward:/hello.jsp";
    }

    /**
     * @Description: forward:转发到mapping
     * @return: java.lang.String
     * @auther: xianzilei
     * @date: 2019/8/6 22:24
     **/
    @RequestMapping(value = "/hello6")
    public String hello06(){
        System.out.println("hello06");
        //转发到其他的mapping
        return "forward:/hello5";
    }

    /**
     * @Description: redirect:表示重定向，转发到当前项目下的xx资源
     * @return: java.lang.String
     * @auther: xianzilei
     * @date: 2019/8/6 22:24
     **/
    @RequestMapping(value = "/hello7")
    public String hello07(){
        System.out.println("hello07");
        //转发到页面
        return "redirect:/hello.jsp";
    }

    /**
     * @Description: forward:重定向到mapping
     * @return: java.lang.String
     * @auther: xianzilei
     * @date: 2019/8/6 22:24
     **/
    @RequestMapping(value = "/hello8")
    public String hello08(){
        System.out.println("hello08");
        //转发到其他的mapping
        return "redirect:/hello7";
    }
}
