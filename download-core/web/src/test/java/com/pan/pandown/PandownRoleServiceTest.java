package com.pan.pandown;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pan.pandown.dao.entity.PandownRole;
import com.pan.pandown.service.IPandownRoleService;
import com.pan.pandown.web.PandownApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author yalier(wenyao)
 * @Description
 * @since 2023-05-25
 */
@SpringBootTest(classes = PandownApplication.class)
public class PandownRoleServiceTest {

    @Autowired
    private IPandownRoleService pandownRoleService;

    @Test
    public void test(){
        LambdaQueryWrapper<PandownRole> lambdaQueryWrapper = new LambdaQueryWrapper<PandownRole>().eq(PandownRole::getRoleName, "admin");
        PandownRole entity = lambdaQueryWrapper.getEntity();
        PandownRole role = pandownRoleService.getOne(lambdaQueryWrapper);
        System.out.println(entity);
        System.out.println(role);

        QueryWrapper<PandownRole> pandownRoleQueryWrapper = new QueryWrapper<>();
        pandownRoleQueryWrapper.eq("role_name", "admin");
        PandownRole entity2 = pandownRoleQueryWrapper.getEntity();
        PandownRole role2 = pandownRoleService.getOne(pandownRoleQueryWrapper);
        System.out.println(entity2);
        System.out.println(role2);
    }
}
