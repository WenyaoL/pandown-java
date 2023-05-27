package com.pan.pandown.util.DTO.pandownAdminApi;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yalier(wenyao)
 * @Description
 * @since 2023-05-25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDetailDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    private String username;

    private String roleName;

    private Long limitFlow;

    private int state;
}
