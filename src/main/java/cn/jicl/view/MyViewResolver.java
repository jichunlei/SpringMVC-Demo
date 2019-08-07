package cn.jicl.view;

import org.springframework.core.Ordered;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.util.Locale;

/**
 * @Auther: xianzilei
 * @Date: 2019/8/7 18:08
 * @Description: 自定义视图解析器
 */
public class MyViewResolver implements ViewResolver, Ordered {

    private Integer order=0;
    /**
     * @Description: 视图解析返回视图对象
     * @param viewName 1
     * @param locale 2
     * @return: org.springframework.web.servlet.View
     * @auther: xianzilei
     * @date: 2019/8/7 18:09
     **/
    @Override
    public View resolveViewName(String viewName, Locale locale) throws Exception {
        if(viewName.startsWith("myView")){
            return new MyView();
        }
        return null;
    }

    @Override
    public int getOrder() {
        return order;
    }

    /**
     * @Description: 设置优先级（值越小优先级越高）
     * @param order 1
     * @return: void
     * @auther: xianzilei
     * @date: 2019/8/7 18:22
     **/
    public void setOrder(Integer order) {
        this.order = order;
    }
}
