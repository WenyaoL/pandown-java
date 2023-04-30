package com.pan.pandown.util.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.AllArgsConstructor;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.Map;
import java.util.Objects;


@AllArgsConstructor
public class JWTService {

    //签名
    private Algorithm algorithm;


    public String createToken(Map map) {
        String token = JWT.create()
                .withPayload(map)
                .sign(algorithm);
        return token;
    }

    public String createToken(Map map,Long expireTime) {
        String token = JWT.create()
                .withPayload(map)
                .withExpiresAt(new Date(System.currentTimeMillis()+expireTime))
                .sign(algorithm);
        return token;
    }

    public Map<String, Claim> parseToken(String token) throws RuntimeException {
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT verify = verifier.verify(token);
        //验证令牌是否过期
        if(Objects.nonNull(verify.getExpiresAt())){
            Assert.state(System.currentTimeMillis()<verify.getExpiresAt().getTime(),"The token has expired");
        }
        return verify.getClaims();
    }

}
