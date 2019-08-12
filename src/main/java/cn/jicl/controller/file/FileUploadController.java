package cn.jicl.controller.file;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;

import java.io.File;
import java.io.IOException;

/**
 * @Auther: xianzilei
 * @Date: 2019/8/12 18:21
 * @Description: 文件上传处理器
 */
@Controller
@RequestMapping("/file")
public class FileUploadController {
    //文件保存位置
    private String fileUploadPath = "D:\\GitRepositories\\SpringMVC-Demo\\note\\";

    /**
     * @Description: 单文件上传
     * @param username 1
     * @param file 2
     * @return: java.lang.String
     * @auther: xianzilei
     * @date: 2019/8/12 18:39
     **/
    @RequestMapping("/upload")
    public String upload(@RequestParam("username") String username, @RequestParam("file") MultipartFile file) {
        System.out.println("用户名：" + username);
        //获取表单中文件组件的名字
        System.out.println("文件组件名：" + file.getName());
        //获取上传文件的原名
        System.out.println("文件原名：" + file.getOriginalFilename());
        try {
            file.transferTo(new File(fileUploadPath + username + "-" + file.getOriginalFilename()));
        } catch (Exception e) {
            return "error";
        }
        return "success";
    }

    /**
     * @Description: 多文件上传
     * @param username 用户名
     * @param files 文件列表
     * @return: java.lang.String
     * @auther: xianzilei
     * @date: 2019/8/12 21:48
     **/
    @RequestMapping("/uploads")
    public String uploads(@RequestParam("username") String username, @RequestParam("file") MultipartFile[] files) {
        System.out.println("用户名：" + username);
        for (MultipartFile file : files) {
            //获取表单中文件组件的名字
            System.out.println("文件组件名：" + file.getName());
            //获取上传文件的原名
            System.out.println("文件原名：" + file.getOriginalFilename());
            try {
                file.transferTo(new File(fileUploadPath + username + "-" + file.getOriginalFilename()));
            } catch (Exception e) {
                return "error";
            }
        }
        return "success";
    }
}
