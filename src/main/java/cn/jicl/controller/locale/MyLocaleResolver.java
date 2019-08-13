package cn.jicl.controller.locale;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * @Auther: xianzilei
 * @Date: 2019/8/13 18:15
 * @Description: 自定义区域信息解析器
 */
@Slf4j
public class MyLocaleResolver implements LocaleResolver {

    public final static String ZH_CN="zh_CN";
    public final static String en_US="en_US";

    /**
     * @Description: 解析Locale
     * @param request 1
     * @return: java.util.Locale
     * @auther: xianzilei
     * @date: 2019/8/13 18:25
     **/
    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        Locale locale;
        String localeParameter = request.getParameter("locale");
        if(!StringUtils.isBlank(localeParameter)){
            if(ZH_CN.equals(localeParameter)){
                locale=new Locale("zh","CN");
            }else if(en_US.equals(localeParameter)){
                locale=new Locale("en","US");
            }else{
                log.warn("无效的区域信息！");
                locale=request.getLocale();
            }
        }else{
            locale=request.getLocale();
        }
        return locale;
    }

    /**
     * @Description: 修改Locale
     * @param request 1
     * @param response 2
     * @param locale 3
     * @return: void
     * @auther: xianzilei
     * @date: 2019/8/13 18:25
     **/
    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {

    }
}
