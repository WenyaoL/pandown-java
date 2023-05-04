package com.pan.pandown.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
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
    //@TableField(value = "add_time",fill = FieldFill.INSERT)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime addTime;

    @ApiModelProperty(value = "会员状态(0:正常,-1:限速)")
    private Integer state;

    @ApiModelProperty(value = "是否正在使用(非零表示真)")
    //@TableField(value = "is_using",fill = FieldFill.UPDATE)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime isUsing;


}
