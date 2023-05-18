package com.pan.pandown.dao.entity;

import java.time.LocalDateTime;
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
@ApiModel(value="PandownUserRole对象", description="")
public class PandownUserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userId;

    private String roleId;

    private LocalDateTime createTime;

    private String creator;


}
