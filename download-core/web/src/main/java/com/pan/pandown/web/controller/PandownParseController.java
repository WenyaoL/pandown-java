package com.pan.pandown.web.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pan.pandown.dao.entity.PandownParse;
import com.pan.pandown.dao.entity.PandownUser;
import com.pan.pandown.dao.model.LoginUser;
import com.pan.pandown.service.IPandownParseService;
import com.pan.pandown.util.DTO.BasePageDTO;
import com.pan.pandown.util.DTO.pandownParseApi.DeleteParseHistoryDTO;
import com.pan.pandown.util.DTO.pandownUserApi.UserRegisterDTO;
import com.pan.pandown.util.baseResp.BaseResponse;
import com.pan.pandown.util.baseResp.FailResponse;
import com.pan.pandown.util.baseResp.SuccessResponse;
import com.pan.pandown.util.constants.RegisterCode;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 *  获取用户解析记录的RestController
 * </p>
 *
 * @author yalier
 * @since 2023-05-26
 */
@RestController
@RequestMapping("/api/pandownParse")
public class PandownParseController {

    @Autowired
    private IPandownParseService pandownParseService;

    @Autowired(required = false)
    private HttpServletRequest httpServletRequest;

    @PostMapping("/userHistory")
    @ApiOperation(value = "获取用户解析历史接口",notes = "用户解析历史",httpMethod = "POST")
    public BaseResponse userHistory(@RequestBody BasePageDTO basePageDTO){
        PandownUser userDetail = (PandownUser)httpServletRequest.getAttribute("userDetail");
        IPage<PandownParse> pandownParseIPage = pandownParseService.pageByUserId(userDetail.getId(), basePageDTO.getPageNum(), basePageDTO.getPageSize());
        return new SuccessResponse(pandownParseIPage);
    }

    @PostMapping("/deleteUserParseHistory")
    @ApiOperation(value = "获取用户解析历史接口",notes = "用户解析历史",httpMethod = "POST")
    public BaseResponse deleteUserParseHistory(@RequestBody DeleteParseHistoryDTO deleteParseHistoryDTO){
        PandownUser userDetail = (PandownUser)httpServletRequest.getAttribute("userDetail");
        List<Integer> deleteIds = deleteParseHistoryDTO.getDeleteIds();
        boolean b = pandownParseService.deleteByIdAndUserId(deleteIds, userDetail.getId());
        if (!b){
            return new FailResponse();
        }
        return new SuccessResponse();
    }
}
