package com.meimeitech.security.web.filter;

import com.google.common.collect.Lists;
import com.meimeitech.common.RetCode;
import com.meimeitech.common.util.UserContextHolder;
import com.meimeitech.common.vo.UserSession;
import com.meimeitech.security.core.JwtUserDetails;
import com.meimeitech.security.util.JwtTokenUtil;
import com.meimeitech.security.util.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.meimeitech.security.util.Constants.HEADER_STRING;
import static com.meimeitech.security.util.Constants.TOKEN_PREFIX;

/**
 * 解析 JWT Token
 * <p>
 * 认证用户，并设置 security context
 * <p>
 * Created by Shawn on 2017/8/15.
 */
public class AuthenticationTokenFilter extends OncePerRequestFilter {

    private final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.header:access_token}")
    private String tokenName;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        String xToken = getToken(request);

        UserSession session = jwtTokenUtil.parser(xToken);

        if (session.getLoginName() != null
                && SecurityContextHolder.getContext().getAuthentication() == null) { // 解析成功

            if(session.isDisabled()){
                ResponseUtil.write(response, RetCode.USERLOCKED);
                return;
            }

            UsernamePasswordAuthenticationToken authentication = getAuthentication(request, session);
            // 认证用户，并设置 security context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserContextHolder.setContext(session);

            logger.debug("认证用户：" + session.getLoginName() + ", 设置 security context。");
        }

        chain.doFilter(request, response);
    }


    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request,  UserSession session) {
        // session -> userDetails
        List<GrantedAuthority> authorities = Lists.newArrayList();

        List<GrantedAuthority> userAuthorities = Lists.newArrayList();
        if (session.getRoleNames() != null) {
            session.getRoleNames().forEach((roleName) -> {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + roleName));
                userAuthorities.add(new SimpleGrantedAuthority(roleName));
            });
        }

        JwtUserDetails userDetails = new JwtUserDetails(session.getId(), session.getLoginName(),session.getMobile(), null,
                userAuthorities, session.isDisabled());
        userDetails.setMobile(session.getMobile());

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        return authentication;
    }


    private String getToken(HttpServletRequest request){

        // 优先Header中获取
        String xToken = request.getHeader(HEADER_STRING);

        // 然后Param中获取
        if (StringUtils.isEmpty(xToken)){
            // Parameter中取
            xToken = request.getParameter(this.tokenName);
        }else {
            xToken = xToken.replace(TOKEN_PREFIX, "");
        }

        logger.debug("JWT Token : " + xToken);

        return xToken;
    }

}