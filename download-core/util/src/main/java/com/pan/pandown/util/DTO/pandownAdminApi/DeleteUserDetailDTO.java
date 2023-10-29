package com.pan.pandown.util.DTO.pandownAdminApi;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author yalier
 * @Description
 * @since 2023-10-28
 */
@Data
public class DeleteUserDetailDTO {

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

}
