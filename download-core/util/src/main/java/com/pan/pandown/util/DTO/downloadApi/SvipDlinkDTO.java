package com.pan.pandown.util.DTO.downloadApi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yalier(wenyao)
 * @Description
 * @since 2023-05-26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SvipDlinkDTO {

    private Integer error;

    private String svipDlink;

}
