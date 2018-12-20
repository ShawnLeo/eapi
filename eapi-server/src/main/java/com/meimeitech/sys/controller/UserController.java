package com.meimeitech.sys.controller;

import com.meimeitech.common.util.UserContextHolder;
import com.meimeitech.common.vo.Response;
import com.meimeitech.common.vo.UserSession;
import com.meimeitech.sys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ResponseBody
    private Response info() {

        return  Response.success(userService.getUserInfo());
    }
}
