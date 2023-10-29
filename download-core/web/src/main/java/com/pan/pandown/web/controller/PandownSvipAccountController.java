package com.pan.pandown.web.controller;

import com.pan.pandown.dao.entity.DbtableSvip;
import com.pan.pandown.service.IDbtableSvipService;
import com.pan.pandown.util.DTO.pandownSvipAccountApi.AddSvipAccountDTO;
import com.pan.pandown.util.DTO.pandownSvipAccountApi.DeleteSvipAccountDTO;
import com.pan.pandown.util.DTO.pandownSvipAccountApi.SvipAccountNumDTO;
import com.pan.pandown.util.DTO.pandownSvipAccountApi.UpdateSvipAccountDTO;
import com.pan.pandown.util.baseResp.BaseResponse;
import com.pan.pandown.util.baseResp.FailResponse;
import com.pan.pandown.util.baseResp.SuccessResponse;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * @author yalier(wenyao)
 * @Description
 * @since 2023-05-11
 */
@Slf4j
@RestController
@RequestMapping("/api/svip")
public class PandownSvipAccountController {

    @Autowired
    private IDbtableSvipService dbtableSvipService;

    @PostMapping("/getAccountNum")
    @ApiOperation(value = "获取svip账号数接口", notes = "返回svip的账号数量和svip可用账号数量", httpMethod = "POST")
    public BaseResponse getAccountNum() {
        SvipAccountNumDTO numDetail = dbtableSvipService.getSvipNumDetail();
        return new SuccessResponse(numDetail);
    }

    @PostMapping("/getAccountDetail")
    @ApiOperation(value = "获取svip账号详情", notes = "返回svip的账号信息", httpMethod = "POST")
    public BaseResponse getAccountDetail() {
        List<DbtableSvip> svipDetail = dbtableSvipService.getSvipDetail();
        if(Objects.isNull(svipDetail)) return new FailResponse();
        return new SuccessResponse(svipDetail);
    }

    @PostMapping("/deleteAccount")
    @ApiOperation(value = "删除svip账号接口", notes = "删除svip账号接口", httpMethod = "POST")
    public BaseResponse deleteAccount(@RequestBody @Valid DeleteSvipAccountDTO deleteSvipAccountDTO) {
        boolean b = dbtableSvipService.deleteSvipDetail(deleteSvipAccountDTO.getId());
        if(b) return new SuccessResponse();
        else return new FailResponse();
    }

    @PostMapping("/addAccount")
    @ApiOperation(value = "添加svip账号接口", notes = "添加svip账号接口", httpMethod = "POST")
    public BaseResponse addAccount(@RequestBody @Valid AddSvipAccountDTO addSvipAccountDTO) {

        String svipBduss = addSvipAccountDTO.getSvipBduss();
        if (svipBduss.startsWith("BDUSS=")) svipBduss = svipBduss.replaceFirst("BDUSS=","");

        String svipStoken = addSvipAccountDTO.getSvipStoken();
        if (svipStoken.startsWith("STOKEN=")) svipStoken = svipStoken.replaceFirst("STOKEN=","");

        DbtableSvip svip = dbtableSvipService.addSvipDetail(svipBduss,svipStoken);
        if(Objects.isNull(svip)) return new FailResponse();
        return new SuccessResponse(svip);
    }

    @PostMapping("/updateAccount")
    @ApiOperation(value = "更新svip账号接口", notes = "更新svip账号接口", httpMethod = "POST")
    public BaseResponse updateAccount(@RequestBody @Valid UpdateSvipAccountDTO updateSvipAccountDTO) {

        String svipBduss = updateSvipAccountDTO.getSvipBduss();
        if (svipBduss.startsWith("BDUSS=")) svipBduss = svipBduss.replaceFirst("BDUSS=","");

        String svipStoken = updateSvipAccountDTO.getSvipStoken();
        if (svipStoken.startsWith("STOKEN=")) svipStoken = svipStoken.replaceFirst("STOKEN=","");

        boolean b = dbtableSvipService.updateSvipDetail(updateSvipAccountDTO.getId(),svipBduss,svipStoken);
        if (b){
            return new SuccessResponse();
        }else {
            return new FailResponse("跟新失败,请检测填写的bduss和stoken是否正确");
        }
    }
}
