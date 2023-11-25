package com.pan.pandown.util.DTO.downloadApi;

import lombok.Data;

/**
 * @author yalier(wenyao)
 * @Description
 * @since 2023-05-29
 */
@Data
public class ListShareDirDTO {
    private String surl;

    private String pwd;

    private String dir = "";

    private Integer page = 0;
}
