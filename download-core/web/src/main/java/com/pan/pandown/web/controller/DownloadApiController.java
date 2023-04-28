package com.pan.pandown.web.controller;


import com.pan.pandown.dao.DTO.ListShareFileApiDTO;
import com.pan.pandown.service.download.DownloadService;
import com.pan.pandown.util.baseResp.BaseResponse;
import com.pan.pandown.util.baseResp.FailResponse;
import com.pan.pandown.util.baseResp.SuccessResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class DownloadApiController {

    @Autowired
    private DownloadService downloadService;

    @PostMapping("/list_all_files")
    @ApiOperation(value = "分享链接解析接口",notes = "分享链接解析(返回分享数据)",httpMethod = "POST")
    public BaseResponse listAllFiles(@RequestBody @Valid ListShareFileApiDTO listShareFileApiDTO){
        return new SuccessResponse(downloadService.listShare(listShareFileApiDTO.getSurl(),listShareFileApiDTO.getPwd()));
    }

    @PostMapping("list_dir")
    @ApiOperation(value = "分享目录解析接口",notes = "分享目录解析(列举指定目录)",httpMethod = "POST")
    public BaseResponse list_share_dir(@RequestBody @Valid ListShareFileApiDTO listShareFileApiDTO){
        Map map = downloadService.listDir(
                listShareFileApiDTO.getSurl(),
                listShareFileApiDTO.getPwd(),
                listShareFileApiDTO.getDir(),
                listShareFileApiDTO.getPage()
        );
        if(Objects.isNull(map)) return new FailResponse("获取失败");

        return new SuccessResponse(map);
    }



}
