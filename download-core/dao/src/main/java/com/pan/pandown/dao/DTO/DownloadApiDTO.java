package com.pan.pandown.dao.DTO;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author yalier(wenyao)
 */
@Data
public class DownloadApiDTO {
    @NotBlank(message = "must has surl")
    private String surl;

    private String pwd;

    private String dir = "";

    private Integer page = 0;
}
