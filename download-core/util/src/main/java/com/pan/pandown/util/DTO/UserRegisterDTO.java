package com.pan.pandown.util.DTO;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author yalier(wenyao)
 * @since 2023/4/28
 * @Description 
 */
@Data
public class UserRegisterDTO {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;
}
