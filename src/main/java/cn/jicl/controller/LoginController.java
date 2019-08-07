package cn.jicl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Auther: xianzilei
 * @Date: 2019/8/7 08:33
 * @Description:
 */
@Controller
public class LoginController {

    /**
     * @Description: 转发到登陆页面
     * @return: void
     * @auther: xianzilei
     * @date: 2019/8/7 8:34
     **/
    @RequestMapping("/login")
    public String toLoginPage(){
        return "login";
    }
}
