package com.wust.web;

import com.wust.entity.Users;
import com.wust.exception.ErrorInfo;
import com.wust.service.UserService;
import com.wust.utils.StringUtil;
import com.wust.vo.MessageVo;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private UserService userService;

    /**
     * 未登录，shiro应重定向到登录界面，此处返回未登录状态信息由前端控制跳转页面
     * @return
     */
    @ApiOperation("未登录页")
    @RequestMapping(value = "/unLogin", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public MessageVo unLogin(){

        MessageVo mess = new MessageVo();
        mess.setCode(ErrorInfo.ERROR);
        mess.setInfo("unLogin");
        mess.setData("未登录,请先登录.");

        return mess;
    }


    @ApiOperation("登录页")
    @RequestMapping(value = "/login",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public MessageVo login(@RequestBody Users users){

        System.out.println("HomeController.login()");

        String errorCode = StringUtil.getStringRandom(12);
        //打印12位随机字符串,用于日至搜索。
        logger.error("errorCode-----"+errorCode);
        MessageVo mess = new MessageVo();

        System.out.println("users =" + users);

        /**
         * @Description: 判断手机号格式是否正确
         */
        String phone = users.getPhone();
        Boolean bool = StringUtil.isHandset(phone);
        if (!bool)
        {
            mess.setCode(ErrorInfo.ERROR);
            mess.setInfo("IncorrectMobileFormatException");
            mess.setData("手机号格式错误");
            return mess;
        }

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(phone, users.getPwd());

        try {
            subject.login(token);
            //清除错误次数缓存
            userService.removeCount("user:" + users.getPhone());
            mess.setCode(ErrorInfo.OK);
            mess.setInfo("Login Success");
            mess.setData("登录成功");
        } catch (IncorrectCredentialsException e) {
            System.out.println("IncorrectCredentialsException -- > 密码不正确");

            //利用缓存记录密码输错次数
            userService.incrLoginCount("user:" + users.getPhone());

            mess.setCode(ErrorInfo.ERROR);
            mess.setInfo("IncorrectCredentialsException");
            mess.setData("密码不正确");
        } catch (LockedAccountException e) {
            System.out.println("LockedAccountException -- > 帐号被冻结");
            mess.setCode(ErrorInfo.ERROR);
            mess.setInfo("LockedAccountException");
            mess.setData("帐号被冻结");
        } catch (UnknownAccountException e) {
            System.out.println("UnknownAccountException -- > 账号不存在");
            mess.setCode(ErrorInfo.ERROR);
            mess.setInfo("UnknownAccountException");
            mess.setData("账号不存在");
        } catch (DisabledAccountException e) {
            System.out.println("DisabledAccountException -- > 帐号被锁定");
            mess.setCode(ErrorInfo.ERROR);
            mess.setInfo("DisabledAccountException");
            mess.setData("由于密码输入错误次数大于3次,帐号已经禁止登录,300秒后可重试.");
        }catch (Exception e) {
            System.out.println("UnknownError -- >" + e);
            mess.setCode(ErrorInfo.ERROR);
            mess.setInfo("UnknownError");
            mess.setData(errorCode);
            logger.error(e.getMessage());
        }
        return mess;
    }

    @ApiOperation("注销登录页")
    @RequestMapping(value = "/logout", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public MessageVo logout(){
        MessageVo mess = new MessageVo();
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        mess.setCode(ErrorInfo.OK);
        mess.setInfo("Logout Success");
        mess.setData("注销登陆成功.");
        return mess;
    }

}
