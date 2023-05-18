package com.pan.pandown.dao.entity;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author yalier
 * @since 2023-05-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="PandownRolePermission对象", description="")
public class PandownRolePermission implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer roleId;

    private Integer permissionId;


}
