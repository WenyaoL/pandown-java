package com.pan.pandown.util.DTO.downloadApi;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yalier(wenyao)
 * @Description
 * @since 2023-05-14
 */
@Data
public class ShareFileDTO {
    @ApiModelProperty(value = "文件id")
    private String fs_id;

    @ApiModelProperty(value = "是否为文件夹")
    private int isdir;

    @ApiModelProperty(value = "md5")
    private String md5;

    @ApiModelProperty(value = "文件路径")
    private String path;

    @ApiModelProperty(value = "文件大小")
    private Long size;

    @ApiModelProperty(value = "文件名")
    private String server_filename;

    @ApiModelProperty(value = "文件普通下载链接")
    private String dlink;

    @ApiModelProperty(value = "文件svip下载链接")
    private String svipDlink;
}
