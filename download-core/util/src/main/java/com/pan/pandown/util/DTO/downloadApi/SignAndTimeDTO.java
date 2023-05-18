package com.pan.pandown.util.DTO.downloadApi;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yalier(wenyao)
 * @Description
 * @since 2023-05-14
 */
@Data
public class SignAndTimeDTO {

    @ApiModelProperty(value = "签名")
    private String sign;

    @ApiModelProperty(value = "时间搓")
    private Long timestamp;
}
