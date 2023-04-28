package com.pan.pandown.service;

import com.pan.pandown.dao.entity.PandownUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pan.pandown.util.constants.RegisterCode;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yalier(wenyao)
 * @since 2023-04-20
 */
public interface IPandownUserService extends IService<PandownUser>, UserDetailsService {

    RegisterCode userRegister(String username, String password);

    String userLogin(String username, String password);
}
