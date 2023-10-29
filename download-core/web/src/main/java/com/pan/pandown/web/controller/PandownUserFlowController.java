package com.pan.pandown.web.controller;


import com.auth0.jwt.interfaces.Claim;
import com.pan.pandown.dao.entity.PandownUserFlow;
import com.pan.pandown.service.IPandownUserFlowService;
import com.pan.pandown.service.common.TokenService;
import com.pan.pandown.util.baseResp.BaseResponse;
import com.pan.pandown.util.baseResp.SuccessResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yalier
 * @since 2023-05-21
 */
@RestController
@RequestMapping("/api/user-flow")
public class PandownUserFlowController {

    @Autowired
    private IPandownUserFlowService pandownUserFlowService;

    @Autowired
    private TokenService tokenService;

    @Autowired(required = false)
    private HttpServletRequest httpServletRequest;

    @PostMapping("/getUserFlowInfo")
    @ApiOperation(value = "获取用户流量信息接口",notes = "获取用户流量信息",httpMethod = "POST")
    public BaseResponse getUserFlowInfo(){
        String token = httpServletRequest.getHeader("token");
        Map<String, Claim> claimMap = tokenService.parseToken(token);
        String userId = claimMap.get(TokenService.LOGIN_USER_KEY).asString();
        PandownUserFlow userFlowInfo = pandownUserFlowService.getUserFlowInfo(Long.parseLong(userId));
        return new SuccessResponse(userFlowInfo);
    }

}
