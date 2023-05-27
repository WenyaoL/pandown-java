package com.pan.pandown.util.DTO.downloadApi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yalier(wenyao)
 * @Description
 * @since 2023-05-04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetDlinkDTO {


    private String sign;

    private Long timestamp;

    private String seckey;

    private ArrayList fsIdList;

    private String shareid;

    private String uk;
}
