package com.pan.pandown.web.controller;


import com.pan.pandown.dao.entity.PandownUser;
import com.pan.pandown.service.IPandownUserService;
import com.pan.pandown.util.DTO.pandownUserApi.UserLoginDTO;
import com.pan.pandown.util.DTO.pandownUserApi.UserRegisterDTO;
import com.pan.pandown.util.PO.pandownUserApi.UserInfoPO;
import com.pan.pandown.util.baseResp.BaseResponse;
import com.pan.pandown.util.baseResp.FailResponse;
import com.pan.pandown.util.baseResp.SuccessResponse;
import com.pan.pandown.util.constants.RegisterCode;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 *  用户控制接口
 * </p>
 *
 * @author wenyao(yalier)
 * @since 2023-04-20
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class PandownUserController {

    @Autowired
    private IPandownUserService pandownUserService;

    @Autowired(required = false)
    private HttpServletRequest httpServletRequest;

    @PostMapping("/register")
    @ApiOperation(value = "用户注册接口",notes = "用户注册",httpMethod = "POST")
    public BaseResponse userRegister(@Valid @RequestBody UserRegisterDTO userRegisterDTO){
        RegisterCode registerCode = pandownUserService.userRegister(userRegisterDTO);
        if(registerCode.getCode().equals("200"))return new SuccessResponse(registerCode.getName());
        else return new FailResponse(registerCode.getName());
    }

    @PostMapping("/login")
    @ApiOperation(value = "用户登录接口",notes = "用户登录",httpMethod = "POST")
    public BaseResponse userLogin(@Valid @RequestBody UserLoginDTO userLoginDTO){
        String token = pandownUserService.userLogin(userLoginDTO.getEmail(), userLoginDTO.getPassword());
        return new SuccessResponse(new HashMap<String,String>(){{
            put("token",token);
        }});
    }

    @PostMapping("/logout")
    @ApiOperation(value = "用户登出接口",notes = "用户登出登录",httpMethod = "POST")
    public BaseResponse userLogout(){
        String token = httpServletRequest.getHeader("token");
        if(StringUtils.isBlank(token)) return new FailResponse("登录token不存在");
        boolean b = pandownUserService.userLogout(token);
        if (!b) return new FailResponse("登出失败");
        return new SuccessResponse();
    }


    @PostMapping("/info")
    @ApiOperation(value = "用户信息接口",notes = "获取用户信息",httpMethod = "POST")
    public BaseResponse userInfo(){
        String token = httpServletRequest.getHeader("token");
        if(StringUtils.isBlank(token)) return new FailResponse("token不存在");
        UserInfoPO userInfo = pandownUserService.getUserInfo(token);
        return new SuccessResponse(userInfo);
    }

    @PostMapping("/postCaptcha")
    @ApiOperation(value = "用户注册邮箱验证码接口",notes = "发送邮箱验证码信息",httpMethod = "POST")
    public BaseResponse postCaptcha(@RequestBody Map<String,String> map){
        if(StringUtils.isBlank(map.get("email"))) return new FailResponse("缺少发送email");
        String captcha = pandownUserService.postCaptcha(map.get("email"));
        if (Objects.nonNull(captcha)) return new SuccessResponse();
        return new FailResponse("邮件发送失败");
    }

    @PostMapping("/postCaptchaByForgetPwd")
    @ApiOperation(value = "用户邮箱验证码接口",notes = "发送邮箱验证码信息(用于忘记密码)",httpMethod = "POST")
    public BaseResponse postCaptchaByForgetPwd(@RequestBody Map<String,String> map){
        if(map.get("email") == null) return new FailResponse("缺少发送email");
        String captcha = pandownUserService.postCaptchaByForgetPwd(map.get("email"));
        if (Objects.nonNull(captcha)) return new SuccessResponse();
        return new FailResponse("邮件发送失败");
    }

    @PostMapping("/resetPassword")
    @ApiOperation(value = "用户邮箱验证码接口",notes = "发送邮箱验证码信息",httpMethod = "POST")
    public BaseResponse resetPassword(@Valid @RequestBody UserRegisterDTO userRegisterDTO){
        RegisterCode registerCode = pandownUserService.resetPassword(
                userRegisterDTO.getPassword(),
                userRegisterDTO.getEmail(),
                userRegisterDTO.getCaptcha()
        );
        if(registerCode.getCode().equals("200"))return new SuccessResponse(registerCode.getName());
        else return new FailResponse(registerCode.getName());
    }
}
