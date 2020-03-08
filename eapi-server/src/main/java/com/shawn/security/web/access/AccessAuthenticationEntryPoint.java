package com.shawn.security.web.access;

import com.shawn.common.RetCode;
import com.shawn.security.util.ResponseUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Shawn on 2017/8/15.
 */
@Component
public class AccessAuthenticationEntryPoint implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        // 认证失败，返回信息
        ResponseUtil.write(response, RetCode.NOPERMISSION);
    }

}

