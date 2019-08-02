package cn.jicl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Auther: xianzilei
 * @Date: 2019/8/1 18:42
 * @Description:
 */
//告诉SpringMVC这个类是一个处理器类
@Controller
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
}
