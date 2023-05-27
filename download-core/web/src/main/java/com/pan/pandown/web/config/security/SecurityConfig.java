package com.pan.pandown.web.config.security;


import com.pan.pandown.dao.entity.PandownPermission;
import com.pan.pandown.service.IPandownPermissionService;
import com.pan.pandown.web.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;


/**
 * @author yalier
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private IPandownPermissionService pandownPermissionService;

    //授权
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);


        //以下接口不需要认证
        http.authorizeRequests()
                .antMatchers(
                        "/user/login",
                        "/user/register",
                        "/user/postCaptcha",
                        "/user/postCaptchaByForgetPwd",
                        "/user/resetPassword"
                        ).anonymous();

        //配置访问权限控制
        List<PandownPermission> list = pandownPermissionService.list();
        for (int i = 0; i < list.size(); i++) {
            PandownPermission pandownPermission = list.get(i);
            String code = pandownPermission.getCode();
            String url = pandownPermission.getUrl();
            http.authorizeRequests().antMatchers(url).hasAnyAuthority(code);

        }

        //其他所有接口都需要认证
        http.authorizeRequests().anyRequest().authenticated();

        //关闭csrf
        http.csrf().disable();


        //rememberMe
        http.rememberMe().rememberMeParameter("rememberme");
    }




    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}

