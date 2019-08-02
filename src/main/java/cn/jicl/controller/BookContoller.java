package cn.jicl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Auther: xianzilei
 * @Date: 2019/8/2 08:57
 * @Description:
 */
@Controller
@RequestMapping("/book")
public class BookContoller {

    @RequestMapping(method = RequestMethod.POST)
    public String addBook() {
        System.out.println("新增图书成功！");
        return "success";
    }

    @RequestMapping(value = "/{bid}", method = RequestMethod.DELETE)
    public String deleteBook(@PathVariable("bid") Integer bid) {
        System.out.println("删除图书【" + bid + "】成功！");
        return "success";
    }

    @RequestMapping(value = "/{bid}", method = RequestMethod.PUT)
    public String updateBook(@PathVariable("bid") Integer bid) {
        System.out.println("更新图书【" + bid + "】成功！");
        return "success";
    }

    @RequestMapping(value = "/{bid}", method = RequestMethod.GET)
    public String getBook(@PathVariable("bid") Integer bid) {
        System.out.println("获取图书【" + bid + "】成功！");
        return "success";
    }
}
