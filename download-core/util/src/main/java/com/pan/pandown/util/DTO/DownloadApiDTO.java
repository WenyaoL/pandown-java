package com.pan.pandown.util.DTO;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;

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

    private String sign;

    private Long timestamp;

    private String sekey;

    private ArrayList fsIdList;

    private String primaryid;

    private String uk;
}
