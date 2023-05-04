package com.pan.pandown.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
@ApiModel(value="Dbtable对象", description="")
public class Dbtable implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户ip")
    private String userip;

    @ApiModelProperty(value = "文件名")
    private String filename;

    @ApiModelProperty(value = "文件大小")
    private String size;

    @ApiModelProperty(value = "文件效验码")
    private String md5;

    @ApiModelProperty(value = "文件路径")
    private String path;

    @ApiModelProperty(value = "文件创建时间")
    private String serverCtime;

    @ApiModelProperty(value = "文件下载地址")
    @TableField("realLink")
    private String reallink;

    @ApiModelProperty(value = "解析时间")
    private LocalDateTime ptime;

    @ApiModelProperty(value = "解析账号id")
    private Integer paccount;


}
