package cn.jicl.controller;

import cn.jicl.pojo.Book;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @Auther: xianzilei
 * @Date: 2019/8/2 08:57
 * @Description:
 */
@Controller
@RequestMapping("/book")
public class BookController {

    /**
     * @Description: 新增图书
     * @return: java.lang.String
     * @auther: xianzilei
     * @date: 2019/8/3 8:03
     **/
    @RequestMapping(method = RequestMethod.POST)
    public String addBook(Book book) {
        System.out.println("新增图书成功！"+book);
        return "success";
    }

    /**
     * @param bid 1
     * @Description: 删除图书
     * @return: java.lang.String
     * @auther: xianzilei
     * @date: 2019/8/3 8:04
     **/
    @RequestMapping(value = "/{bid}", method = RequestMethod.DELETE)
    public String deleteBook(@PathVariable("bid") Integer bid) {
        System.out.println("删除图书【" + bid + "】成功！");
        return "success";
    }

    /**
     * @param bid 1
     * @Description: 更新图书
     * @return: java.lang.String
     * @auther: xianzilei
     * @date: 2019/8/3 8:04
     **/
    @RequestMapping(value = "/{bid}", method = RequestMethod.PUT)
    public String updateBook(@PathVariable("bid") Integer bid) {
        System.out.println("更新图书【" + bid + "】成功！");
        return "success";
    }

    /**
     * @param bid 1
     * @Description: 查询图书
     * @return: java.lang.String
     * @auther: xianzilei
     * @date: 2019/8/3 8:04
     **/
    @RequestMapping(value = "/{bid}", method = RequestMethod.GET)
    public String getBook(@PathVariable("bid") Integer bid) {
        System.out.println("获取图书【" + bid + "】成功！");
        return "success";
    }

    @RequestMapping("/param")
    public String getRequestParams
            (Integer bid,
             @RequestParam(value = "username", required = false, defaultValue = "未知用户") String user,
             @RequestHeader("User-Agent") String UserAgent,
             @CookieValue("JSESSIONID") String JID) {
        System.out.println("获取请求体参数bid：" + bid);
        System.out.println("获取请求体参数user：" + user);
        System.out.println("获取请求头参数User-Agent：" + UserAgent);
        System.out.println("获取cookie参数JSESSIONID：" + JID);
        return "success";
    }

    /**
     * @Description: 自定义一个model（该方法会提前运行）
     * @param model 1
     * @return: void
     * @auther: xianzilei
     * @date: 2019/8/5 8:46
     **/
    @ModelAttribute
    public void bookModel(Model model){
        Book book = new Book();
        book.setBookName("Java编程思想");
        book.setPrice(100);
        model.addAttribute("book",book);
    }

    /**
     * @Description: 局部字段更新
     * @param book 1
     * @return: java.lang.String
     * @auther: xianzilei
     * @date: 2019/8/5 8:40
     **/
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public String updateBook(@ModelAttribute("book") Book book) {
        System.out.println("更新图书【" + book + "】成功！");
        return "success";
    }

}
