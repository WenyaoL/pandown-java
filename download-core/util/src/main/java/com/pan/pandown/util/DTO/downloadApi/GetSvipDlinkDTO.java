package com.pan.pandown.util.DTO.downloadApi;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author yalier(wenyao)
 * @Description
 * @since 2023-05-22
 */
@Data
public class GetSvipDlinkDTO {
    private ShareFileDTO shareFile;

    private String sign;

    private Long timestamp;

    private String seckey;

    private String shareid;

    private String uk;
}
