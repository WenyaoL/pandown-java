package com.pan.pandown.service.Impl;

import com.pan.pandown.dao.entity.PandownUserRole;
import com.pan.pandown.dao.mapper.PandownUserRoleMapper;
import com.pan.pandown.service.IPandownUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yalier
 * @since 2023-05-11
 */
@Service
public class PandownUserRoleServiceImpl extends ServiceImpl<PandownUserRoleMapper, PandownUserRole> implements IPandownUserRoleService {



    @Override
    public PandownUserRole initCommonUserRole(Long id) {
        PandownUserRole pandownUserRole = new PandownUserRole();
        pandownUserRole.setUserId(id);
        pandownUserRole.setRoleId(2);  //普通用户
        pandownUserRole.setCreator("system");
        pandownUserRole.setCreateTime(LocalDateTime.now());
        boolean save = save(pandownUserRole);
        if (!save) return null;
        return pandownUserRole;
    }
}
