package com.pan.pandown.service;

import com.pan.pandown.dao.entity.DbtableCommonAccount;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pan.pandown.util.DTO.pandownCommonAccountApi.CommonAccountNumDTO;

import java.util.List;

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
     * @return
     */
    boolean deleteAccountDetail(Long id,String name);

    /**
     * 跟新账号信息
     * @return
     */
    boolean updateAccountDetail(Long id,String name,String cookie);


    /**
     * 添加账号
     * @return 添加的svip账号
     */
    DbtableCommonAccount addAccountDetail(String cookie);

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
