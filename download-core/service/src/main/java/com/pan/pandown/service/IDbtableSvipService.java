package com.pan.pandown.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pan.pandown.dao.entity.DbtableSvip;
import com.pan.pandown.util.DTO.dbtableSvipApi.AddSvipDetailDTO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yalier(wenyao)
 * @since 2023-04-17
 */
public interface IDbtableSvipService extends IService<DbtableSvip> {

    /**
     * 获取svip账号数量和svip可用账号数量
     * @return
     */
    Map getSvipNumDetail();

    /**
     * 列出所有账号信息
     * @return
     */
    List getSvipDetail();

    /**
     * 删除账号信息
     * @param dbtableSvip
     * @return
     */
    boolean deleteSvipDetail(DbtableSvip dbtableSvip);

    /**
     * 跟新账号信息
     * @param dbtableSvip
     * @return
     */
    boolean updateSvipDetail(DbtableSvip dbtableSvip);


    /**
     * 添加svip账号
     * @param dbtableSvip
     * @return 添加的svip账号
     */
    DbtableSvip addSvipDetail(DbtableSvip dbtableSvip);

    List<DbtableSvip> listAvailableSvip();

    /**
     * 获取缓存中的下一个svip
     * @return
     */
    DbtableSvip getNextSvip();

    /**
     * 冻结一个svip账号
     * @param id
     * @return
     */
    boolean freezeSvip(Long id);
}
