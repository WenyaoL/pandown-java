package com.pan.pandown.util.DTO.downloadApi;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yalier(wenyao)
 * @Description
 * @since 2023-05-04
 */
@Data
public class GetDlinkDTO {

    private List dlinkList;

    private String sign;

    private Long timestamp;

    private String seckey;

    private ArrayList fsIdList;

    private String shareid;

    private String uk;
}
