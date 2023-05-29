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
 * @since 2023-05-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="PandownUserFlow对象", description="")
public class PandownUserFlow implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
    private Long id;

    @ApiModelProperty(value = "解析数")
    private Integer parseNum;

    @ApiModelProperty(value = "解析流量")
    private Long parseFlow;

    @ApiModelProperty(value = "限额流量(剩余流量)")
    private Long limitFlow;

    @ApiModelProperty(value = "状态")
    private Integer state;


}
