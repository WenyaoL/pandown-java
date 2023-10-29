package com.pan.pandown.util.DTO.pandownSvipAccountApi;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Data
public class UpdateSvipAccountDTO {

    @NotNull
    @ApiModelProperty(value = "id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @NotBlank
    @ApiModelProperty(value = "会员bduss")
    private String svipBduss;

    @NotBlank
    @ApiModelProperty(value = "会员stoken")
    private String svipStoken;

}
