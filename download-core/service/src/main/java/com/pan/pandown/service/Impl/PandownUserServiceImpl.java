package com.pan.pandown.service.Impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pan.pandown.dao.entity.PandownUser;
import com.pan.pandown.dao.mapper.PandownUserMapper;
import com.pan.pandown.dao.model.LoginUser;
import com.pan.pandown.service.IPandownUserService;
import com.pan.pandown.service.login.EmailService;
import com.pan.pandown.service.login.TokenService;
import com.pan.pandown.util.constants.RegisterCode;
import com.pan.pandown.util.mybatisPlus.SnowflakeGenerator;
import com.pan.pandown.util.redis.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yalier(wenyao)
 * @since 2023-04-20
 */
@Service
public class PandownUserServiceImpl extends ServiceImpl<PandownUserMapper, PandownUser> implements IPandownUserService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private SnowflakeGenerator snowflakeGenerator;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Resource
    private EmailService emailService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        PandownUser pandownUser = query().eq("email",email).one();
        if (Objects.isNull(pandownUser)){
            throw new UsernameNotFoundException("未知用户");
        }else {
            List<GrantedAuthority> userAuthorityList = AuthorityUtils.createAuthorityList("USER");
            return new LoginUser(pandownUser);
        }
    }

    /**
     * 用户注册服务
     * @param username
     * @param password
     * @param email
     * @param captcha
     * @return
     */
    @Override
    public RegisterCode userRegister(String username, String password, String email, String captcha) {
        Integer count = query().eq("email", email).count();
        if (count>0){
            return RegisterCode.FAIL_EMAIL_REPEAT;
        }

        //获取验证码
        Object registerCaptcha = redisService.hget("user_register:captcha",email);
        //查看验证码是否存在
        if (Objects.isNull(registerCaptcha)) return RegisterCode.FAIL;
        //如果用户名为空
        if (StringUtils.isBlank(username)) username = email;
        //验证码相同并保存
        if(registerCaptcha.toString().equals(captcha)
                && save(new PandownUser(snowflakeGenerator.nextId(),username,bCryptPasswordEncoder.encode(password),email))){
            redisService.hdel("user_register:captcha",email);
            return RegisterCode.SUCCESS;
        }

        return RegisterCode.FAIL;
    }

    @Override
    public String userLogin(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("认证失败");
        }
        LoginUser principal = (LoginUser) authenticate.getPrincipal();
        String token = tokenService.createToken(principal);
        return token;
    }

    @Override
    public Map getUserInfo(String token) {
        LoginUser loginUser = tokenService.verifyToken(token);
        Long id = loginUser.getPandownUser().getId();
        return this.getBaseMapper().getUserInfoById(id);
    }

    @Override
    public String postCaptcha(String email) {
        //查询邮箱是否需要等待
        boolean flag = redisService.hHasKey("user_register:captcha_wait",email);
        if(flag) return null;

        try {
            //发送验证码
            String captcha = emailService.captchaEmail(email);
            if (Integer.parseInt(captcha) < 0) return null;
            //设置验证码等待(用于防止验证码连续发送)
            redisService.hset("user_register:captcha_wait",email,1,30L);
            //设置验证码
            redisService.hset("user_register:captcha",email,captcha,180L);
            return captcha;
        } catch (MessagingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String postCaptchaByForgetPwd(String email) {
        //查询邮箱是否需要等待
        boolean flag = redisService.hHasKey("user_forgetpwd:captcha_wait",email);
        if(flag) return null;

        try {
            //发送验证码
            String captcha = emailService.captchaEmail(email);
            if (Integer.parseInt(captcha) < 0) return null;
            //设置验证码等待(用于防止验证码连续发送)
            redisService.hset("user_forgetpwd:captcha_wait",email,1,30L);
            //设置验证码
            redisService.hset("user_forgetpwd:captcha",email,captcha,180L);
            return captcha;
        } catch (MessagingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public RegisterCode resetPassword(String password, String email, String captcha){

        //获取验证码
        Object registerCaptcha = redisService.hget("user_forgetpwd:captcha",email);
        //查看验证码是否存在
        if (Objects.isNull(registerCaptcha)) return RegisterCode.FAIL;

        //验证码相同并保存
        if(registerCaptcha.toString().equals(captcha)
            && update(new UpdateWrapper<PandownUser>().set("password", bCryptPasswordEncoder.encode(password)).eq("email", email))){
            redisService.hdel("user_forgetpwd:captcha",email);
            return RegisterCode.SUCCESS;
        }

        return RegisterCode.FAIL;
    }
}
