package com.pan.pandown.web.controller;


import com.pan.pandown.dao.entity.DbtableCommonAccount;
import com.pan.pandown.service.IDbtableCommonAccountService;
import com.pan.pandown.util.DTO.pandownCommonAccountApi.AddCommonAccountDTO;
import com.pan.pandown.util.DTO.pandownCommonAccountApi.CommonAccountNumDTO;
import com.pan.pandown.util.DTO.pandownCommonAccountApi.DeleteCommonAccountDTO;
import com.pan.pandown.util.DTO.pandownCommonAccountApi.UpdateCommonAccountDTO;
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

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yalier
 * @since 2023-05-16
 */
@Slf4j
@RestController
@RequestMapping("/api/common-account")
public class PandownCommonAccountController {

    @Autowired
    private IDbtableCommonAccountService dbtableCommonAccountService;

    @PostMapping("/getAccountNum")
    @ApiOperation(value = "获取普通账号数接口", notes = "返回普通账号数量和普通可用账号数量", httpMethod = "POST")
    public BaseResponse getAccountNum() {
        CommonAccountNumDTO numDetail = dbtableCommonAccountService.getAccountNumDetail();
        return new SuccessResponse(numDetail);
    }

    @PostMapping("/getAccountDetail")
    @ApiOperation(value = "获取普通账号信息接口", notes = "返回普通的账号信息", httpMethod = "POST")
    public BaseResponse getAccountDetail() {
        List<DbtableCommonAccount> dbtableCommonAccounts = dbtableCommonAccountService.listAccountDetail();
        if (Objects.isNull(dbtableCommonAccounts))return new FailResponse("查询失败");
        return new SuccessResponse(dbtableCommonAccounts);
    }

    @PostMapping("/deleteAccount")
    @ApiOperation(value = "删除账号接口", notes = "删除账号接口", httpMethod = "POST")
    public BaseResponse deleteAccount(@RequestBody @Valid DeleteCommonAccountDTO deleteCommonAccountDTO) {
        boolean b = dbtableCommonAccountService.deleteAccountDetail(deleteCommonAccountDTO.getId(), deleteCommonAccountDTO.getName());
        if (b) return new SuccessResponse();
        return new FailResponse();
    }

    @PostMapping("/addAccount")
    @ApiOperation(value = "添加账号接口", notes = "添加账号接口", httpMethod = "POST")
    public BaseResponse addAccount(@RequestBody @Valid AddCommonAccountDTO addCommonAccountDTO) {
        DbtableCommonAccount account = dbtableCommonAccountService.addAccountDetail(addCommonAccountDTO.getCookie());
        if (Objects.isNull(account)) return new FailResponse("添加失败");
        return new SuccessResponse(account);
    }

    @PostMapping("/updateAccount")
    @ApiOperation(value = "更新svip账号接口", notes = "更新svip账号接口", httpMethod = "POST")
    public BaseResponse updateAccount(@RequestBody @Valid UpdateCommonAccountDTO updateCommonAccountDTO) {
        boolean b = dbtableCommonAccountService.updateAccountDetail(
                updateCommonAccountDTO.getId(),
                updateCommonAccountDTO.getName(),
                updateCommonAccountDTO.getCookie());
        if (b) return new SuccessResponse();
        return new FailResponse();
    }

}
