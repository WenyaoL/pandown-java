package com.pan.pandown.dao.DTO;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ListShareFileApiDTO {
    @NotBlank(message = "must has surl")
    private String surl;
    @NotBlank(message = "must has pwd")
    private String pwd;

    private String dir = "";

    private Integer page = 0;
}
