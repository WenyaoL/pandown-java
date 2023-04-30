package com.pan.pandown.web.controller;


import com.pan.pandown.util.DTO.DownloadApiDTO;
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

/**
 * @author yalier(wenyao)
 * @since 2023/4/28
 * @Description
 */
@RestController
@RequestMapping("/api")
public class DownloadApiController {

    @Autowired
    private DownloadService downloadService;

    @PostMapping("/list_all_files")
    @ApiOperation(value = "分享链接解析接口",notes = "分享链接解析(返回分享数据)",httpMethod = "POST")
    public BaseResponse listAllFiles(@RequestBody @Valid DownloadApiDTO downloadApiDTO){
        return new SuccessResponse(downloadService.listShare(downloadApiDTO.getSurl(),downloadApiDTO.getPwd()));
    }

    @PostMapping("/list_dir")
    @ApiOperation(value = "分享目录解析接口",notes = "分享目录解析(列举指定目录)",httpMethod = "POST")
    public BaseResponse listShareDir(@RequestBody @Valid DownloadApiDTO downloadApiDTO){
        Map map = downloadService.listDir(
                downloadApiDTO.getSurl(),
                downloadApiDTO.getPwd(),
                downloadApiDTO.getDir(),
                downloadApiDTO.getPage()
        );
        if(Objects.isNull(map)) return new FailResponse("获取失败");

        return new SuccessResponse(map);
    }

    @PostMapping("/getSignAndTime")
    @ApiOperation(value = "时间搓和签名请求接口",notes = "获取sign和Time",httpMethod = "POST")
    public BaseResponse getSignAndTime(@RequestBody @Valid DownloadApiDTO downloadApiDTO){
        Map map = downloadService.getSignAndTime(downloadApiDTO.getSurl());
        if(Objects.isNull(map)) return new FailResponse("获取失败");
        return new SuccessResponse(map);
    }

    @PostMapping("/getSvipdLink")
    @ApiOperation(value = "直链请求接口",notes = "获取直链",httpMethod = "POST")
    public BaseResponse getSvipdLink(@RequestBody @Valid DownloadApiDTO downloadApiDTO){
        Object dlink = downloadService.getDlink(downloadApiDTO);
        return new SuccessResponse(dlink);
    }

}
