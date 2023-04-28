package com.pan.pandown.dao.DTO;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class DownloadApiDTO {

    private String surl;

    private String pwd;
}
