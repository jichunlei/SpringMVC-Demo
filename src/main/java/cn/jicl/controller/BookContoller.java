package cn.jicl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Auther: xianzilei
 * @Date: 2019/8/2 08:57
 * @Description:
 */
@Controller
@RequestMapping("/book")
public class BookContoller {

    /**
     * @Description: 新增图书
     * @return: java.lang.String
     * @auther: xianzilei
     * @date: 2019/8/3 8:03
     **/
    @RequestMapping(method = RequestMethod.POST)
    public String addBook() {
        System.out.println("新增图书成功！");
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
    public String getRequestParams(Integer bid, @RequestParam(value = "username",required = false,defaultValue = "未知用户") String user) {
        System.out.println("获取参数bid：" + bid);
        System.out.println("获取参数user：" + user);
        return "success";
    }
}
