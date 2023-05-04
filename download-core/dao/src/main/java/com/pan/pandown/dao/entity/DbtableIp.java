package com.pan.pandown.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author yalier
 * @since 2023-04-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="DbtableIp对象", description="")
public class DbtableIp implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "ip地址")
    private String ip;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "白黑名单添加时间")
    private LocalDateTime addTime;

    @ApiModelProperty(value = "状态(0:允许,-1:禁止)")
    private Integer type;


}
