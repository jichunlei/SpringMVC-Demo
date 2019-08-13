package cn.jicl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Locale;

/**
 * @Auther: xianzilei
 * @Date: 2019/8/7 08:33
 * @Description:
 */
@Controller
public class LoginController {

    @Autowired
    private MessageSource messageSource;
    /**
     * @Description: 转发到登陆页面
     * @return: void
     * @auther: xianzilei
     * @date: 2019/8/7 8:34
     **/
    @RequestMapping("/login")
    public String toLoginPage(Locale locale){
        System.out.println(locale.getLanguage());
        String username = messageSource.getMessage("login.username", null, locale);
        System.out.println(username);
        return "login";
    }
}
