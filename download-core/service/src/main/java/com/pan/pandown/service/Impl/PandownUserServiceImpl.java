package com.pan.pandown.service.Impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pan.pandown.dao.entity.PandownUser;
import com.pan.pandown.dao.entity.PandownUserRole;
import com.pan.pandown.dao.mapper.PandownUserMapper;
import com.pan.pandown.dao.model.LoginUser;
import com.pan.pandown.service.IPandownUserFlowService;
import com.pan.pandown.service.IPandownUserRoleService;
import com.pan.pandown.service.IPandownUserService;
import com.pan.pandown.service.login.EmailService;
import com.pan.pandown.service.login.TokenService;
import com.pan.pandown.util.DTO.SpringGrantedAuthority;
import com.pan.pandown.util.DTO.pandownUserApi.UserRegisterDTO;
import com.pan.pandown.util.PO.PandownUserDetailPO;
import com.pan.pandown.util.PO.pandownUserApi.UserInfoPO;
import com.pan.pandown.util.constants.RegisterCode;
import com.pan.pandown.util.mybatisPlus.SnowflakeGenerator;
import com.pan.pandown.util.redis.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Resource
    private PandownUserMapper pandownUserMapper;

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

    @Autowired
    private IPandownUserRoleService pandownUserRoleService;

    @Autowired
    private IPandownUserFlowService pandownUserFlowService;

    public List<GrantedAuthority> createAuthorityList(String... authorities) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>(authorities.length);
        for (String authority : authorities) {
            grantedAuthorities.add(new SpringGrantedAuthority(authority));
        }
        return grantedAuthorities;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        PandownUser pandownUser = query().eq("email",email).one();
        if (Objects.isNull(pandownUser)){
            throw new UsernameNotFoundException("未知用户");
        }else {
            List<String> permissions = pandownUserMapper.getPermissionsByUserId(pandownUser.getId());
            String[] permissionsArr = permissions.toArray(new String[permissions.size()]);
            List<GrantedAuthority> authorityList = this.createAuthorityList(permissionsArr);

            return new LoginUser(pandownUser,authorityList);
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
    @Transactional
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
        //雪花id
        long nextId = snowflakeGenerator.nextId();

        //验证码不相同
        if(!registerCaptcha.toString().equals(captcha)) return RegisterCode.FAIL_CAPTCHA_ERROR;

        //保存用户信息
        save(new PandownUser(nextId,username,bCryptPasswordEncoder.encode(password),email));

        //初始化普通用户权限
        pandownUserRoleService.initCommonUserRole(nextId);

        //初始化用户流量表
        pandownUserFlowService.initUserFlow(nextId);

        //删除验证码
        redisService.hdel("user_register:captcha",email);

        return RegisterCode.SUCCESS;
    }

    @Override
    public RegisterCode userRegister(UserRegisterDTO userRegisterDTO){
        RegisterCode registerCode = userRegister(
                userRegisterDTO.getUsername(),
                userRegisterDTO.getPassword(),
                userRegisterDTO.getEmail(),
                userRegisterDTO.getCaptcha());
        return registerCode;
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
    public boolean userLogout(String token) {
        boolean b = tokenService.expireToken(token);
        return b;
    }

    @Override
    public UserInfoPO getUserInfo(String token) {
        LoginUser loginUser = tokenService.verifyToken(token);
        Long id = loginUser.getPandownUser().getId();
        UserInfoPO userInfoPO = baseMapper.getUserInfoById(id);
        return userInfoPO;
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

    @Override
    public IPage<PandownUserDetailPO> pageUserInfo(int pageNum,int pageSize) {
        Page<PandownUserDetailPO> pandownUserDetailPOPage = new Page<>(pageNum,pageSize);
        Page<PandownUserDetailPO> page = baseMapper.pageListUserInfo(pandownUserDetailPOPage);
        return page;
    }
}
