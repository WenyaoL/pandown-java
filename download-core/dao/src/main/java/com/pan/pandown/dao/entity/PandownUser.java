package com.pan.pandown.dao.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author yalier
 * @since 2023-04-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="PandownUser对象", description="")
@AllArgsConstructor
@NoArgsConstructor
public class PandownUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "用户密码")
    private String password;


}
