package com.meimeitech.security;

import com.meimeitech.security.crypto.MD5PasswordEncoder;
import com.meimeitech.security.web.UnAuthenticationEntryPoint;
import com.meimeitech.security.web.access.AccessAuthenticationEntryPoint;
import com.meimeitech.security.web.filter.AuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.Header;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.Filter;
import java.util.Arrays;


@Configuration
@EnableWebSecurity
public class SecurityConfig  extends WebSecurityConfigurerAdapter {



    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UnAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private AccessAuthenticationEntryPoint accessAuthenticationEntryPoint;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers("/login", "/user/register", "/user/email/**", "/project/list", "/v2/api-docs/**").permitAll()
                // 登陆拦截
                .anyRequest().authenticated()
                // 认证拦截，response返回
                .and().exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
                // 权限拦截，response返回
                .and().exceptionHandling().accessDeniedHandler(accessAuthenticationEntryPoint)
                // 不需要创建session
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                // 退出登陆成功 Handler
//                .and().logout().logoutSuccessHandler(myLogoutSuccessHandler)

//                .and().rememberMe().rememberMeServices(rememberMeServicesBean()).key(REMEMBER_KEY);


        // 跨域拦截
        // custom-filter顺序详见：
        //  https://docs.spring.io/spring-security/site/docs/4.2.4.BUILD-SNAPSHOT/reference/htmlsingle/#ns-custom-filters
        http.addFilterBefore(corsFilter(), ChannelProcessingFilter.class);

        // 自定义JWT认证拦截器
        http.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

        // 自定义资源角色拦截
//        http.addFilterBefore(filterSecurityInterceptor(), FilterSecurityInterceptor.class);

        // disable page caching
        http.headers().cacheControl();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new MD5PasswordEncoder()); // 数据库验证用户
        auth.authenticationProvider(authenticationProvider()); // MD5加密
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(new MD5PasswordEncoder());

        return authenticationProvider;
    }

    @Bean
    public AuthenticationTokenFilter authenticationTokenFilterBean() {
        return new AuthenticationTokenFilter();
    }

    @Bean
    public Filter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
