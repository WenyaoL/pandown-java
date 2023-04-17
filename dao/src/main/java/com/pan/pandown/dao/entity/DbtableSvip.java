package com.pan.pandown.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @author wenyao
 * @since 2023-04-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="DbtableSvip对象", description="")
public class DbtableSvip implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "账号名称")
    private String name;

    @ApiModelProperty(value = "会员bduss")
    private String svipBduss;

    @ApiModelProperty(value = "会员stoken")
    private String svipStoken;

    @ApiModelProperty(value = "会员账号加入时间")
    private LocalDateTime addTime;

    @ApiModelProperty(value = "会员状态(0:正常,-1:限速)")
    private Integer state;

    @ApiModelProperty(value = "是否正在使用(非零表示真)")
    private LocalDateTime isUsing;


}
