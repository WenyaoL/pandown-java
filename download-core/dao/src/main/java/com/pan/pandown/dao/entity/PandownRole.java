package com.pan.pandown.dao.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
@ApiModel(value="PandownRole对象", description="")
public class PandownRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String roleName;

    private String description;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String status;


}
