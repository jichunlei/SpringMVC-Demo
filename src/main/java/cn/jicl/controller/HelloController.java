package cn.jicl.controller;

import cn.jicl.pojo.Person;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
    @RequestMapping(value = "/hello1",method = RequestMethod.GET)
    public String hello01(){
        System.out.println("hello！");
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

    /**
     * @Description: 自定义视图解析器
     * @return: java.lang.String
     * @auther: xianzilei
     * @date: 2019/8/6 22:24
     **/
    @RequestMapping(value = "/hello9")
    public String hello09(Model model){
        System.out.println("hello09");
        model.addAttribute("hello","hello9");
        return "myView:/hello9";
    }

    /**
     * @Description: 数据转换/数据格式化/数据校验
     * @return: java.lang.String
     * @auther: xianzilei
     * @date: 2019/8/6 22:24
     **/
    @RequestMapping(value = "/add")
    public String hello10(@Valid Person person, BindingResult result){
        if(result.hasErrors()){
            List<FieldError> fieldErrors = result.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                System.out.println(fieldError.getField()+"的错误信息>>>"+fieldError.getDefaultMessage());
                System.out.println(fieldError.getField()+"的错误代码>>>"+fieldError.getCode());
                System.out.println(fieldError.getField()+"的完整错误信息>>>"+fieldError);
            }
            return "add";
        }
        System.out.println(person);
        return "success";
    }

    @RequestMapping(value = "/add_page")
    public String hello11(Model model){
        model.addAttribute("person",new Person("贤子磊"));
        return "add";
    }

    @RequestMapping("/ajaxRequest")
    @ResponseBody
    public Person ajaxRequest(){
        return new Person("贤子磊");
    }

    @RequestMapping("/getResponseBody")
    public String getResponseBody(@RequestBody Person person){
        System.out.println(person);
        return "success";
    }

    /**
     * @Description: 获取所有请求信息，包括请求头
     * @param entity 1
     * @return: java.lang.String
     * @auther: xianzilei
     * @date: 2019/8/12 8:40
     **/
    @RequestMapping("/getHttpEntity")
    public String getHttpEntity(HttpEntity<String> entity){
        System.out.println(entity);
        return "success";
    }

    /**
     * @Description: 直接将返回信息写到返回体中
     * @return: java.lang.String
     * @auther: xianzilei
     * @date: 2019/8/12 8:45
     **/
    @ResponseBody
    @RequestMapping("/getResponseBody1")
    public String getResponseBody1(){
        return "<h1>success</h1>";
    }

    /**
     * @Description: 自定义请求头和请求体返回给界面
     * @return: java.lang.String
     * @auther: xianzilei
     * @date: 2019/8/12 8:45
     **/
    @ResponseBody
    @RequestMapping("/getResponseEntity")
    public ResponseEntity<String> getResponseEntity(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type","text/html;charset=UTF-8");
        httpHeaders.set("HAHAHA","AAA");
        ResponseEntity<String> responseEntity = new ResponseEntity<>("<h1>hello1<h1>", httpHeaders, HttpStatus.OK);
        return responseEntity;
    }
}
