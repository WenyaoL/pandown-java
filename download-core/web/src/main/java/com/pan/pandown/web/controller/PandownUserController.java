package com.pan.pandown.web.controller;


import com.pan.pandown.dao.DTO.UserLoginDTO;
import com.pan.pandown.dao.DTO.UserRegisterDTO;
import com.pan.pandown.service.IPandownUserService;
import com.pan.pandown.util.baseResp.BaseResponse;
import com.pan.pandown.util.baseResp.SuccessResponse;
import com.pan.pandown.util.constants.RegisterCode;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  用户控制接口
 * </p>
 *
 * @author wenyao
 * @since 2023-04-20
 */
@RestController
@RequestMapping("/user")
public class PandownUserController {

    @Autowired
    private IPandownUserService pandownUserService;

    @PostMapping("/register")
    @ApiOperation(value = "用户注册接口",notes = "用户注册",httpMethod = "POST")
    public BaseResponse userRegister(@Valid @RequestBody UserRegisterDTO userRegisterDTO){
        RegisterCode registerCode = pandownUserService.userRegister(userRegisterDTO.getUsername(), userRegisterDTO.getPassword());
        return new SuccessResponse(registerCode.getName());
    }

    @PostMapping("/login")
    @ApiOperation(value = "用户登录接口",notes = "用户登录",httpMethod = "POST")
    public BaseResponse userLogin(@Valid @RequestBody UserLoginDTO userLoginDTO){
        String token = pandownUserService.userLogin(userLoginDTO.getUsername(), userLoginDTO.getPassword());
        return new SuccessResponse(new HashMap<String,String>(){{
            put("token",token);
        }});
    }

    @PostMapping("/info")
    @ApiOperation(value = "用户信息接口",notes = "获取用户信息",httpMethod = "POST")
    public BaseResponse userInfo(){
        return new SuccessResponse();
    }
}
