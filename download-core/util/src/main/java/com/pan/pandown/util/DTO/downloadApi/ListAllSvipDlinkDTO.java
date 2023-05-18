package com.pan.pandown.util.DTO.downloadApi;

import lombok.Data;

import java.util.List;

/**
 * @author yalier(wenyao)
 * @Description
 * @since 2023-05-14
 */
@Data
public class ListAllSvipDlinkDTO {

    private String surl;

    private String pwd;

    private String uk;

    private String shareid;

    private String seckey;

    private List<ShareFileDTO> shareFileList;
}
