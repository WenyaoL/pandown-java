package com.pan.pandown.util.DTO;

import lombok.Data;

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

    private String fs_id;

    private String shareid;

    private String uk;
}
