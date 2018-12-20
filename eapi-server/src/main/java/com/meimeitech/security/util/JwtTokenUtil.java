package com.meimeitech.security.util;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Sets;
import com.meimeitech.common.vo.UserSession;
import com.meimeitech.security.core.JwtUserDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -3301605591108950415L;

    private final Log logger = LogFactory.getLog(this.getClass());

    @Value("${jwt.secret:1A871EB8569E1B32CDC597F4AF72FE33}")
    private String secret;

    // 失效时间  7天
    private long expiration = 7 * 24 * 60 * 60 * 1000;


    /**
     * 生成 JWT Token
     *
     * @param session
     * @return
     */
    public String generate(UserSession session) {
        String token = Jwts.builder()
                .setIssuedAt(new Date())
                .setSubject(JSON.toJSONString(session))
                .setExpiration(new Date(new Date().getTime() + expiration)) // 失效时间
                .signWith(SignatureAlgorithm.HS512, secret) // 加密方式
                .compact();
        // 存redis
//        redisTemplate.opsForHash().put(Constants.KEY_PORTAL_TOKEN, token, session);
//        redisTemplate.expire(Constants.KEY_PORTAL_TOKEN, Constants.TIME_KEY_PORTAL_TOKEN, TimeUnit.MINUTES);
        return token;
    }

    public String generate() {

        JwtUserDetails userDetails = (JwtUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        Set<String> roleNames = Sets.newHashSet();

        userDetails.getAuthorities().forEach((authorities)-> {
            roleNames.add(authorities.getAuthority());
        }) ;

        // 返回jwt token
        UserSession session = new UserSession();
        session.setId(userDetails.getId());
        session.setRoleNames(roleNames);
        session.setMobile(userDetails.getMobile());
        session.setLoginName(userDetails.getUsername());
        session.setDisabled(!userDetails.isEnabled());

        String token = Jwts.builder()
                .setIssuedAt(new Date())
                .setSubject(JSON.toJSONString(session))
                .setExpiration(new Date(new Date().getTime() + expiration)) // 失效时间
                .signWith(SignatureAlgorithm.HS512, secret) // 加密方式
                .compact();
        // 存redis
//        redisTemplate.opsForHash().put(Constants.KEY_PORTAL_TOKEN, token, session);
//        redisTemplate.expire(Constants.KEY_PORTAL_TOKEN, Constants.TIME_KEY_PORTAL_TOKEN, TimeUnit.MINUTES);
        return token;
    }

    /**
     * 解析 JWT Token
     *
     * 先从redis中获取，如果没有则解析token, 存在redis中
     *
     * @param token
     * @return
     */
    public UserSession parser(String token) {

        UserSession session = new UserSession();

        if (StringUtils.isEmpty(token)) {
            return session;
        }

        // redis 中获取
//        if (redisTemplate.opsForHash().hasKey(Constants.KEY_HASH_TOKEN, token)) {
//            try {
//                BeanUtils.copyProperties(redisTemplate.opsForHash().get(Constants.KEY_HASH_TOKEN, token),session);
//            } catch (Exception e) {
//                logger.info("用户会话转换异常！");
//                e.printStackTrace();
//            }
//            return session;
//        }

        try {
            // 解析
            session = JSON.parseObject(Jwts.parser().setSigningKey(secret)
                    .parseClaimsJws(token).getBody().getSubject(), UserSession.class);

            // 存redis
//            redisTemplate.opsForHash().put(Constants.KEY_HASH_TOKEN, token, session);
//            redisTemplate.expire(Constants.KEY_HASH_TOKEN, Constants.TIME_KEY_HASH_TOKEN, TimeUnit.MINUTES);

        } catch (Exception e) {
            logger.info("JWT token 解析异常！");
        }

        return session;
    }

}