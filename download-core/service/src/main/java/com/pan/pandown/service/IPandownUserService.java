package com.pan.pandown.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pan.pandown.dao.entity.PandownUser;
import com.pan.pandown.util.constants.RegisterCode;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yalier(wenyao)
 * @since 2023-04-20
 */
public interface IPandownUserService extends IService<PandownUser>, UserDetailsService {

    RegisterCode userRegister(String username, String password, String email, String captcha);

    String userLogin(String email, String password);

    Map getUserInfo(String token);

    String postCaptcha(String email);

    String postCaptchaByForgetPwd(String email);

    RegisterCode resetPassword(String password, String email, String captcha);
}
