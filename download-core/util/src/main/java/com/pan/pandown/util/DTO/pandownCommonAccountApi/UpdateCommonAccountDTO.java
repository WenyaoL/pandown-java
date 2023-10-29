package com.pan.pandown.util.DTO.pandownCommonAccountApi;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Data
public class UpdateCommonAccountDTO {

    @ApiModelProperty(value = "id")
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty(value = "普通账号的用户名")
    @NotBlank
    private String name;

    @NotBlank
    @ApiModelProperty(value = "普通账号的cookies字符串")
    private String cookie;
}
