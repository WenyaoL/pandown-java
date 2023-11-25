package com.pan.pandown.service;

import com.pan.pandown.dao.entity.DbtableCommonAccount;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pan.pandown.dao.entity.DbtableSvip;
import com.pan.pandown.util.DTO.dbtableCommonAccountApi.CommonAccountNumDTO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yalier
 * @since 2023-05-16
 */
public interface IDbtableCommonAccountService extends IService<DbtableCommonAccount> {


    CommonAccountNumDTO getAccountNumDetail();

    /**
     * 列出所有账号信息
     * @return
     */
    List<DbtableCommonAccount> listAccountDetail();

    /**
     * 删除账号信息
     * @param dbtableCommonAccount
     * @return
     */
    boolean deleteAccountDetail(DbtableCommonAccount dbtableCommonAccount);

    /**
     * 跟新账号信息
     * @param dbtableCommonAccount
     * @return
     */
    boolean updateAccountDetail(DbtableCommonAccount dbtableCommonAccount);


    /**
     * 添加账号
     * @param dbtableCommonAccount
     * @return 添加的svip账号
     */
    DbtableCommonAccount addAccountDetail(DbtableCommonAccount dbtableCommonAccount);

    /**
     * 获取下一个普通账号的cookie字符串
     * @return
     */
    String getNextCommonAccountCookie();

    List<DbtableCommonAccount>listAvailableAccount();

    /**
     * 获取下一个普通账号
     * @return
     */
    DbtableCommonAccount getNextCommonAccount();

    /**
     * 冻结账号
     * @return
     */
    boolean freezeCommonAccount(Long id);
}
