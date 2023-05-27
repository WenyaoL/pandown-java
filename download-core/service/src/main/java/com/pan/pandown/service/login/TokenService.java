package com.pan.pandown.service.login;


import com.auth0.jwt.interfaces.Claim;
import com.pan.pandown.dao.model.LoginUser;
import com.pan.pandown.util.redis.RedisService;
import com.pan.pandown.util.security.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * @author yalier(wenyao)
 */
@Service
public class TokenService {

    //token有效期
    @Value("${pandown.JWT.expireTime}")
    private Integer expireTime;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private RedisService redisService;


    /**
     * 令牌前缀,(redis存储时的前缀)
     */
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";

    /**
     * JWT存储数据的字段名
     */
    public static final String LOGIN_USER_KEY = "login_user_key";

    protected static final long MINUTE_TO_SECOND = 60;
    protected static final long HOUR_TO_MINUTE = 60;
    protected static final long DAY_TO_HOUR = 24;

    protected static final long DAY_TO_SECOND = MINUTE_TO_SECOND * HOUR_TO_MINUTE * DAY_TO_HOUR;

    /**
     * 刷新redis中的令牌有效期
     * @param loginUser
     */
    public void refreshToken(LoginUser loginUser){
        long expireTimeMillis = expireTime * DAY_TO_SECOND;
        redisService.set(
                LOGIN_TOKEN_KEY+loginUser.getPandownUser().getId(),
                loginUser,
                expireTimeMillis
        );
    }

    /**
     * 创建token，并在redis中创建关联
     * @param loginUser
     * @return
     */
    public String createToken(LoginUser loginUser){
        Long id = loginUser.getPandownUser().getId();
        HashMap<String, String> map = new HashMap<>();
        map.put(LOGIN_USER_KEY,id.toString());
        String token = jwtService.createToken(map);
        refreshToken(loginUser);
        return token;
    }



    /**
     * 验证当前loginUser对应的token是否存在
     * @param loginUser
     * @return
     */
    public boolean verifyToken(LoginUser loginUser){
        LoginUser o = (LoginUser) redisService.get(LOGIN_TOKEN_KEY + loginUser.getPandownUser().getId());
        if(Objects.nonNull(loginUser)) {
            refreshToken(loginUser);
            return true;
        }
        return false;
    }

    public Map<String, Claim> parseToken(String token){
        Map<String, Claim> claimMap = jwtService.parseToken(token);
        return claimMap;
    }

    public LoginUser verifyToken(String token){
        Map<String, Claim> claimMap = jwtService.parseToken(token);
        String s = claimMap.get(LOGIN_USER_KEY).asString();
        LoginUser loginUser = (LoginUser) redisService.get(LOGIN_TOKEN_KEY + s);

        if (Objects.nonNull(loginUser)) {
            refreshToken(loginUser);
        }
        return loginUser;
    }

    /**
     * 使一个token失效
     * @return
     */
    public boolean expireToken(String token){
        Map<String, Claim> claimMap = jwtService.parseToken(token);
        String s = claimMap.get(LOGIN_USER_KEY).asString();
        boolean b = redisService.hasKey(LOGIN_TOKEN_KEY + s);
        if (!b) return b;

        redisService.del(LOGIN_TOKEN_KEY + s);
        return true;
    }

}
