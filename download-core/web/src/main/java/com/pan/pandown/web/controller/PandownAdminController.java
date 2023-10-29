package com.pan.pandown.web.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pan.pandown.service.IPandownAdminService;
import com.pan.pandown.service.IPandownUserService;
import com.pan.pandown.util.DTO.pandownAdminApi.AddUserDetailDTO;
import com.pan.pandown.util.DTO.pandownAdminApi.DeleteUserDetailDTO;
import com.pan.pandown.util.DTO.pandownAdminApi.UpdateUserDetailDTO;
import com.pan.pandown.util.baseResp.BaseResponse;
import com.pan.pandown.util.baseResp.FailResponse;
import com.pan.pandown.util.baseResp.SuccessResponse;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yalier(wenyao)
 * @Description
 * @since 2023-05-24
 */
@Slf4j
@RestController
@RequestMapping("/api/admin")
public class PandownAdminController {

    @Autowired
    private IPandownAdminService pandownAdminService;

    @Autowired
    private IPandownUserService pandownUserService;

    @Autowired(required = false)
    private HttpServletRequest httpServletRequest;

    @PostMapping("/getUserInfo")
    @ApiOperation(value = "获取所有用户信息接口",notes = "获取用户流量信息",httpMethod = "POST")
    public BaseResponse getUserInfo(){
        return null;
    }

    @PostMapping("/getUserInfoByPage")
    @ApiOperation(value = "获取所有用户信息分页接口",notes = "获取所有用户信息信息",httpMethod = "POST")
    public BaseResponse getUserInfoByPage(@RequestBody Map map){
        Integer pageNum = Integer.parseInt(map.get("pageNum").toString());
        Integer pageSize = Integer.parseInt(map.get("pageSize").toString());
        IPage userInfoByPage = pandownAdminService.getUserInfoByPage(pageNum,pageSize);
        return new SuccessResponse(userInfoByPage);
    }

    @PostMapping("/getUserNum")
    @ApiOperation(value = "获取所有用户信息分页接口",notes = "获取所有用户信息信息",httpMethod = "POST")
    public BaseResponse getUserNum(){
        int count = pandownUserService.count();
        return new SuccessResponse(new HashMap<String,Object>(){{put("count",count);}});
    }

    @PostMapping("/updateUserDetail")
    @ApiOperation(value = "更新用户详情接口",notes = "更新用户信息，包含用户角色，用户流量状态",httpMethod = "POST")
    public BaseResponse updateUserDetail(@RequestBody UpdateUserDetailDTO updateUserDetailDTO){
        boolean b = pandownAdminService.updateUserDetail(updateUserDetailDTO);
        if (b) return new SuccessResponse();
        return new FailResponse();
    }

    @PostMapping("/addUserDetail")
    @ApiOperation(value = "更新用户详情接口",notes = "更新用户信息，包含用户角色，用户流量状态",httpMethod = "POST")
    public BaseResponse addUserDetail(@RequestBody AddUserDetailDTO addUserDetailDTO){
        boolean b = pandownAdminService.addUserDetail(addUserDetailDTO);
        if (b) return new SuccessResponse();
        return new FailResponse();
    }

    @PostMapping("/deleteUserDetail")
    @ApiOperation(value = "删除用户详情接口",notes = "删除用户",httpMethod = "POST")
    public BaseResponse deleteUserDetail(@RequestBody DeleteUserDetailDTO deleteUserDetailDTO){
        boolean b = pandownAdminService.deleteUserDetail(deleteUserDetailDTO);
        if (b) return new SuccessResponse();
        return new FailResponse();
    }

}
