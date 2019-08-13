package cn.jicl.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.http.HttpSession;
import java.util.Locale;

/**
 * @Auther: xianzilei
 * @Date: 2019/8/7 08:33
 * @Description:
 */
@Controller
@Slf4j
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

    /**
     * @Description: 转发到登陆页面
     * @return: void
     * @auther: xianzilei
     * @date: 2019/8/7 8:34
     **/
    @RequestMapping("/login2")
    public String toLoginPage2(@RequestParam(value = "locale",required = false) String localeStr,
                               HttpSession session){
        Locale locale;
        if("zh_CN".equals(localeStr)){
            locale=new Locale("zh","CN");
        }else if("en_US".equals(localeStr)){
            locale=new Locale("en","US");
        }else{
            log.warn("未知的区域信息！");
            locale=new Locale("zh","CN");
        }
        session.setAttribute(SessionLocaleResolver.class.getName() + ".LOCALE",locale);
        return "login";
    }
}
