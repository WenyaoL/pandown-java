package com.pan.pandown.util.DTO.PandownUserApi;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author yalier(wenyao)
 * @since 2023/4/28
 * @Description
 */
@Data
public class UserLoginDTO {

    @NotBlank(message = "邮箱不能为空")
    private String email;

    @NotBlank(message = "密码不能为空")
    private String password;
}
