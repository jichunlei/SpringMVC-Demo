package cn.jicl.view;

import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Auther: xianzilei
 * @Date: 2019/8/7 18:11
 * @Description: 自定义视图对象
 */
public class MyView implements View {
    @Override
    public String getContentType() {
        return "text/html;";
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object hello = model.get("hello");
        response.setContentType("text/html");
        response.getWriter().write("<h1>"+hello+"贤子磊<h1>");
    }
}
