package com.shawn.security.util;

import com.alibaba.fastjson.JSONObject;
import com.shawn.common.RetCode;
import com.shawn.common.vo.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseUtil {

    private static Logger logger = LoggerFactory.getLogger(ResponseUtil.class);

    public static void write(HttpServletResponse servletResponse, RetCode retcode) {
        Response response = new Response();
        response.getHeader().setCode(retcode.getCode());
        response.getHeader().setMessage(retcode.getMessage());
        try {
            servletResponse.setContentType("text/html;charset=utf-8");
            servletResponse.getWriter().write(JSONObject.toJSONString(response));
        } catch (IOException e) {
            logger.error("IOException:"+e.getMessage());
        }
    }

}
