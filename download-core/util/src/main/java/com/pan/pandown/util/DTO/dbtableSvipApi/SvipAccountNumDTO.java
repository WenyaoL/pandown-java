package com.pan.pandown.util.DTO.dbtableSvipApi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yalier(wenyao)
 * @Description
 * @since 2023-05-29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SvipAccountNumDTO {
    private Integer accountNum;
    private Long availableAccountNum;
}
