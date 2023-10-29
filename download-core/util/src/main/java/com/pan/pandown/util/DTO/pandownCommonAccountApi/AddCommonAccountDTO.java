package com.pan.pandown.util.DTO.pandownCommonAccountApi;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class AddCommonAccountDTO {

    @NotNull
    @ApiModelProperty(value = "普通账号的cookies字符串")
    private String cookie;
}
