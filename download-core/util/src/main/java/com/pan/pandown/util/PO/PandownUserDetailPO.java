package com.pan.pandown.util.PO;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author yalier(wenyao)
 * @Description
 * @since 2023-05-24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PandownUserDetailPO implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "用户邮箱")
    private String email;

    @ApiModelProperty(value = "用户头像")
    private String avatar;

    @ApiModelProperty(value = "解析数")
    private Integer parseNum;

    @ApiModelProperty(value = "解析流量")
    private Long parseFlow;

    @ApiModelProperty(value = "流量限制")
    private Long limitFlow;

    @ApiModelProperty(value = "状态")
    private Integer state;

    @ApiModelProperty(value = "角色名")
    private String roleName;
}
