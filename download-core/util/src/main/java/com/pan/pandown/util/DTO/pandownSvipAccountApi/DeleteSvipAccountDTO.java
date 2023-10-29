package com.pan.pandown.util.DTO.pandownSvipAccountApi;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class DeleteSvipAccountDTO {

    @ApiModelProperty(value = "id")
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
}
