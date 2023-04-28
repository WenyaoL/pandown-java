package com.pan.pandown.dao.mapper;

import com.pan.pandown.dao.entity.PandownUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wenyao
 * @since 2023-04-20
 */
public interface PandownUserMapper extends BaseMapper<PandownUser> {

    public PandownUser selectByUserName(String username);
}
