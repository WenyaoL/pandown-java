package com.pan.pandown;

import com.pan.pandown.dao.entity.PandownUser;
import com.pan.pandown.service.IPandownUserFlowService;
import com.pan.pandown.service.IPandownUserRoleService;
import com.pan.pandown.service.IPandownUserService;
import com.pan.pandown.util.mybatisPlus.SnowflakeGenerator;
import com.pan.pandown.web.PandownApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest(classes = PandownApplication.class)
public class UserGenerator {

    @Autowired
    IPandownUserService pandownUserService;

    @Autowired
    IPandownUserRoleService pandownUserRoleService;

    @Autowired
    IPandownUserFlowService pandownUserFlowService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private SnowflakeGenerator snowflakeGenerator;

    /*@Test
    public void generatAdmin() throws Exception {

        //保存用户信息
        pandownUserService.save(new PandownUser(0L,"admin",bCryptPasswordEncoder.encode("123456"),"admin@pan.com"));

        //初始化普通用户权限
        pandownUserRoleService.initCommonUserRole(0L);

        //初始化用户流量表
        pandownUserFlowService.initUserFlow(0L);
    }*/
}
