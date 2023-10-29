package com.pan.pandown.util.DTO.pandownSvipAccountApi;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author yalier(wenyao)
 * @Description
 * @since 2023-05-15
 */
@Data
public class AddSvipAccountDTO {


    @NotBlank
    @ApiModelProperty(value = "会员bduss")
    private String svipBduss;

    @NotBlank
    @ApiModelProperty(value = "会员stoken")
    private String svipStoken;


}
