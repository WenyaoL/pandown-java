package com.pan.pandown.util.DTO.pandownAdminApi;

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
public class AddUserDetailDTO {

    private String username;

    private String roleName;

    private String email;

    private String password;

    private Long limitFlow;

    private int state;
}
