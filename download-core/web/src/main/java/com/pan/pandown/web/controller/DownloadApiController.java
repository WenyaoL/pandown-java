package com.pan.pandown.web.controller;


import com.pan.pandown.service.download.DownloadService;
import com.pan.pandown.util.DTO.downloadApi.DownloadApiDTO;
import com.pan.pandown.util.DTO.downloadApi.GetDlinkDTO;
import com.pan.pandown.util.DTO.downloadApi.ListFileDTO;
import com.pan.pandown.util.baseResp.BaseResponse;
import com.pan.pandown.util.baseResp.FailResponse;
import com.pan.pandown.util.baseResp.SuccessResponse;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.*;

/**
 * @author yalier(wenyao)
 * @Description
 * @since 2023/4/28
 */
@RestController
@RequestMapping("/api")
public class DownloadApiController {

    @Autowired
    private DownloadService downloadService;

    @PostMapping("/list_all_files")
    @ApiOperation(value = "分享链接解析接口", notes = "分享链接解析(返回分享数据)", httpMethod = "POST")
    public BaseResponse listAllFiles(@RequestBody @Valid ListFileDTO listFileDTO) {
        return new SuccessResponse(downloadService.listShare(listFileDTO.getSurl(), listFileDTO.getPwd()));
    }

    @PostMapping("/list_dir")
    @ApiOperation(value = "分享目录解析接口", notes = "分享目录解析(列举指定目录)", httpMethod = "POST")
    public BaseResponse listShareDir(@RequestBody @Valid ListFileDTO listFileDTO) {
        Map map = downloadService.listDir(
                listFileDTO.getSurl(),
                listFileDTO.getPwd(),
                listFileDTO.getDir(),
                listFileDTO.getPage()
        );
        if (Objects.isNull(map)) return new FailResponse("获取失败");

        return new SuccessResponse(map);
    }

    @PostMapping("/getSignAndTime")
    @ApiOperation(value = "时间搓和签名请求接口", notes = "获取sign和Time", httpMethod = "POST")
    public BaseResponse getSignAndTime(@RequestBody @Valid ListFileDTO listFileDTO) {
        Map map = downloadService.getSignAndTime(listFileDTO.getSurl());
        if (Objects.isNull(map)) return new FailResponse("获取失败");
        return new SuccessResponse(map);
    }

    @PostMapping("/getDlink")
    @ApiOperation(value = "获取下载链接接口", notes = "获取下载链接", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shareid",value = "分享Id",dataType = "String"),
            @ApiImplicitParam(name = "sign",value = "签名",dataType = "String"),
            @ApiImplicitParam(name = "timestamp",value = "时间搓",dataType = "Long"),
            @ApiImplicitParam(name = "fsIdList",value = "分享文件列表",dataType = "List"),
            @ApiImplicitParam(name = "uk",value = "uk",dataType = "String"),
            @ApiImplicitParam(name = "seckey",value = "seckey",dataType = "String"),
    })
    public BaseResponse getDlink(@RequestBody @Valid GetDlinkDTO getDlinkDTO) {
        List list = (List) downloadService.getDlink(getDlinkDTO);
        if (Objects.isNull(list)) return new FailResponse("获取失败");
        return new SuccessResponse(new HashMap<String,Object>(){{put("list",list);}});
    }

    @PostMapping("/getSvipDlink")
    @ApiOperation(value = "直链请求接口", notes = "获取直链", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dlinkList",value = "dlinkList",dataType = "List")
    })
    public BaseResponse getSvipdLink(@RequestBody @Valid GetDlinkDTO getDlinkDTO) {
        List dlinkList = getDlinkDTO.getDlinkList();
        if(Objects.isNull(dlinkList)){
            ArrayList dlinks = (ArrayList) downloadService.getDlink(getDlinkDTO);
            Object svipDlink = downloadService.getSvipDlink(dlinks);
            return new SuccessResponse(svipDlink);
        }

        return new SuccessResponse(downloadService.getSvipDlink(dlinkList));
    }

}
