package com.meimeitech.security.core;

import com.meimeitech.common.RetCode;
import com.meimeitech.common.vo.Response;
import com.meimeitech.security.util.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by Shawn on 2017/8/15.
 */
@RestController
public class LoginController {

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @RequestMapping(value = {"/login"}, method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Response login(@RequestParam String username, @RequestParam String password, HttpServletResponse response) {
        logger.info("用户登陆：{}",username);
        Authentication token = new UsernamePasswordAuthenticationToken(username, password);
        try {
            token = authenticationManager.authenticate(token); // 登录认证
            SecurityContextHolder.getContext().setAuthentication(token);

        } catch (UsernameNotFoundException | BadCredentialsException ex) {
            return Response.response(RetCode.USERORPWDERR);
        } catch (LockedException e) {
            return Response.response(RetCode.USERLOCKED);
        } catch (AuthenticationException e) {
            return Response.response(RetCode.INTERNALEXCEP);
        }
        return Response.success(jwtTokenUtil.generate());
    }

}