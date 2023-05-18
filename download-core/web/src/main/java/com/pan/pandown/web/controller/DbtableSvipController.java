package com.pan.pandown.web.controller;

import com.pan.pandown.dao.entity.DbtableSvip;
import com.pan.pandown.service.IDbtableSvipService;
import com.pan.pandown.util.DTO.dbtableSvipApi.AddSvipDetailDTO;
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

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author yalier(wenyao)
 * @Description
 * @since 2023-05-11
 */
@Slf4j
@RestController
@RequestMapping("/api/dbtableSvip")
public class DbtableSvipController {

    @Autowired
    private IDbtableSvipService dbtableSvipService;

    @PostMapping("/getAccountNum")
    @ApiOperation(value = "获取svip账号数接口", notes = "返回svip的账号数量和svip可用账号数量", httpMethod = "POST")
    public BaseResponse getAccountNum() {
        Map numDetail = dbtableSvipService.getSvipNumDetail();
        return new SuccessResponse(numDetail);
    }

    @PostMapping("/getAccountDetail")
    @ApiOperation(value = "获取svip账号详情", notes = "返回svip的账号信息", httpMethod = "POST")
    public BaseResponse getAccountDetail() {
        List svipDetail = dbtableSvipService.getSvipDetail();
        if(Objects.isNull(svipDetail)) return new FailResponse();
        return new SuccessResponse(svipDetail);
    }

    @PostMapping("/deleteAccount")
    @ApiOperation(value = "删除svip账号接口", notes = "删除svip账号接口", httpMethod = "POST")
    public BaseResponse deleteAccount(@RequestBody DbtableSvip dbtableSvip) {
        boolean b = dbtableSvipService.deleteSvipDetail(dbtableSvip);
        if(b) return new SuccessResponse();
        else return new FailResponse();
    }

    @PostMapping("/addAccount")
    @ApiOperation(value = "添加svip账号接口", notes = "添加svip账号接口", httpMethod = "POST")
    public BaseResponse addAccount(@RequestBody DbtableSvip dbtableSvip) {

        DbtableSvip svip = dbtableSvipService.addSvipDetail(dbtableSvip);
        if(Objects.isNull(svip)) return new FailResponse();

        return new SuccessResponse(svip);
    }

    @PostMapping("/updateAccount")
    @ApiOperation(value = "更新svip账号接口", notes = "更新svip账号接口", httpMethod = "POST")
    public BaseResponse updateAccount(@RequestBody DbtableSvip dbtableSvip) {
        if (Objects.isNull(dbtableSvip.getId())) return new FailResponse("id不能为空");

        boolean b = dbtableSvipService.updateSvipDetail(dbtableSvip);
        if (b){
            return new SuccessResponse();
        }else {
            return new FailResponse("跟新失败,请检测填写的bduss和stoken是否正确");
        }
    }
}
