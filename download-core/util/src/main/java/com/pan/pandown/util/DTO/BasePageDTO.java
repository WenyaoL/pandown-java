package com.pan.pandown.util.DTO;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author yalier(wenyao)
 * @Description
 * @since 2023-05-28
 */
@Data
public class BasePageDTO {

    @NotNull(message = "页码不可为空")
    private Long pageNum;

    @NotNull(message = "页大小不可为空")
    private Long pageSize;
}
