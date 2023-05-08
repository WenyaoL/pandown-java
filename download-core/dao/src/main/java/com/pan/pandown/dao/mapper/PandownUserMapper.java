package com.pan.pandown.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pan.pandown.dao.entity.PandownUser;

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
}
