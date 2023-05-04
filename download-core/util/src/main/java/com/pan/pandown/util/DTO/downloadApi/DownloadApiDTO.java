package com.pan.pandown.util.DTO.downloadApi;

import lombok.Data;

import java.util.ArrayList;

/**
 * @author yalier(wenyao)
 */
@Data
public class DownloadApiDTO {
    // @NotBlank(message = "must has surl")
    private String surl;

    private String pwd;

    private String dir = "";

    private Integer page = 0;

    private String sign;

    private Long timestamp;

    private String seckey;

    private ArrayList fsIdList;

    private String shareid;

    private String uk;
}
