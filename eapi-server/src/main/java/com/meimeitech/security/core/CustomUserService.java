package com.meimeitech.security.core;

import com.google.common.collect.Lists;
import com.meimeitech.sys.entity.UserAuth;
import com.meimeitech.sys.service.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 *
 * Created by Shawn on 2017/8/15.
 */
@Component("userDetailsService")
public class CustomUserService implements UserDetailsService {

    /**
     *
     */
    @Autowired
    private UserAuthService userAuthService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        UserAuth userAuth = userAuthService.findByAuthId(userName);

        if( userAuth == null ){
            throw new UsernameNotFoundException(String.format("用户：%s ，不存在", userName));
        }

        List<GrantedAuthority> authorities = Lists.newArrayList( new SimpleGrantedAuthority("USER"));

        return new JwtUserDetails(Long.parseLong(userAuth.getUser().getId().toString()), userAuth.getAuthId(),userAuth.getUser().getPhone(), userAuth.getAuthPass(),
                authorities, !userAuth.getUser().getStatus().equals("-1"));
    }
}
