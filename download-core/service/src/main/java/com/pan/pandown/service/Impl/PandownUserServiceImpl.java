package com.pan.pandown.service.Impl;

import com.pan.pandown.dao.entity.PandownUser;
import com.pan.pandown.dao.mapper.PandownUserMapper;
import com.pan.pandown.dao.model.LoginUser;
import com.pan.pandown.service.IPandownUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pan.pandown.service.login.TokenService;
import com.pan.pandown.util.constants.RegisterCode;
import com.pan.pandown.util.mybatisPlus.SnowflakeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yalier(wenyao)
 * @since 2023-04-20
 */
@Service
public class PandownUserServiceImpl extends ServiceImpl<PandownUserMapper, PandownUser> implements IPandownUserService {




    @Autowired
    private SnowflakeGenerator snowflakeGenerator;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        PandownUser pandownUser = query().eq("username",username).one();
        //PandownUser pandownUser = pandownUserMapper.selectByUserName(username);
        System.out.println("username:"+username);
        System.out.println("pandownUser:"+pandownUser);

        if (Objects.isNull(pandownUser)){
            throw new UsernameNotFoundException("未知用户");
        }else {
            List<GrantedAuthority> userAuthorityList = AuthorityUtils.createAuthorityList("USER");

            //User user = new User(pandownUser.getUsername(), pandownUser.getPassword(), userAuthorityList);
            return new LoginUser(pandownUser);
        }
    }


    @Override
    public RegisterCode userRegister(String username, String password) {
        Integer count = query().eq("username", username).count();
        if (count>0){
            return RegisterCode.FAIL_NAME_REPEAT;
        }

        if(save(new PandownUser(snowflakeGenerator.nextId(),username,password))){
            return RegisterCode.SUCCESS;
        }

        return RegisterCode.FAIL;
    }

    @Override
    public String userLogin(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("认证失败");
        }
        LoginUser principal = (LoginUser) authenticate.getPrincipal();
        String token = tokenService.createToken(principal);
        return token;
    }


}
