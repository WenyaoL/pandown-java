package com.pan.pandown.util.DTO.dbtableSvipApi;

import lombok.Data;

/**
 * @author yalier(wenyao)
 * @Description
 * @since 2023-05-15
 */
@Data
public class AddSvipDetailDTO {

    private Integer id;

    private String name;

    private String svipBduss;

    private String svipStoken;

    private Integer state;

    private String is_vip;

    private String is_svip;

    private String is_evip;

}
