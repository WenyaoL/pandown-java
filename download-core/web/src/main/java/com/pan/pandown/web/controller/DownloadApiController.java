package com.pan.pandown.web.controller;


import com.auth0.jwt.interfaces.Claim;
import com.pan.pandown.service.IPandownUserFlowService;
import com.pan.pandown.service.download.DownloadService;
import com.pan.pandown.service.login.TokenService;
import com.pan.pandown.util.DTO.downloadApi.*;
import com.pan.pandown.util.baseResp.BaseResponse;
import com.pan.pandown.util.baseResp.FailResponse;
import com.pan.pandown.util.baseResp.SuccessResponse;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;

/**
 * @author yalier(wenyao)
 * @Description
 * @since 2023/4/28
 */
@Slf4j
@RestController
@RequestMapping("/api/download")
public class DownloadApiController {

    @Autowired
    private IPandownUserFlowService pandownUserFlowService;

    @Autowired
    private DownloadService downloadService;

    @Autowired
    private TokenService tokenService;

    @Autowired(required = false)
    private HttpServletRequest httpServletRequest;

    @Autowired(required = false)
    private HttpServletResponse httpServletResponse;

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
        SignAndTimeDTO signAndTime = downloadService.getSignAndTime(listFileDTO.getSurl());
        if (Objects.isNull(signAndTime)) return new FailResponse("获取失败");
        return new SuccessResponse(signAndTime);
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
        List list = downloadService.getDlink(getDlinkDTO);
        if (Objects.isNull(list)) return new FailResponse("获取失败");
        return new SuccessResponse(new HashMap<String,Object>(){{put("list",list);}});
    }

    @PostMapping("/getSvipDlink")
    @ApiOperation(value = "直链请求接口", notes = "获取直链", httpMethod = "POST")
    public BaseResponse getSvipdLink(@RequestBody @Valid GetSvipDlinkDTO getSvipDlinkDTO) {
        String token = httpServletRequest.getHeader("token");
        Map<String, Claim> claimMap = tokenService.parseToken(token);
        Long userId = Long.parseLong(claimMap.get(TokenService.LOGIN_USER_KEY).asString());
        //流量状态是否可用
        boolean available = pandownUserFlowService.isAvailable(userId);
        if (!available) return new FailResponse("流量不足,或者用户流量被冻结");

        try {
            List svipDlink = downloadService.getSvipDlink(getSvipDlinkDTO, userId);
            return new SuccessResponse(svipDlink);
        }catch (Exception e){
            e.printStackTrace();
            return new FailResponse("流量不足,或者用户流量被冻结");
        }

    }

    @PostMapping("/getAllSvipDlink")
    @ApiOperation(value = "直链请求接口", notes = "获取直链", httpMethod = "POST")
    public BaseResponse getAllSvipdLink(@RequestBody ListAllSvipDlinkDTO listAllSvipDlinkDTO) {
        String token = httpServletRequest.getHeader("token");
        Map<String, Claim> claimMap = tokenService.parseToken(token);
        Long userId = Long.parseLong(claimMap.get(TokenService.LOGIN_USER_KEY).asString());
        //流量状态是否可用
        boolean available = pandownUserFlowService.isAvailable(userId);
        if (!available) return new FailResponse("流量不足,或者用户流量被冻结");

        try {
            List<ShareFileDTO> allSvipdLink = downloadService.getAllSvipdLink(listAllSvipDlinkDTO,userId);
            return new SuccessResponse(allSvipdLink);
        }catch (Exception e){
            e.printStackTrace();
            return new FailResponse("流量不足,或者用户流量被冻结");
        }

    }

}
