package com.pan.pandown.util.PO.pandownUserApi;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author yalier(wenyao)
 * @Description
 * @since 2023-05-29
 */
@Data
public class UserInfoPO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户Id")
    private Long id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "用户邮箱")
    private String email;

    @ApiModelProperty(value = "用户头像")
    private String avatar;

    @ApiModelProperty(value = "用户角色名")
    private String roleName;
}
