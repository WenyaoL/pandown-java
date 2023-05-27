package com.pan.pandown.util.DTO.pandownUserApi;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author yalier(wenyao)
 * @since 2023/4/28
 * @Description 
 */
@Data
public class UserRegisterDTO {

    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "邮箱不能为空")
    private String email;

    @NotBlank(message = "邮箱验证码不能为空")
    private String captcha;
}
