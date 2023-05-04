package com.pan.pandown.util.DTO.downloadApi;

import lombok.Data;

/**
 * @author yalier(wenyao)
 * @Description
 * @since 2023-05-04
 */
@Data
public class ListFileDTO {
    private String surl;

    private String pwd;

    private String dir = "";

    private Integer page = 0;
}
