package com.pan.pandown.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pan.pandown.dao.entity.PandownUser;
import com.pan.pandown.util.PO.PandownUserDetailPO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yalier(wenyao)
 * @since 2023-04-20
 */
public interface PandownUserMapper extends BaseMapper<PandownUser> {

    PandownUser selectByUserName(String username);

    PandownUser selectByEmail(String email);

    Map getUserInfoById(Long id);

    List<String> getPermissionsByUserId(Long id);

    Page<PandownUserDetailPO> pageListUserInfo(Page page);

}
