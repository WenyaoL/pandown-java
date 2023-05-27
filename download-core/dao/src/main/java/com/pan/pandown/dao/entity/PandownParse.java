package com.pan.pandown.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since 2023-05-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="PandownParse对象", description="")
public class PandownParse implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户ip")
    private Long userId;

    @ApiModelProperty(value = "文件名")
    private String filename;

    @ApiModelProperty(value = "文件大小")
    private Long size;

    @ApiModelProperty(value = "文件效验码")
    private String md5;

    @ApiModelProperty(value = "文件路径")
    private String path;

    @ApiModelProperty(value = "文件创建时间")
    private String serverCtime;

    @ApiModelProperty(value = "文件下载地址")
    @TableField("realLink")
    private String reallink;

    @ApiModelProperty(value = "解析账号id")
    private Long parseAccountId;

    @ApiModelProperty(value = "解析时间")
    private LocalDateTime createTime;


}
