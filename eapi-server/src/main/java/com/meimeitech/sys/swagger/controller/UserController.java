package com.meimeitech.sys.swagger.controller;

import com.meimeitech.sys.swagger.model.DefaultResponse;
import com.meimeitech.sys.swagger.model.EmailActivateReq;
import com.meimeitech.sys.swagger.model.EmailActivateSendReq;
import com.meimeitech.sys.swagger.model.EmailResetReq;
import com.meimeitech.sys.swagger.model.EmailResetSendReq;
import com.meimeitech.sys.swagger.model.RegisterReq;
import com.meimeitech.sys.swagger.model.UserInfoResponse;

import com.meimeitech.sys.swagger.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * 激活邮件
     * 
     * @param emailActivateReq
     * @return
     */
    @RequestMapping(value = "/user/email/activate",
        method = RequestMethod.POST)
    public DefaultResponse activateEmail( @Valid @RequestBody EmailActivateReq emailActivateReq) {

        logRequest("activateEmail" , "emailActivateReq", emailActivateReq );

        DefaultResponse response = userService.activateEmail(emailActivateReq);

        logResponse("activateEmail", response);

        return response;

    }

    /**
     * 获取用户信息
     * 
     * @return
     */
    @RequestMapping(value = "/user/info",
        method = RequestMethod.GET)
    public UserInfoResponse info() {

        logRequest("info" );

        UserInfoResponse response = userService.info();

        logResponse("info", response);

        return response;

    }

    /**
     * 注册
     * 
     * @param registerReq
     * @return
     */
    @RequestMapping(value = "/user/register",
        method = RequestMethod.POST)
    public DefaultResponse register( @Valid @RequestBody RegisterReq registerReq) {

        logRequest("register" , "registerReq", registerReq );

        DefaultResponse response = userService.register(registerReq);

        logResponse("register", response);

        return response;

    }

    /**
     * 重置密码
     * 
     * @param emailResetReq
     * @return
     */
    @RequestMapping(value = "/user/email/reset",
        method = RequestMethod.POST)
    public DefaultResponse resetPass( @Valid @RequestBody EmailResetReq emailResetReq) {

        logRequest("resetPass" , "emailResetReq", emailResetReq );

        DefaultResponse response = userService.resetPass(emailResetReq);

        logResponse("resetPass", response);

        return response;

    }

    /**
     * 重新发送激活邮件
     * 
     * @param emailActivateSendReq
     * @return
     */
    @RequestMapping(value = "/user/email/activate/send",
        method = RequestMethod.POST)
    public DefaultResponse sendActivateEmail( @Valid @RequestBody EmailActivateSendReq emailActivateSendReq) {

        logRequest("sendActivateEmail" , "emailActivateSendReq", emailActivateSendReq );

        DefaultResponse response = userService.sendActivateEmail(emailActivateSendReq);

        logResponse("sendActivateEmail", response);

        return response;

    }

    /**
     * 发送重置邮件
     * 
     * @param emailResetSendReq
     * @return
     */
    @RequestMapping(value = "/user/email/reset/send",
        method = RequestMethod.POST)
    public DefaultResponse sendResetEmail( @Valid @RequestBody EmailResetSendReq emailResetSendReq) {

        logRequest("sendResetEmail" , "emailResetSendReq", emailResetSendReq );

        DefaultResponse response = userService.sendResetEmail(emailResetSendReq);

        logResponse("sendResetEmail", response);

        return response;

    }
}
