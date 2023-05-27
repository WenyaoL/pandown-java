package com.pan.pandown.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pan.pandown.dao.entity.PandownUser;
import com.pan.pandown.util.DTO.pandownUserApi.UserRegisterDTO;
import com.pan.pandown.util.PO.PandownUserDetailPO;
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

    /**
     * 用户注册
     * @param username
     * @param password
     * @param email
     * @param captcha
     * @return
     */
    RegisterCode userRegister(String username, String password, String email, String captcha);
    RegisterCode userRegister(UserRegisterDTO userRegisterDTO);

    /**
     * 用户登录
     * @param email
     * @param password
     * @return
     */
    String userLogin(String email, String password);

    /**
     * 用户登出
     * @param token
     * @return
     */
    boolean userLogout(String token);

    /**
     * 获取用户信息
     * @param token
     * @return
     */
    Map getUserInfo(String token);

    /**
     * 发送邮箱验证码
     * @param email
     * @return
     */
    String postCaptcha(String email);

    /**
     * 发送邮箱验证码(忘记密码接口)
     * @param email
     * @return
     */
    String postCaptchaByForgetPwd(String email);

    /**
     * 重置密码
     * @param password
     * @param email
     * @param captcha
     * @return
     */
    RegisterCode resetPassword(String password, String email, String captcha);


    IPage<PandownUserDetailPO> pageUserInfo(int pageNum, int pageSize);

}
