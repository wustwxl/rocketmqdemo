package com.wust.exception;

import com.wust.vo.MessageVo;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: F7687967
 * @date: 2017/10/16
 * @time: 下午 04:05
 * @description: 统一异常处理, 通过使用@ControllerAdvice定义统一的异常处理类，而不是在每个Controller中逐个定义
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 返回json数据 的异常处理
     * @param ex
     * @return
     */

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    //处理访问权限不足问题
    @ExceptionHandler(value = UnauthorizedException.class)
    public MessageVo defaultErrorHandler(HttpServletRequest req, Exception ex)  {
        MessageVo mess = new MessageVo();
        mess.setCode(ErrorInfo.ERROR);
        mess.setInfo("No Permission");
        mess.setData(ex.getMessage());
        return mess;
    }

    // 拦截捕捉自定义异常 MyException.class
    @ExceptionHandler(value = MyException.class)
    public Map jsonErrorHandler(HttpServletRequest req, MyException ex) throws Exception {
        Map map = new HashMap();
        map.put("code", ErrorInfo.ERROR);
        map.put("info", "Server Error.");
        map.put("url", req.getRequestURL().toString());
        map.put("data", ex.getMessage());
        return map;
    }

    //全局异常捕捉处理
    @ExceptionHandler(value = Exception.class)
    public Map errorHandler(Exception ex) {
        Map map = new HashMap();
        map.put("code", ErrorInfo.ERROR);
        map.put("info", "Unknown Error.");
        map.put("data", ex.getMessage());
        return map;
    }

}