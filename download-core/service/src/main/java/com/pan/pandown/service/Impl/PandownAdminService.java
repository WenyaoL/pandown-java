package com.pan.pandown.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.pan.pandown.dao.entity.PandownRole;
import com.pan.pandown.dao.entity.PandownUser;
import com.pan.pandown.dao.entity.PandownUserFlow;
import com.pan.pandown.dao.entity.PandownUserRole;
import com.pan.pandown.service.*;
import com.pan.pandown.util.DTO.pandownAdminApi.AddUserDetailDTO;
import com.pan.pandown.util.DTO.pandownAdminApi.UpdateUserDetailDTO;
import com.pan.pandown.util.mybatisPlus.SnowflakeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @author yalier(wenyao)
 * @Description
 * @since 2023-05-24
 */
@Slf4j
@Service
public class PandownAdminService implements IPandownAdminService {

    @Autowired
    private IPandownUserService pandownUserService;

    @Autowired
    private IPandownRoleService pandownRoleService;

    @Autowired
    private IPandownUserRoleService pandownUserRoleService;

    @Autowired
    private IPandownUserFlowService pandownUserFlowService;

    @Autowired
    private SnowflakeGenerator snowflakeGenerator;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public IPage getUserInfoByPage(int pageNum, int pageSize) {
        IPage iPage = pandownUserService.pageUserInfo(pageNum,pageSize);
        return iPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserDetail(UpdateUserDetailDTO updateUserDetailDTO) {

        Long id = updateUserDetailDTO.getId();
        String username = updateUserDetailDTO.getUsername();
        String roleName = updateUserDetailDTO.getRoleName();
        int state = updateUserDetailDTO.getState();
        Long limitFlow = updateUserDetailDTO.getLimitFlow();

        //查询角色id
        LambdaQueryWrapper<PandownRole> lambdaQueryWrapper = new LambdaQueryWrapper<PandownRole>().eq(PandownRole::getRoleName, roleName);
        PandownRole pandownRole = pandownRoleService.getOne(lambdaQueryWrapper);
        Integer roleId = pandownRole.getId();

        //更新角色
        pandownUserRoleService.update()
                .set("role_id", roleId)
                .eq("user_id", id)
                .update();


        //查询出用户流量信息
        PandownUserFlow pandownUserFlow = pandownUserFlowService.getById(id);

        //跟新流量状态
        UpdateChainWrapper<PandownUserFlow> userFlowUpdateChainWrapper = pandownUserFlowService.update()
                .eq("id", id)
                .set("limit_flow", limitFlow);
        //如果流量超标,并且不是设置为冻结，自动设置状态未0
        if (limitFlow<pandownUserFlow.getParseFlow() && state!=-1){
            userFlowUpdateChainWrapper.set("state",0);
        }else{
            userFlowUpdateChainWrapper.set("state",state);
        }
        userFlowUpdateChainWrapper.update();

        //跟新用户信息
        pandownUserService.update()
                .set("username",username)
                .eq("id",id)
                .update();

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addUserDetail(AddUserDetailDTO addUserDetailDTO) {
        int state = addUserDetailDTO.getState();
        Long limitFlow = addUserDetailDTO.getLimitFlow();
        String email = addUserDetailDTO.getEmail();
        String username = addUserDetailDTO.getUsername();
        String password = addUserDetailDTO.getPassword();
        String roleName = addUserDetailDTO.getRoleName();
        long nextId = snowflakeGenerator.nextId();

        //添加用户
        PandownUser pandownUser = new PandownUser();
        pandownUser.setId(nextId);
        pandownUser.setEmail(email);
        pandownUser.setPassword(bCryptPasswordEncoder.encode(password));
        pandownUser.setUsername(username);
        pandownUserService.save(pandownUser);

        //初始流量信息
        pandownUserFlowService.initUserFlow(nextId,state,limitFlow);

        //查询角色id
        LambdaQueryWrapper<PandownRole> lambdaQueryWrapper = new LambdaQueryWrapper<PandownRole>().eq(PandownRole::getRoleName, roleName);
        PandownRole pandownRole = pandownRoleService.getOne(lambdaQueryWrapper);
        Integer roleId = pandownRole.getId();

        //添加用户角色
        PandownUserRole pandownUserRole = new PandownUserRole();
        pandownUserRole.setUserId(nextId);
        pandownUserRole.setRoleId(roleId);
        pandownUserRole.setCreator("admin");
        pandownUserRole.setCreateTime(LocalDateTime.now());
        pandownUserRoleService.save(pandownUserRole);

        return true;
    }
}
