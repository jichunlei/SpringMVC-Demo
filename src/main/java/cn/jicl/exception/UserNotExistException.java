package cn.jicl.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @Auther: xianzilei
 * @Date: 2019/8/14 12:16
 * @Description: 自定义用户不存在异常
 */
@ResponseStatus(reason = "用户不存在！",value = HttpStatus.NOT_ACCEPTABLE)
public class UserNotExistException extends Exception{

}
